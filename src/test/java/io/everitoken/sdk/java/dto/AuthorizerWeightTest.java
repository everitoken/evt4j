package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.PublicKey;

class AuthorizerWeightTest {
    @Test
    @DisplayName("JSON serialize")
    void toJSONString() {
        AuthorizerWeight authorizerWeight = AuthorizerWeight.createOwner(2);
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(authorizerWeight));
        Assertions.assertEquals("[G] .OWNER", json.getString("ref"));
        Assertions.assertEquals(2, (int) json.getInteger("weight"));

        PublicKey key = PublicKey.of("EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa");
        AuthorizerWeight authorizerWeight1 = AuthorizerWeight.createAccount(key, 1);
        JSONObject json1 = JSONObject.parseObject(JSON.toJSONString(authorizerWeight1));
        Assertions.assertEquals(json1.getString("ref"),
                String.format("%s %s", "[A]", "EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa"));
        Assertions.assertEquals(1, (int) json1.getInteger("weight"));
    }
}