package io.everitoken.sdk.java.dto;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.Utils;

public class TransactionDigest {
    private final String id;
    private final byte[] digest;

    private TransactionDigest(@Nullable String id, byte[] digest) {
        this.id = id;
        this.digest = digest;
    }

    public static TransactionDigest of(@NotNull JSONObject json) {
        String id = null;

        if (json.containsKey("id")) {
            id = json.getString("id");
        }

        return new TransactionDigest(id, Utils.HEX.decode(json.getString("digest")));
    }

    @Nullable
    public String getId() {
        return this.id;
    }

    public byte[] getDigest() {
        return this.digest;
    }

}
