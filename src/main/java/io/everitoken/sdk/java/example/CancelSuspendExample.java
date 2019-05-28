package io.everitoken.sdk.java.example;

import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionService;

public class CancelSuspendExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();
        TransactionService transactionService = TransactionService.of(netParam);
        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");

        // try {
        // CancelSuspendAction action = CancelSuspendAction.of("tp17");
        //
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // keyProvider);
        //
        // TransactionData txData = transactionService.push(trxConfig,
        // Arrays.asList(action));
        // System.out.println(txData.getTrxId());
        //
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
