package io.everitoken.sdk.java.service;

import java.util.*;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.*;
import io.everitoken.sdk.java.abi.*;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.apiResource.SigningRequiredKeys;
import io.everitoken.sdk.java.apiResource.TransactionCommit;
import io.everitoken.sdk.java.apiResource.TransactionEstimatedCharge;
import io.everitoken.sdk.java.dto.*;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.EvtLinkStatusParam;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.KeyProviderInterface;
import io.everitoken.sdk.java.provider.SignProvider;
import io.everitoken.sdk.java.provider.SignProviderInterface;

public class TransactionService {
    private final NetParams netParams;
    private final AbiSerialisationProviderInterface actionSerializeProvider;

    private TransactionService(NetParams netParams, AbiSerialisationProviderInterface provider) {
        this.netParams = netParams;
        actionSerializeProvider = provider;
    }

    public static void main(String[] args) throws ApiResponseException {
        // build raw transaction
        NetParams netParams = new TestNetNetParams();
        NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));

        TransactionService transactionService = TransactionService.of(netParams);
        TransferFungibleAction transferFungibleAction = TransferFungibleAction.of("1.00000 S#20",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H", "test java");

        TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"), false, null);

        Transaction rawTrx = transactionService.buildRawTransaction(trxConfig, Arrays.asList(transferFungibleAction),
                true);

        TransactionDigest digest = TransactionService.getTransactionSignableDigest(netParams, rawTrx);
        List<String> signatures = TransactionService.signTransaction(digest.getDigest(),
                SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

        TransactionData push = transactionService.push(rawTrx, signatures);
        System.out.println(JSON.toJSONString(push));
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

    public static List<String> signTransaction(byte[] trxDigest, SignProviderInterface signProvider) {
        return signProvider.sign(trxDigest).stream().map(Signature::toString).collect(Collectors.toList());
    }

    public static TransactionDigest getTransactionSignableDigest(NetParams netParams, Transaction trx)
            throws ApiResponseException {
        return SignProvider.getSignableDigest(netParams, trx);
    }

    public Map<String, String> pushEveriPayAction(TransactionConfiguration trxConfig, EveriPayAction action,
            KeyProviderInterface keyProvider) throws ApiResponseException {
        push(trxConfig, Collections.singletonList(action), false, keyProvider);
        return new EvtLink(netParams).getStatusOfEvtLink(EvtLinkStatusParam.of(action.getLinkId()));
    }

    public Map<String, String> pushEveriPayAction(TransactionConfiguration trxConfig, EveriPayAction action,
            SignProviderInterface signProvider) throws ApiResponseException {
        push(trxConfig, Collections.singletonList(action), false, signProvider);
        return new EvtLink(netParams).getStatusOfEvtLink(EvtLinkStatusParam.of(action.getLinkId()));
    }

    public TransactionData push(Transaction trx, List<String> signatures) throws ApiResponseException {
        return new TransactionCommit().request(RequestParams.of(netParams, () -> {
            JSONObject payload = new JSONObject();
            payload.put("compression", "none");
            payload.put("transaction", JSONObject.parseObject(JSON.toJSONString(trx)));
            payload.put("signatures", signatures);
            return payload.toString();
        }));
    }

    public TransactionData push(TransactionConfiguration trxConfig, List<? extends Abi> actions, boolean checkEveriPay,
            KeyProviderInterface keyProvider) throws ApiResponseException {
        return push(trxConfig, actions, checkEveriPay, SignProvider.of(keyProvider));
    }

    public TransactionData push(TransactionConfiguration trxConfig, List<? extends Abi> actions, boolean checkEveriPay,
            SignProviderInterface signProvider) throws ApiResponseException {

        Transaction rawTrx = buildRawTransaction(trxConfig, actions, checkEveriPay);
        TransactionDigest digest = getTransactionSignableDigest(netParams, rawTrx);
        List<String> signatures = TransactionService.signTransaction(digest.getDigest(), signProvider);

        return push(rawTrx, signatures);
    }

    public Transaction buildRawTransaction(TransactionConfiguration trxConfig, List<? extends Abi> actions,
            boolean checkEveriPay) {

        List<String> serializedActions = actions.stream().map(action -> action.serialize(actionSerializeProvider))
                .collect(Collectors.toList());

        boolean hasEveriPay = actions.stream().anyMatch(action -> action.getName().equals("everipay"));

        if (checkEveriPay && hasEveriPay) {
            throw new IllegalArgumentException("EveriPay action is found in this action list, use "
                    + "\"pushEveriPayAction\" for everipay action instead.");
        }

        return new Transaction(serializedActions, trxConfig.getExpiration(), trxConfig.getBlockNum(),
                trxConfig.getBlockPrefix(), trxConfig.getMaxCharge(), trxConfig.getPayer());
    }

    public Charge estimateCharge(TransactionConfiguration trxConfig, List<? extends Abi> actions,
            List<PublicKey> availablePublicKeys) throws ApiResponseException {
        Transaction rawTx = buildRawTransaction(trxConfig, actions, false);

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
