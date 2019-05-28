package io.everitoken.sdk.java.example;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.Symbol;
import io.everitoken.sdk.java.abi.UpdateFungibleAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class UpdateFungibleExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();

        UpdateFungibleAction updateFungibleAction = UpdateFungibleAction.of(Symbol.of(20, 5),
                JSONObject.parseObject("{\"name\":\"issue\",\"threshold\":2,\"authorizers\":[{\"ref\":\"[A] "
                        + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"weight\":" + "1}, {\"ref\":\"[A] "
                        + "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H\",\"weight\":" + "1}]}"),
                null);
        // try {
        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        //
        // TransactionData txData = transactionService.push(trxConfig,
        // Arrays.asList(updateFungibleAction));
        // System.out.println(txData.getTrxId());
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
