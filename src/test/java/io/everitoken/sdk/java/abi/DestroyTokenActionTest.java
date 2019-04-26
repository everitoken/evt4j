package io.everitoken.sdk.java.abi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

class DestroyTokenActionTest {
    @Test
    void test() {
        String expected = "{\"action\":\"destroytoken\",\"args\":{\"domain\":\"test1119\",\"name\":\"t3\"}}";

        DestroyTokenAction destroyTokenAction = DestroyTokenAction.of("test1119", "t3");

        Assertions.assertEquals(expected, JSON.toJSONString(new AbiToBin<>("destroytoken", destroyTokenAction)));
    }
}