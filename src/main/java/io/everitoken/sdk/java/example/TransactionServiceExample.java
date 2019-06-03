package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.Api;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.TransferFungibleAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.Transaction;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.dto.TransactionDigest;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.CustomNetParams;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class TransactionServiceExample {

    public static void main(String[] args) {
        try {
            signTransactionWithEvtwd();
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }

    public static void signTransactionWithEvtwd() throws ApiResponseException {
        // Requirements
        // - evtwd needs to be running
        // - wallet needs to be unlocked
        // - wallet must have corresponding public key

        // Init test net
        NetParams netParams = new CustomNetParams("http", "127.0.0.1", 8888, 15000);

        // init net for wallet
        NetParams walletParams = new CustomNetParams("http", "127.0.0.1", 9999, 15000);
        NodeInfo nodeInfo = new Api(netParams).getInfo();

        TransferFungibleAction transferFungibleAction = TransferFungibleAction.of("1.00000 S#347",
                "EVT6oMRJBkeDEe5bdjoHN1fLGKARwMBp2dBfFTkzK6ii9qVxpJrQT",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND", "test offline sign");

        // Init transaction service with net parameters
        TransactionService transactionService = TransactionService.of(netParams);

        // Construct transaction configuration
        TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                PublicKey.of("EVT6oMRJBkeDEe5bdjoHN1fLGKARwMBp2dBfFTkzK6ii9qVxpJrQT"), false, null);

        // Construct raw transaction
        Transaction rawTrx = transactionService.buildRawTransaction(trxConfig, Arrays.asList(transferFungibleAction),
                true);

        // get signatures from offline wallet rpc
        List<String> signatures = TransactionService.signTransaction(rawTrx,
                Arrays.asList("EVT6oMRJBkeDEe5bdjoHN1fLGKARwMBp2dBfFTkzK6ii9qVxpJrQT"), walletParams,
                nodeInfo.getChainId());

        // Push the raw transaction together with the signature to chain
        TransactionData push = transactionService.push(rawTrx, signatures);

        System.out.println(JSON.toJSONString(push));
    }

    public static void buildTransactionManually() throws ApiResponseException {
        // Init test net
        NetParams netParams = new TestNetNetParams();
        // get node info for building the transaction
        NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));

        // Init an action to be pushed to chain (e.g. transferFungible action)
        TransferFungibleAction transferFungibleAction = TransferFungibleAction.of("1.00000 S#20",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H", "test java");

        // Init transaction service with net parameters
        TransactionService transactionService = TransactionService.of(netParams);

        // Construct transaction configuration
        TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"), false, null);

        // Construct raw transaction
        Transaction rawTrx = transactionService.buildRawTransaction(trxConfig, Arrays.asList(transferFungibleAction),
                true);

        // Get signable digest from the chain
        TransactionDigest digest = TransactionService.getTransactionSignableDigest(netParams, rawTrx);

        // Use the digest to create corresponding signature for transaction
        List<String> signatures = TransactionService.signTransaction(digest.getDigest(),
                SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

        // Push the raw transaction together with the signature to chain
        TransactionData push = transactionService.push(rawTrx, signatures);

        System.out.println(JSON.toJSONString(push));
    }

    public static void buildTransactionAutomatically() throws ApiResponseException {
        // Init test net
        NetParams netParams = new TestNetNetParams();
        // get node info for building the transaction
        NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));

        // Init an action to be pushed to chain (e.g. transferFungible action)
        TransferFungibleAction transferFungibleAction = TransferFungibleAction.of("1.00000 S#20",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H", "test java");

        // Init transaction service with net parameters
        TransactionService transactionService = TransactionService.of(netParams);

        // Construct transaction configuration
        TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"), false, null);

        // Push the raw transaction together with the signature to chain
        TransactionData push = transactionService.push(trxConfig, Arrays.asList(transferFungibleAction), false,
                KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));

        System.out.println(JSON.toJSONString(push));
    }
}
