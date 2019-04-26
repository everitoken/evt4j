package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

public abstract class Abi implements AbiActionInterface {
    @JSONField(deserialize = false, serialize = false)
    private final String name;

    @JSONField(deserialize = false, serialize = false)
    private final String key;

    @JSONField(deserialize = false, serialize = false)
    private final String domain;

    Abi(String name, String key, String domain) {
        this.name = name;
        this.key = key;
        this.domain = domain;
    }

    public String serialize(AbiSerialisationProviderInterface provider) {
        JSONObject payload = new JSONObject();
        payload.put("name", getName());
        payload.put("key", getKey());
        payload.put("domain", getDomain());
        payload.put("data", getData(provider));
        return payload.toString();
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    @JSONField(name = "domain")
    public String getDomain() {
        return domain;
    }

    public String getData(AbiSerialisationProviderInterface provider) {
        return provider.serialize(JSON.toJSONString(new AbiToBin<>(getName(), this)));
    }
}
