package io.everitoken.sdk.java.dto;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;

class TransactionTest {

    @Test
    @DisplayName("Deserialize correctly")
    void serialize() {
        String digest = "{\"digest\":\"b046185545b5c85a87dbf416d103403d913d74cdc1b6615646fc02fd6c0ee8ec\","
                + "\"id\":\"8ed7e5ffc3313ed01d44735183dfa9766237a7d8e0576f05576002c300129f65\"}";
        String actions = "{\"data"
                + "\":\"010002c8f031561c4758c9551cff47246f2c347189fe684c04da35cf88e813f810e3c2010003e5a96ca55eac7852ce028fca0dfdb2684123fd72298cbafe75cea4f01c3a9d32a08601000000000014000000050000000974657374206a617661\",\"domain\":\".fungible\",\"key\":\"20\",\"name\":\"transferft\"}";
        TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
                PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
                KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"), null);
        Transaction transaction = new Transaction(Arrays.asList(actions), "expiration", 1, 1L, 1, "payer", trxConfig);
        transaction.setTransactionDigest(TransactionDigest.of(JSON.parseObject(digest)));

        String output = JSON.toJSONString(transaction);
        Assertions.assertFalse(output.contains("trxId"));
        Assertions.assertFalse(output.contains("digest"));
        Assertions.assertFalse(output.contains("trxConfig"));
    }

}