package io.everitoken.sdk.java.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        for (int i = 0; i < owner.length(); i++) {
            try {
                this.owner.add(PublicKey.of((String) owner.get(i)));
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

    @Override
    public String toString() {
        return String.format("%s, %s -> %s, %s -> %s", super.toString(), "NewDomainAction", getName(), "owner",
                owner.stream().map(PublicKey::toString).reduce(String::concat));
    }
}