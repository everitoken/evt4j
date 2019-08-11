package io.everitoken.sdk.java.example;

import java.util.Arrays;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.abi.UpdateGroupAction;
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

public class UpdateGroupExample {
    public static void main(String[] args) {
        final NetParams netParams = new TestNetNetParams();
        final String data = "{\n" + "  \"name\": \"feitestgroup2\",\n"
                + "  \"key\": \"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\",\n" + "  \"root\": {\n"
                + "    \"threshold\": 4,\n" + "    \"weight\": 0,\n" + "    \"nodes\": [\n" + "      {\n"
                + "        \"threshold\": 1,\n" + "        \"weight\": 3,\n" + "        \"nodes\": [\n"
                + "          {\n" + "            \"key\": \"EVT6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV\",\n"
                + "            \"weight\": 1\n" + "          },\n" + "          {\n"
                + "            \"key\": \"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\n"
                + "            \"weight\": 1\n" + "          }\n" + "        ]\n" + "      },\n" + "      {\n"
                + "        \"key\": \"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\n"
                + "        \"weight\": 3\n" + "      }\n" + "    ]\n" + "  }\n" + "}";

        final JSONObject json = JSONObject.parseObject(data);
        final UpdateGroupAction updateGroupAction = UpdateGroupAction.ofRaw("feitestgroup2", json);

        KeyProvider keyProvider = KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
        try {
            NodeInfo nodeInfo = (new Info()).request(RequestParams.of(netParams));
            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = TransactionConfiguration.of(nodeInfo, 1000000,
                    Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));
            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(updateGroupAction), false,
                    keyProvider);

            System.out.println(txData.getTrxId());
        } catch (final ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
