package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TransactionDetailTest {

    @Test
    @DisplayName("Can create from json object")
    void create() {
        Assertions.assertDoesNotThrow(() -> {
            JSONObject obj = new JSONObject();
            obj.put("block_num", 1);
            obj.put("packed_trx", "test_packed_trx");
            obj.put("id", "test_id");
            obj.put("compression", "test_compression");
            obj.put("signatures", JSONArray.parseArray(
                    "[\"SIG_K1_Ke1xR6s7BfUFguPDNbGvH5SnWeKSZnXwepzWK1mWSyVaYkZ8zRDzZkmTNbaGUhwATt1VNV4kDatmvK96uahTsH3cQcKgqJ\", \"SIG_K1_Kg3UGU7UVDefMVZLnyDuzCEQarZf3vUFgwLzr3Hrovxdom4WWY5WQdinDNc2gVA98Rpf7Yg3ZGCmjNK13jVyFsnLTwWJMb\"]"));
            obj.put("transaction", new JSONObject());
            obj.put("block_id", "test_block_id");
            TransactionDetail.create(obj);
        });
    }

    @Test
    @DisplayName("Throw exception when input is not valid json")
    void throwException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            TransactionDetail.create(new JSONObject());
        });
    }
}