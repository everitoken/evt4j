package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PermissionTest {
    private static final String raw = "{\"name\":\"issue\",\"threshold\":1,\"authorizers\":[{\"ref\":\"[G] "
            + ".everiToken\"," + "\"weight\":1}]}";

    @Test
    @DisplayName("Deserialize successful")
    void deserialize() {
        Assertions.assertDoesNotThrow(() -> {
            Permission permission = Permission.ofRaw(JSONObject.parseObject(raw));
            Assertions.assertEquals(raw, JSON.toJSONString(permission));
            Assertions.assertEquals("issue", permission.getName());
            Assertions.assertEquals(1, permission.getThreshold());
            Assertions.assertEquals(1, permission.getAuthorizers().get(0).getWeight());
        });
    }
}