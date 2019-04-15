package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import io.everitoken.sdk.java.dto.Charge;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

class TransactionEstimatedChargeTest {
    @Test
    @EnabledIfEnvironmentVariable(named = "ENV", matches = "TransactionEstimatedCharge")
    void test() {
        Assertions.assertDoesNotThrow(() -> {
            String transaction = "{\"actions\":[{\"name\":\"newdomain\"," + "\"data"
                    + "\":\"4710f541def7681843650c062d0000000002c8f031561c4758c9551cff47246f2c347189fe684c04da35cf88e813f810e3c2000000008052e74c0100000001010002c8f031561c4758c9551cff47246f2c347189fe684c04da35cf88e813f810e3c20100000000b298e982a40100000001000001000000000094135c680100000001010002c8f031561c4758c9551cff47246f2c347189fe684c04da35cf88e813f810e3c20100\",\"domain\":\"feitestdomainame9\",\"key\":\".create\"}],\"expiration\":\"2019-01-30T22:12:20\",\"ref_block_num\":53647,\"ref_block_prefix\":1474724499,\"max_charge\":1000000,\"payer\":\"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\"}";

            NetParams netParams = new TestNetNetParams();
            TransactionEstimatedCharge transactionEstimatedCharge = new TransactionEstimatedCharge();

            Charge res = transactionEstimatedCharge.request(RequestParams.of(netParams, () -> {
                JSONObject json = new JSONObject();
                json.put("transaction", JSONObject.parseObject(transaction));
                json.put("sign_num", 1);
                return json.toString();
            }));

            Assertions.assertTrue(res.getCharge() > 0);
        });
    }
}