package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.TransferFungibleAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.Transaction;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.dto.TransactionDigest;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
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
            buildTransactionAutomatically();
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
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
