package io.everitoken.sdk.java.apiResource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import io.everitoken.sdk.java.Utils;
import io.everitoken.sdk.java.dto.TransactionDigest;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

class SignableDigestTest {
    @Test
    @EnabledIfEnvironmentVariable(named = "SINGLE_RUN_TEST", matches = "SignableDigestTest")
    void test() {
        Assertions.assertDoesNotThrow(() -> {
            String refDigest = "b046185545b5c85a87dbf416d103403d913d74cdc1b6615646fc02fd6c0ee8ec";

            String data = "{\"actions\":[{\"name\":\"newdomain\"," + "\"data"
                    + "\":\"4710f541def7681843650c062d0000000002c8f031561c4758c9551cff47246f2c347189fe684c04da35cf88e813f810e3c2000000008052e74c0100000001010002c8f031561c4758c9551cff47246f2c347189fe684c04da35cf88e813f810e3c20100000000b298e982a40100000001000001000000000094135c680100000001010002c8f031561c4758c9551cff47246f2c347189fe684c04da35cf88e813f810e3c20100\",\"domain\":\"feitestdomainame9\",\"key\":\".create\"}],\"expiration\":\"2019-01-28T22:15:00\",\"ref_block_num\":36049,\"ref_block_prefix\":1756121974,\"max_charge\":1000000,\"payer\":\"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\"}";
            NetParams netParams = new TestNetNetParams();
            SignableDigest signableDigest = new SignableDigest();
            TransactionDigest digest = signableDigest.request(RequestParams.of(netParams, () -> data));
            Assertions.assertEquals(refDigest, Utils.HEX.encode(digest.getDigest()));
        });
    }
}