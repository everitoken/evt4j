package io.everitoken.sdk.java.abi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

class CancelSuspendActionTest {

    @Test
    void serializationTest() {
        CancelSuspendAction action = CancelSuspendAction.of("tp17");
        Assertions.assertTrue(JSON.toJSONString(action)
                .contains("\"domain\":\".suspend\",\"key\":\"cancelsuspend\",\"name\":\"tp17\""));
    }
}