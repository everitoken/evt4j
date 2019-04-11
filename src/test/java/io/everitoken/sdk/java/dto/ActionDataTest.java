package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ActionDataTest {

    @Test
    @DisplayName("Should throw correct exception")
    void throwCorrectException() {
        Assertions.assertThrows(NullPointerException.class, () -> ActionData.create(JSONObject.parseObject("")));
    }

}