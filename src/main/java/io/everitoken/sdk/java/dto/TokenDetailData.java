package io.everitoken.sdk.java.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.exceptions.InvalidPublicKeyException;

public class TokenDetailData implements Meta {
    private List<PublicKey> owner = new ArrayList<>();
    private final JSONArray metas;
    private String name;
    private String domain;

    private TokenDetailData(@NotNull JSONObject raw) throws JSONException {
        name = raw.getString("name");
        domain = raw.getString("domain");
        metas = raw.getJSONArray("metas");

        JSONArray owner = raw.getJSONArray("owner");

        for (int i = 0; i < owner.size(); i++) {
            try {
                PublicKey publicKey = PublicKey.of((String) owner.get(i));
                this.owner.add(publicKey);
            } catch (InvalidPublicKeyException ex) {
            }
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static TokenDetailData create(JSONObject raw) throws JSONException {
        Objects.requireNonNull(raw);
        return new TokenDetailData(raw);
    }

    public String getName() {
        return name;
    }

    public String getDomain() {
        return domain;
    }

    public List<PublicKey> getOwner() {
        return owner;
    }

    @Override
    public JSONArray getMetas() {
        return metas;
    }
}