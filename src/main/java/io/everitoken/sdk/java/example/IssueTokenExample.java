package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.Collections;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.abi.IssueTokenAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class IssueTokenExample {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();
        IssueTokenAction issueTokenAction = IssueTokenAction.of("test1126", Arrays.asList("t3", "t4"),
                Collections.singletonList(Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));

            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(issueTokenAction), false,
                    keyProvider);
            System.out.println(txData.getTrxId());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
