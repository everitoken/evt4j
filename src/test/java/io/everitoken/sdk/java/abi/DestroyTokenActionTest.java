package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSON;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DestroyTokenActionTest {
    @Test
    void test() {
        String shape = "{\"action\":\"destroytoken\",\"args\":{\"domain\":\"test1119\",\"key\":\"t3\",\"name\":\"t3\"}}";

        DestroyTokenAction destroyTokenAction = DestroyTokenAction.of("test1119", "t3");

        Assertions.assertEquals(shape, JSON.toJSONString(new AbiToBin<>("destroytoken", destroyTokenAction)));
    }
}