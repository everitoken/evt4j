package io.everitoken.sdk.java.example;

import java.util.Map;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.EvtLink;
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

public class EvtStatusLinkExample {
    public static void main(String[] args) {
        int maxAmount = 100;
        NetParams netParams = new TestNetNetParams();

        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
        EvtLink evtLink = new EvtLink(netParams);
        String linkId = EvtLink.getUniqueLinkId();
        EvtLink.EveriPayParam everiPayParam1 = new EvtLink.EveriPayParam(1, linkId, maxAmount);
        String payText = evtLink.getEvtLinkForEveriPay(everiPayParam1, SignProvider.of(keyProvider));

        EveriPayAction action = EveriPayAction.of(payText, "0.00001 " + "S#1",
                "EVT6TMdjW6v5YkxDUegd6733bnKVvPydaiG4zfeJGU8CWw1AuRnCq");

        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"), true, null);

            Map<String, String> statusOfEvtLink = transactionService.pushEveriPayAction(trxConfig, action, keyProvider);
            System.out.println(statusOfEvtLink);
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
