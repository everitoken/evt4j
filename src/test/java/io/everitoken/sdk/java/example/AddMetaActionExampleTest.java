package io.everitoken.sdk.java.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.AddMetaAction;
import io.everitoken.sdk.java.dto.AuthorizerRef;

class AddMetaActionExampleTest {

    @Test
    void serializeTest() {
        String expected = "{\"creator\":\"[A] EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\","
                + "\"domain\":\"test1125\",\"key\":\"logo2\",\"value\":\"feitesting1\"}";

        AddMetaAction action = AddMetaAction.ofDomainToken("t20", "test1125", "logo2", "feitesting1",
                AuthorizerRef.createAccount(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")));

        Assertions.assertEquals(expected, JSON.toJSONString(action));
    }
}