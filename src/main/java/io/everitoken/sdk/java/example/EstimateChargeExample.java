package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.Collections;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.abi.IssueTokenAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class EstimateChargeExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();
        IssueTokenAction issueTokenAction = IssueTokenAction.of("test1123", Arrays.asList("t3"),
                Collections.singletonList(Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

        // try {
        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        //
        // Charge charge = transactionService.estimateCharge(trxConfig,
        // Arrays.asList(issueTokenAction),
        // Arrays.asList(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));
        //
        // System.out.println(JSON.toJSONString(charge));
        //
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
