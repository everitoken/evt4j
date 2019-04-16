package io.everitoken.sdk.java.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import io.everitoken.sdk.java.Api;
import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.PrivateKey;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.Signature;
import io.everitoken.sdk.java.Utils;
import io.everitoken.sdk.java.abi.Abi;
import io.everitoken.sdk.java.abi.AbiSerialisationProviderInterface;
import io.everitoken.sdk.java.abi.EveriPayAction;
import io.everitoken.sdk.java.abi.RemoteAbiSerialisationProvider;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.apiResource.SigningRequiredKeys;
import io.everitoken.sdk.java.apiResource.TransactionCommit;
import io.everitoken.sdk.java.apiResource.TransactionEstimatedCharge;
import io.everitoken.sdk.java.dto.Charge;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.Transaction;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.EvtLinkStatusParam;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.provider.KeyProviderInterface;
import io.everitoken.sdk.java.provider.SignProvider;

public class TransactionService {
    private final NetParams netParams;
    private final AbiSerialisationProviderInterface actionSerializeProvider;

    private TransactionService(NetParams netParams, AbiSerialisationProviderInterface provider) {
        this.netParams = netParams;
        actionSerializeProvider = provider;
    }

    @NotNull
    @Contract("_ -> new")
    public static TransactionService of(NetParams netParams) {
        return new TransactionService(netParams, new RemoteAbiSerialisationProvider(netParams));
    }

    @NotNull
    @Contract("_, _ -> new")
    public static TransactionService of(NetParams netParams, AbiSerialisationProviderInterface provider) {
        return new TransactionService(netParams, provider);
    }

    @NotNull
    public static String getExpirationTime(String referenceTime) {
        return getExpirationTime(referenceTime, null);
    }

    @NotNull
    public static String getExpirationTime(@NotNull String referenceTime, @Nullable String type) {
        Objects.requireNonNull(referenceTime);

        int TIMESTAMP_LENGTH = 19;
        Duration expireDuration = Duration.standardSeconds(100);

        if (type != null && type.equals("everipay")) {
            expireDuration = Duration.standardSeconds(10);
        }

        DateTime dateTime = Utils.getCorrectedTime(referenceTime);
        DateTime expiration = dateTime.plus(expireDuration);

        return expiration.toString().substring(0, TIMESTAMP_LENGTH);
    }

    public Map<String, String> pushEveriPayAction(TransactionConfiguration trxConfig, EveriPayAction action)
            throws ApiResponseException {
        pushActions(trxConfig, Collections.singletonList(action), false);
        return new EvtLink(netParams).getStatusOfEvtLink(EvtLinkStatusParam.of(action.getLinkId()));
    }

    public TransactionData push(TransactionConfiguration trxConfig, List<? extends Abi> actions)
            throws ApiResponseException {
        return pushActions(trxConfig, actions, true);
    }

    private TransactionData pushActions(TransactionConfiguration trxConfig, List<? extends Abi> actions,
            boolean checkEveriPay) throws ApiResponseException {
        Transaction rawTx = buildRawTransaction(trxConfig, actions);

        // get signable digest from node
        byte[] digest = SignProvider.getSignableDigest(netParams, rawTx);
        boolean hasEveryPay = false;

        if (checkEveriPay) {
            hasEveryPay = actions.stream().anyMatch(action -> action.getName().equals("everipay"));
        }

        if (hasEveryPay) {
            throw new IllegalArgumentException("EveriPay action is found in this action list, use "
                    + "\"pushEveriPayAction\" for everipay action instead.");
        }

        return new TransactionCommit().request(RequestParams.of(netParams, () -> {
            JSONObject payload = new JSONObject();
            payload.put("compression", "none");
            payload.put("transaction", JSONObject.parseObject(JSON.toJSONString(rawTx)));
            payload.put("signatures", trxConfig.getSignProvider().sign(digest).stream().map(Signature::toString)
                    .collect(Collectors.toList()));
            return payload.toString();
        }));
    }

    public Charge estimateCharge(TransactionConfiguration trxConfig, List<? extends Abi> actions,
            List<PublicKey> availablePublicKeys) throws ApiResponseException {
        Transaction rawTx = buildRawTransaction(trxConfig, actions);

        JSONObject txObj = JSONObject.parseObject(JSON.toJSONString(rawTx));
        List<String> requiredKeys = new SigningRequiredKeys().request(RequestParams.of(netParams, () -> {
            JSONObject json = new JSONObject();
            json.put("transaction", txObj);
            json.put("available_keys",
                    availablePublicKeys.stream().map(PublicKey::toString).collect(Collectors.toList()));
            return json.toString();
        }));

        return new TransactionEstimatedCharge().request(RequestParams.of(netParams, () -> {
            JSONObject json = new JSONObject();
            json.put("transaction", txObj);
            json.put("sign_num", requiredKeys.size());
            return json.toString();
        }));
    }

    public Transaction buildRawTransaction(TransactionConfiguration trxConfig, List<? extends Abi> actions)
            throws ApiResponseException {
        List<String> serializedActions = actions.stream().map(action -> action.serialize(actionSerializeProvider))
                .collect(Collectors.toList());

        boolean hasEveryPay = actions.stream().anyMatch(action -> action.getName().equals("everipay"));

        if (hasEveryPay && trxConfig.getExpiration() != null) {
            throw new IllegalArgumentException("Expiration can not be set in a transaction including a everipay "
                    + "action, the expiration must be set automatically by SDK");
        }

        NodeInfo res = (new Info()).request(RequestParams.of(netParams));

        int refBlockNumber = Utils.getNumHash(res.getLastIrreversibleBlockId());
        long refBlockPrefix = Utils.getLastIrreversibleBlockPrefix(res.getLastIrreversibleBlockId());
        String expirationDateTime = trxConfig.getExpiration();

        if (expirationDateTime == null) {
            expirationDateTime = TransactionService.getExpirationTime(res.getHeadBlockTime(),
                    hasEveryPay ? "everipay" : null);
        }

        return new Transaction(serializedActions, expirationDateTime, refBlockNumber, refBlockPrefix,
                trxConfig.getMaxCharge(), trxConfig.getPayer());
    }

    public List<Signature> getSignaturesByProposalName(KeyProviderInterface keyProvider, String proposalName)
            throws ApiResponseException {
        // get proposal transactions
        Api api = new Api(netParams);

        String suspendedProposalRaw = api.getSuspendedProposal(proposalName);
        JSONObject trxRaw = JSONObject.parseObject(suspendedProposalRaw).getJSONObject("trx");

        // get the signable digest
        byte[] trxSignableDigest = api.getSignableDigest(trxRaw.toString());

        // get required keys for suspended proposals
        List<String> publicKeys = keyProvider.get().stream().map(PrivateKey::toPublicKey).map(PublicKey::toString)
                .collect(Collectors.toList());

        JSONArray suspendRequiredArray = api.getSuspendRequiredKeys(proposalName, publicKeys);
        List<String> suspendRequiredKeys = new ArrayList<>();

        for (int i = 0; i < suspendRequiredArray.size(); i++) {
            suspendRequiredKeys.add((String) suspendRequiredArray.get(i));
        }

        // sign it to get the signatures
        return keyProvider.get().stream().filter(privateKey -> {
            PublicKey publicKey = privateKey.toPublicKey();
            return suspendRequiredKeys.contains(publicKey.toString());
        }).map(privateKey -> Signature.signHash(trxSignableDigest, privateKey)).collect(Collectors.toList());
    }
}
