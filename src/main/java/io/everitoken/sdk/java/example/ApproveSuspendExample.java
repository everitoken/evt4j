package io.everitoken.sdk.java.example;

import java.util.List;
import java.util.stream.Collectors;

import io.everitoken.sdk.java.Signature;
import io.everitoken.sdk.java.abi.ApproveSuspendAction;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionService;

public class ApproveSuspendExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();
        TransactionService transactionService = TransactionService.of(netParam);
        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");

        try {
            List<Signature> signatures = transactionService.getSignaturesByProposalName(keyProvider, "tp16");

            ApproveSuspendAction action = ApproveSuspendAction.of("tp16",
                    signatures.stream().map(Signature::toString).collect(Collectors.toList()));

            // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
            // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
            // keyProvider);
            //
            // System.out.println(JSON.toJSONString(action));
            // TransactionData txData = transactionService.push(trxConfig,
            // Arrays.asList(action));
            // System.out.println(txData.getTrxId());

        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
