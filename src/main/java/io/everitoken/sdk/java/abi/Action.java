package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSONObject;

public class Action {
    private final String name;
    private final String key;
    private final String domain;
    private final String data;

    private Action(String name, String key, String domain, String data) {
        this.name = name;
        this.key = key;
        this.domain = domain;
        this.data = data;
    }

    public static Action ofRaw(JSONObject raw) {
        return new Action(raw.getString("name"), raw.getString("key"), raw.getString("domain"), raw.getString("data"));
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getDomain() {
        return domain;
    }

    public String getData() {
        return data;
    }
}
