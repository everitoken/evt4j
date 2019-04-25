package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Utils;

public class TransactionDigest {
    private final String id;
    private final byte[] digest;

    private TransactionDigest(String id, byte[] digest) {
        this.id = id;
        this.digest = digest;
    }

    public static TransactionDigest of(@NotNull JSONObject json) {
        return new TransactionDigest(json.getString("id"), Utils.HEX.decode(json.getString("digest")));
    }

    public String getId() {
        return this.id;
    }

    public byte[] getDigest() {
        return this.digest;
    }

}
