package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.exceptions.InvalidPublicKeyException;

class GroupDetailDataTest {

    @Test
    @DisplayName("Throw JSON exception if can't parse json")
    void throwJSONException() {
        Assertions.assertThrows(JSONException.class, () -> {
            GroupDetailData.create(JSONObject.parseObject("["));
        });
    }

    @Test
    @DisplayName("Throw when public key is invalid")
    void throwPublicKeyException() {
        Assertions.assertThrows(InvalidPublicKeyException.class, () -> {
            JSONObject json = new JSONObject();
            json.put("name", "testName");
            json.put("key", "wrongkey");
            GroupDetailData.create(json);
        });
    }

    @Test
    @DisplayName("Don't throw when everything is fine")
    void notThrowWhenPayloadIsGood() {
        Assertions.assertDoesNotThrow(() -> {
            JSONObject json = new JSONObject();
            json.put("name", "testName");
            json.put("key", "EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa");
            json.put("metas", new JSONArray());
            json.put("root", new JSONObject());
            GroupDetailData.create(json);
        });
    }
}