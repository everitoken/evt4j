package io.everitoken.sdk.java.dto;

import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import io.everitoken.sdk.java.abi.NewDomainAction;

public class DomainDetailData extends NewDomainAction implements Meta, Addressable {
    private final JSONArray metas;
    private final String address;
    private final DateTime createdTime;

    private DomainDetailData(@NotNull JSONObject raw) {
        super(raw.getString("name"), raw.getString("creator"), raw.getJSONObject("issue"),
                raw.getJSONObject("transfer"), raw.getJSONObject("manage"));
        address = raw.getString("address");
        metas = raw.getJSONArray("metas");
        createdTime = new DateTime(raw.getString("create_time"));
    }

    @NotNull
    @Contract("_ -> new")
    public static DomainDetailData of(JSONObject raw) throws JSONException {
        Objects.requireNonNull(raw);
        return new DomainDetailData(raw);
    }

    @Override
    public JSONArray getMetas() {
        return metas;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @JSONField(name = "create_time")
    public DateTime getCreatedTime() {
        return createdTime;
    }
}