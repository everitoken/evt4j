package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.Api;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.TransferFungibleAction;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.Transaction;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.CustomNetParams;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class SignTransactionWithEvtwd {
    public static void main(String[] args) {
        try {
            buildTransactionManually();
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }

    public static void buildTransactionManually() throws ApiResponseException {
        // Init test net

        NetParams netParams = new CustomNetParams("http", "127.0.0.1", 8888, 15000);
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

        List<String> signatures = TransactionService.signTransaction(rawTrx,
                Arrays.asList("EVT6oMRJBkeDEe5bdjoHN1fLGKARwMBp2dBfFTkzK6ii9qVxpJrQT"), walletParams,
                nodeInfo.getChainId());

        // Push the raw transaction together with the signature to chain
        TransactionData push = transactionService.push(rawTrx, signatures);

        System.out.println(JSON.toJSONString(push));
    }
}
