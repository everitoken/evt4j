package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Signature;
import io.everitoken.sdk.java.abi.ApproveSuspendAction;
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

public class ApproveSuspendExample {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();
        TransactionService transactionService = TransactionService.of(netParams);
        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");

        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            List<Signature> signatures = transactionService.getSignaturesByProposalName(keyProvider, "tp19");

            ApproveSuspendAction action = ApproveSuspendAction.of("tp19",
                    signatures.stream().map(Signature::toString).collect(Collectors.toList()));

            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));

            System.out.println(JSON.toJSONString(action));
            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(action), false, keyProvider);
            System.out.println(txData.getTrxId());

        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
