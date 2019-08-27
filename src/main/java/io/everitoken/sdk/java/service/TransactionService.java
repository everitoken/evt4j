package io.everitoken.sdk.java.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.*;
import io.everitoken.sdk.java.abi.Abi;
import io.everitoken.sdk.java.abi.AbiSerialisationProviderInterface;
import io.everitoken.sdk.java.abi.EveriPayAction;
import io.everitoken.sdk.java.abi.RemoteAbiSerialisationProvider;
import io.everitoken.sdk.java.apiResource.SigningRequiredKeys;
import io.everitoken.sdk.java.apiResource.TransactionCommit;
import io.everitoken.sdk.java.apiResource.TransactionEstimatedCharge;
import io.everitoken.sdk.java.apiResource.TransactionSignature;
import io.everitoken.sdk.java.dto.Charge;
import io.everitoken.sdk.java.dto.Transaction;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.dto.TransactionDigest;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.EvtLinkStatusParam;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
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
        List<Signature> signatures = signProvider.sign(trxDigest);
        List<String> rtn = new ArrayList<>();

        for (int i = 0; i < signatures.size(); i++) {
            rtn.add(signatures.get(i).toString());
        }

        return rtn;
    }

    public static List<String> signTransaction(Transaction trx, List<String> keys, NetParams walletNetParams,
            String chainId) throws ApiResponseException {

        JSONObject res = new TransactionSignature().request(RequestParams.of(walletNetParams, () -> {
            JSONArray body = new JSONArray();
            body.add(trx);
            body.add(keys);
            body.add(chainId);
            return body.toString();
        }));

        List<String> signatures = new ArrayList<>();

        JSONArray signaturesList = res.getJSONArray("signatures");
        for (int i = 0; i < signaturesList.size(); i++) {
            signatures.add(signaturesList.getString(i));
        }

        return signatures;
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

        boolean hasEveriPay = false;

        List<String> serializedActions = new ArrayList<>();

        for (int i = 0; i < actions.size(); i++) {

            if (!hasEveriPay) {
                hasEveriPay = actions.get(i).getName().equals("everipay");
            }

            serializedActions.add(actions.get(i).serialize(actionSerializeProvider));
        }

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

        List<String> publicKeys = new ArrayList<>();

        for (int i = 0; i < availablePublicKeys.size(); i++) {
            publicKeys.add(availablePublicKeys.get(i).toString());
        }

        List<String> requiredKeys = new SigningRequiredKeys().request(RequestParams.of(netParams, () -> {
            JSONObject json = new JSONObject();
            json.put("transaction", txObj);
            json.put("available_keys", publicKeys);
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
        List<String> publicKeys = new ArrayList<>();
        List<PrivateKey> privateKeys = keyProvider.get();

        for (int i = 0; i < privateKeys.size(); i++) {
            publicKeys.add(privateKeys.get(i).toPublicKey().toString());
        }

        JSONArray suspendRequiredArray = api.getSuspendRequiredKeys(proposalName, publicKeys);
        List<String> suspendRequiredKeys = new ArrayList<>();

        for (int i = 0; i < suspendRequiredArray.size(); i++) {
            suspendRequiredKeys.add((String) suspendRequiredArray.get(i));
        }

        List<Signature> signatures = new ArrayList<>();

        for (int i = 0; i < privateKeys.size(); i++) {
            PrivateKey privateKey = privateKeys.get(i);
            PublicKey publicKey = privateKey.toPublicKey();

            if (suspendRequiredKeys.contains(publicKey.toString())) {
                signatures.add(Signature.signHash(trxSignableDigest, privateKey));
            }
        }

        return signatures;
    }
}
