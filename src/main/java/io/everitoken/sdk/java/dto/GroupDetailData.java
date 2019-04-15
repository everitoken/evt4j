package io.everitoken.sdk.java.dto;

import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.exceptions.InvalidPublicKeyException;

public class GroupDetailData implements Meta, Namable {
    private final String name;
    private final PublicKey key;
    private final JSONObject root;
    private final JSONArray metas;

    // TODO implement root tree structure, extract to a separate dto for node
    private GroupDetailData(@NotNull JSONObject raw) throws JSONException {
        name = raw.getString("name");
        key = PublicKey.of(raw.getString("key"));
        metas = raw.getJSONArray("metas");
        root = raw.getJSONObject("root");
    }

    @NotNull
    @Contract("_ -> new")
    public static GroupDetailData create(JSONObject raw) throws InvalidPublicKeyException {
        Objects.requireNonNull(raw);
        return new GroupDetailData(raw);
    }

    @Override
    public String getName() {
        return name;
    }

    public String getKey() {
        return key.toString();
    }

    public JSONObject getRoot() {
        return root;
    }

    @Override
    public JSONArray getMetas() {
        return metas;
    }

    @Override
    public String toString() {
        return name;
    }
}