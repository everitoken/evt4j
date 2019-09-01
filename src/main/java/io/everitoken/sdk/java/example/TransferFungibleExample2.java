package io.everitoken.sdk.java.example;

import java.util.Arrays;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.abi.TransferFungibleAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class TransferFungibleExample2 {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();
        String bnbAddress = "tbnb1ffxzdd37u7djmkkafqvdldwxaleg0zcrp0w8hn";
        String evtPayerAddress = "EVT6z5r4rnTKMZ5KWZJFfivJtd7z4jjhrMApks6jD6NzF5f2AZ2ga";
        String evtPayerPrivateKey = "5Jdn3JdGTUMoeNtwj85bZySBCCXphZBDSA2RcF8PeaBFvX8Gxax";
        String evtSwapAddress = "EVT7f6pEXvD8E2mbytzkirYqv9DBxEd7ebDv9TJpQ6kuKPemLtKUY";

        TransferFungibleAction transferFungibleAction = TransferFungibleAction.of("1.10000 S#1", evtPayerAddress,
                evtSwapAddress, bnbAddress);

        TransferFungibleAction transferFungibleAction1 = TransferFungibleAction.of("1.10000 S#1", evtPayerAddress,
                evtSwapAddress, bnbAddress);

        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000000000000L,
                    Address.of(evtPayerAddress));

            TransactionData txData = transactionService.push(trxConfig,
                    Arrays.asList(transferFungibleAction, transferFungibleAction1), true,
                    KeyProvider.of(evtPayerPrivateKey));

            System.out.println(txData.getTrxId());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
