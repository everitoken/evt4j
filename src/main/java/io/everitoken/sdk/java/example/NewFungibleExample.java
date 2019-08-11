package io.everitoken.sdk.java.example;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Symbol;
import io.everitoken.sdk.java.abi.NewFungibleAction;
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

public class NewFungibleExample {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();

        NewFungibleAction newFungibleAction = NewFungibleAction.of(Symbol.of(347, 5), ".whatnot",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                JSONObject.parseObject("{\"name\":\"issue\",\"threshold\":1,\"authorizers\":[{\"ref\":\"[A] "
                        + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"weight\":" + "1}]}"),
                JSONObject.parseObject("{\"name\":\"manage\",\"threshold\":1,\"authorizers\":[{\"ref\":\"[A] "
                        + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"weight\":" + "1}]}"),
                "10000000000.00000 S#347");
        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));

            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(newFungibleAction), false,
                    keyProvider);
            System.out.println(txData.getTrxId());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
