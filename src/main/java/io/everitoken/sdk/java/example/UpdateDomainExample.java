package io.everitoken.sdk.java.example;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.UpdateDomainAction;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class UpdateDomainExample {
    public static void main(String[] args) {
        final NetParams netParam = new TestNetNetParams();
        final String data = "{\"manage\":{\"name\":\"manage\",\"threshold\":2,\"authorizers\":[{\"ref\":\"[A] "
                + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"weight\":1},{\"ref\":\"[A] "
                + "EVT5cd4a3RyaVoubc4w3j3Z3YvCJgtKZPRdJHDdk7wVsMbc3yEH5U\",\"weight\":1}]},\"name\":\"test1123\"}";

        final JSONObject json = JSONObject.parseObject(data);
        final UpdateDomainAction updateDomainAction = UpdateDomainAction.ofRaw(json.getString("name"), null, null,
                json.getJSONObject("manage"));

        try {
            TransactionService transactionService = TransactionService.of(netParam);
            TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
                    PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
                    KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(updateDomainAction));
            System.out.println(txData.getTrxId());
        } catch (final ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
