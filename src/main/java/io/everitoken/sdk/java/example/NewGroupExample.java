package io.everitoken.sdk.java.example;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.abi.NewGroupAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class NewGroupExample {
    public static void main(String[] args) {
        final NetParams netParam = new TestNetNetParams();
        final String data = "{\"name\":\"feitestgroup6\","
                + "\"key\":\"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\"root\":{\"threshold\":6,"
                + "\"weight\":0,\"nodes\":[{\"threshold\":1,\"weight\":3,"
                + "\"nodes\":[{\"key\":\"EVT6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV\",\"weight\":1},"
                + "{\"key\":\"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\"weight\":1}]},"
                + "{\"key\":\"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\"weight\":3},{\"threshold\":1,"
                + "\"weight\":3,\"nodes\":[{\"key\":\"EVT6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV\","
                + "\"weight\":1},{\"key\":\"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\"weight\":1}]}]}}";

        final JSONObject json = JSONObject.parseObject(data);
        final NewGroupAction newGroupAction = NewGroupAction.ofRaw("feitestgroup6", json);

        // try {
        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        // TransactionData txData = transactionService.push(trxConfig,
        // Arrays.asList(newGroupAction));
        // System.out.println(txData.getTrxId());
        // } catch (final ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
