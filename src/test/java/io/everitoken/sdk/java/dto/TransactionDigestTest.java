package io.everitoken.sdk.java.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.Utils;

class TransactionDigestTest {
    @Test
    void createObject() {
        String raw = "{\"digest\":\"b046185545b5c85a87dbf416d103403d913d74cdc1b6615646fc02fd6c0ee8ec\",\"id\":\"8ed7e5ffc3313ed01d44735183dfa9766237a7d8e0576f05576002c300129f65\"}";
        TransactionDigest digest = TransactionDigest.of(JSONObject.parseObject(raw));
        assertEquals("b046185545b5c85a87dbf416d103403d913d74cdc1b6615646fc02fd6c0ee8ec",
                Utils.HEX.encode(digest.getDigest()));
        assertEquals("8ed7e5ffc3313ed01d44735183dfa9766237a7d8e0576f05576002c300129f65", digest.getId());
    }

}