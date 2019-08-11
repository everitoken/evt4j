package io.everitoken.sdk.java.example;

import java.util.Arrays;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.abi.EveriPassAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class EveriPassActionExample {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();
        EvtLink evtLink = new EvtLink(netParams);
        // make sure the domain and token you use exist and has correct authorize keys
        EvtLink.EveriPassParam everiPassParam = new EvtLink.EveriPassParam(true, "test1125", "t20");
        String passText = evtLink.getEvtLinkForEveriPass(everiPassParam,
                SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

        EveriPassAction everiPassAction = EveriPassAction.of(passText);

        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));

            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(everiPassAction), false,
                    KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
            System.out.println(txData.getTrxId());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
