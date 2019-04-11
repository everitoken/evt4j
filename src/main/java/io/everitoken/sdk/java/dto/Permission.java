package io.everitoken.sdk.java.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.NotNull;

public class Permission implements Namable {
    private final String name;
    private final int threshold;
    private List<AuthorizerWeight> authorizers = new ArrayList<>();

    private Permission(String name, int threshold, List<AuthorizerWeight> authorizers) {
        this.name = name;
        this.threshold = threshold;
        this.authorizers = authorizers;
    }

    @NotNull
    public static Permission ofRaw(@NotNull JSONObject raw) {
        Objects.requireNonNull(raw);
        String name = raw.getString("name");
        int threshold = raw.getInteger("threshold");
        List<AuthorizerWeight> authorizers = new ArrayList<>();
        JSONArray authorizersArray = raw.getJSONArray("authorizers");

        for (int i = 0; i < authorizersArray.size(); i++) {
            authorizers.add(AuthorizerWeight.ofRaw((JSONObject) authorizersArray.get(i)));
        }

        return new Permission(name, threshold, authorizers);
    }

    @JSONField(ordinal = 1)
    public int getThreshold() {
        return threshold;
    }

    @JSONField(ordinal = 2)
    public List<AuthorizerWeight> getAuthorizers() {
        return authorizers;
    }

    @Override
    public String getName() {
        return name;
    }
}
