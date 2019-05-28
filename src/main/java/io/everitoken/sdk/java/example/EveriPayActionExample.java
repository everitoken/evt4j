package io.everitoken.sdk.java.example;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.abi.EveriPayAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;

public class EveriPayActionExample {
    public static void main(String[] args) {

        String uniqueLinkId = EvtLink.getUniqueLinkId();
        int symbolId = 1;
        int maxAmount = 100;

        NetParams netParams = new TestNetNetParams();
        EvtLink evtLink = new EvtLink(netParams);
        EvtLink.EveriPayParam everiPayParam = new EvtLink.EveriPayParam(symbolId, uniqueLinkId, maxAmount);

        String payText = evtLink.getEvtLinkForEveriPay(everiPayParam,
                SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

        EveriPayAction action = EveriPayAction.of(payText, "0.00001 " + "S#1",
                "EVT5cd4a3RyaVoubc4w3j3Z3YvCJgtKZPRdJHDdk7wVsMbc3yEH5U");
        System.out.println(String.format("%s: %s", "everipayAction", JSON.toJSONString(action)));

        // try {
        // TransactionService transactionService = TransactionService.of(netParams);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"), null);
        //
        // Map<String, String> rst = transactionService.pushEveriPayAction(trxConfig,
        // action);
        // System.out.println(rst);
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getCause().getMessage());
        // }
    }
}
