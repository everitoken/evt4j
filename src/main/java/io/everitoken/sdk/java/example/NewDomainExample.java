package io.everitoken.sdk.java.example;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.NewDomainAction;
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

public class NewDomainExample {
    public static void main(String[] args) {
        final NetParams netParams = new TestNetNetParams();
        final String data = "{\"name\":\"test1127\","
                + "\"creator\":\"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"issue\":{\"name\":\"issue\","
                + "\"threshold\":1,\"authorizers\":[{\"ref\":\"[A] "
                + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\","
                + "\"weight\":1}]},\"transfer\":{\"name\":\"transfer\",\"threshold\":1,\"authorizers\":[{\"ref\":\"[G] "
                + ".OWNER\",\"weight\":1}]},\"manage\":{\"name\":\"manage\",\"threshold\":1,"
                + "\"authorizers\":[{\"ref\":\"[A]"
                + " EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"weight\":1}]}}";

        final JSONObject json = JSONObject.parseObject(data);
        final NewDomainAction newDomainAction = NewDomainAction.ofRaw(json.getString("name"), json.getString("creator"),
                json.getJSONObject("issue"), json.getJSONObject("transfer"), json.getJSONObject("manage"));

        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));
            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(newDomainAction), false,
                    keyProvider);
            System.out.println(txData.getTrxId());
        } catch (final ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
