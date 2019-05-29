package io.everitoken.sdk.java.example;

import java.util.Arrays;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.AddMetaAction;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.dto.AuthorizerRef;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class AddMetaActionExample {
    public static void main(String[] args) {
        NetParams netParams = new TestNetNetParams();
        TransactionService transactionService = TransactionService.of(netParams);
        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");

        try {

            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            AddMetaAction actionForDomainToken = AddMetaAction.ofDomainToken("t20", "test1125", "logo2", "feitesting1",
                    AuthorizerRef.createAccount(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));
            System.out.println(JSON.toJSONString(actionForDomainToken));

            AddMetaAction actionForGroup = AddMetaAction.ofDomainToken("t2", "test1122", "logo3", "feitesting1",
                    AuthorizerRef.createAccount(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

            AddMetaAction actionForFungible = AddMetaAction.ofFungible("20", "logo4", "feitesting1",
                    AuthorizerRef.createAccount(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));

            TransactionData txData = transactionService.push(trxConfig,
                    Arrays.asList(actionForDomainToken, actionForGroup, actionForFungible), false, keyProvider);
            System.out.println(txData.getTrxId());

        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
