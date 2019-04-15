package io.everitoken.sdk.java.dto;

import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

public class ActionData implements Transactable {
    private final String trxId;
    private final JSONObject data;
    private final String domain;
    private final String name;
    private final String key;
    private final DateTime timestamp;

    private ActionData(@NotNull JSONObject raw) {
        trxId = raw.getString("trx_id");
        data = raw.getJSONObject("data");
        domain = raw.getString("domain");
        key = raw.getString("key");
        name = raw.getString("name");
        timestamp = new DateTime(raw.getString("timestamp"));
    }

    @NotNull
    @Contract("_ -> new")
    public static ActionData create(JSONObject raw) {
        Objects.requireNonNull(raw);
        return new ActionData(raw);
    }

    @Override
    @JSONField(name = "trx_id")
    public String getTrxId() {
        return trxId;
    }

    public JSONObject getData() {
        return data;
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }
}
