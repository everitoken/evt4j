package io.everitoken.sdk.java.abi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.Signature;

class ApproveSuspendActionTest {

    @Test
    void serializationTest() {
        List<Signature> signatures = new ArrayList<>();

        ApproveSuspendAction action = ApproveSuspendAction.of("tp16",
                signatures.stream().map(Signature::toString).collect(Collectors.toList()));

        Assertions.assertTrue(JSON.toJSONString(action)
                .contains("\"domain\":\".suspend\",\"key\":\"aprvsuspend\",\"name\":\"tp16\","));
    }
}