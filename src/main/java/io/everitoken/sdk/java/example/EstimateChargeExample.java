package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.Collections;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.IssueTokenAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.Charge;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class EstimateChargeExample {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();
        IssueTokenAction issueTokenAction = IssueTokenAction.of("test1123", Arrays.asList("t3"),
                Collections.singletonList(Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));

            Charge charge = transactionService.estimateCharge(trxConfig, Arrays.asList(issueTokenAction),
                    Arrays.asList(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

            System.out.println(JSON.toJSONString(charge));

        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
