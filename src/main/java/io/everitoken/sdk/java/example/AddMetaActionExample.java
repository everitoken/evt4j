package io.everitoken.sdk.java.example;

import java.util.Arrays;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.AddMetaAction;
import io.everitoken.sdk.java.dto.AuthorizerRef;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class AddMetaActionExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();
        TransactionService transactionService = TransactionService.of(netParam);
        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");

        try {
            AddMetaAction actionForDomainToken = AddMetaAction.ofDomainToken("t2", "test1122", "logo2", "feitesting1",
                    AuthorizerRef.createAccount(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

            AddMetaAction actionForGroup = AddMetaAction.ofDomainToken("t2", "test1122", "logo3", "feitesting1",
                    AuthorizerRef.createAccount(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

            AddMetaAction actionForFungible = AddMetaAction.ofFungible("20", "logo4", "feitesting1",
                    AuthorizerRef.createAccount(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

            TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
                    PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"), keyProvider);

            TransactionData txData = transactionService.push(trxConfig,
                    Arrays.asList(actionForDomainToken, actionForGroup, actionForFungible));
            System.out.println(txData.getTrxId());

        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
