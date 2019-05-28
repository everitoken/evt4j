package io.everitoken.sdk.java.example;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.Symbol;
import io.everitoken.sdk.java.abi.NewFungibleAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class NewFungibleExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();

        NewFungibleAction newFungibleAction = NewFungibleAction.of(Symbol.of(346, 5), ".JAVA",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                JSONObject.parseObject("{\"name\":\"issue\",\"threshold\":1,\"authorizers\":[{\"ref\":\"[A] "
                        + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"weight\":" + "1}]}"),
                JSONObject.parseObject("{\"name\":\"manage\",\"threshold\":1,\"authorizers\":[{\"ref\":\"[A] "
                        + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"weight\":" + "1}]}"),
                "10000000000.00000 S#346");
        // try {
        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        //
        // TransactionData txData = transactionService.push(trxConfig,
        // Arrays.asList(newFungibleAction));
        // System.out.println(txData.getTrxId());
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
