package io.everitoken.sdk.java.param;

import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FungibleActionParamsTest {
    @Test
    @DisplayName("Serialization should be correct")
    void asJSONString() {
        FungibleActionParams params = new FungibleActionParams("address", "testSymbol");
        JSONObject json = JSONObject.parseObject(params.asBody());

        Assertions.assertEquals("address", json.getString("addr"));
        Assertions.assertEquals("testSymbol", json.getString("sym_id"));
        Assertions.assertEquals(0, (int) json.getInteger("skip"));
        Assertions.assertEquals(10, (int) json.getInteger("take"));
    }
}