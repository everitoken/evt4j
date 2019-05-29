package io.everitoken.sdk.java.example;

import java.util.Map;

import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.EveriPayAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class EveriPayActionExample {
    public static void main(String[] args) {

        String uniqueLinkId = EvtLink.getUniqueLinkId();
        int symbolId = 1;
        int maxAmount = 100;

        NetParams netParams = new TestNetNetParams();
        EvtLink evtLink = new EvtLink(netParams);
        EvtLink.EveriPayParam everiPayParam = new EvtLink.EveriPayParam(symbolId, uniqueLinkId, maxAmount);

        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
        String payText = evtLink.getEvtLinkForEveriPay(everiPayParam, SignProvider.of(keyProvider));

        EveriPayAction action = EveriPayAction.of(payText, "0.00001 " + "S#1",
                "EVT5cd4a3RyaVoubc4w3j3Z3YvCJgtKZPRdJHDdk7wVsMbc3yEH5U");

        try {

            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"), true, null);

            Map<String, String> rst = transactionService.pushEveriPayAction(trxConfig, action, keyProvider);

            System.out.println(rst);
        } catch (ApiResponseException ex) {
            System.out.println(ex.getCause().getMessage());
        }
    }
}
