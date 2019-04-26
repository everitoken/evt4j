package io.everitoken.sdk.java.abi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

class ExecuteSuspendActionTest {

    @Test
    void serializationTest() {

        ExecuteSuspendAction action = ExecuteSuspendAction.of("tp16",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND");
        String expected = "{\"domain\":\".suspend\","
                + "\"executor\":\"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\","
                + "\"key\":\"execsuspend\",\"name\":\"tp16\"}";

        Assertions.assertEquals(expected, JSON.toJSONString(action));

    }
}