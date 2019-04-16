package io.everitoken.sdk.java.abi;

import java.util.Arrays;

import com.alibaba.fastjson.JSON;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TransferActionTest {
    @Test
    void testAbiShape() {
        String shape = "{\"action\":\"transfer\",\"args\":{\"domain\":\"test1119\",\"key\":\"t3\",\"memo\":\"\",\"name\":\"t3\","
                + "\"to\":[\"EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H\"]}}";

        TransferAction issueTokenAction = TransferAction.of("test1119", "t3",
                Arrays.asList("EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H"), "");
        Assertions.assertEquals(shape, JSON.toJSONString(new AbiToBin<>("transfer", issueTokenAction)));
    }
}