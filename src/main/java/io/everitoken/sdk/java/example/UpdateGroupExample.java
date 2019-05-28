package io.everitoken.sdk.java.example;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.abi.UpdateGroupAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class UpdateGroupExample {
    public static void main(String[] args) {
        final NetParams netParam = new TestNetNetParams();
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

        // try {
        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        // TransactionData txData = transactionService.push(trxConfig,
        // Arrays.asList(updateGroupAction));
        // System.out.println(txData.getTrxId());
        // } catch (final ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
