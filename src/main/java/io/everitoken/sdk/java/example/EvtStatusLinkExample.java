package io.everitoken.sdk.java.example;

import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.abi.EveriPayAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;

public class EvtStatusLinkExample {
    public static void main(String[] args) {
        int maxAmount = 100;
        NetParams netParams = new TestNetNetParams();
        EvtLink evtLink = new EvtLink(netParams);
        String linkId = EvtLink.getUniqueLinkId();
        EvtLink.EveriPayParam everiPayParam1 = new EvtLink.EveriPayParam(1, linkId, maxAmount);
        String payText = evtLink.getEvtLinkForEveriPay(everiPayParam1,
                SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

        EveriPayAction action = EveriPayAction.of(payText, "0.00001 " + "S#1",
                "EVT5cd4a3RyaVoubc4w3j3Z3YvCJgtKZPRdJHDdk7wVsMbc3yEH5U");

        // try {
        // TransactionService transactionService = TransactionService.of(netParams);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        //
        // Map<String, String> statusOfEvtLink =
        // transactionService.pushEveriPayAction(trxConfig, action);
        // System.out.println(statusOfEvtLink);
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getCause().getMessage());
        // }
    }
}
