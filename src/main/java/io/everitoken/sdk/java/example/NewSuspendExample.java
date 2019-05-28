package io.everitoken.sdk.java.example;

import io.everitoken.sdk.java.abi.Evt2PevtAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class NewSuspendExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();
        Evt2PevtAction evt2PevtAction = Evt2PevtAction.of("0.00001 S#1",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H", "test java");

        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        // try {
        // Transaction trx = transactionService.buildRawTransaction(trxConfig,
        // Arrays.asList(evt2PevtAction), false);
        // NewSuspendAction action = NewSuspendAction.of("tp17",
        // "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND", trx);
        //
        // TransactionData push = transactionService.push(trxConfig,
        // Arrays.asList(action));
        // System.out.println(push.getTrxId());
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
