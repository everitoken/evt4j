package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.Collections;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.abi.IssueTokenAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class IssueTokenExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();
        IssueTokenAction issueTokenAction = IssueTokenAction.of("test1126", Arrays.asList("t1", "t2"),
                Collections.singletonList(Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

        // try {
        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        //
        // TransactionData txData = transactionService.push(trxConfig,
        // Arrays.asList(issueTokenAction));
        // System.out.println(txData.getTrxId());
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
