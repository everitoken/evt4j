package io.everitoken.sdk.java.dto;

import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.PublicKey;

public class FungibleDetailData implements Meta {
    private final PublicKey creator;
    private final Permission issue;
    private final Address address;
    private final JSONArray metas;
    private final String name;
    private final String symName;
    private final DateTime createdTime;
    private final Asset currentSupply;
    private final Asset totalSupply;
    private final String sym;
    private final Permission manage;

    public FungibleDetailData(PublicKey creator, Permission issue, Address address, JSONArray metas, String name,
            DateTime createdTime, Asset currentSupply, Asset totalSupply, String sym, Permission manage,
            String symName) {
        this.creator = creator;
        this.issue = issue;
        this.address = address;
        this.metas = metas;
        this.name = name;
        this.createdTime = createdTime;
        this.currentSupply = currentSupply;
        this.totalSupply = totalSupply;
        this.sym = sym;
        this.manage = manage;
        this.symName = symName;
    }

    @NotNull
    @Contract("_ -> new")
    public static FungibleDetailData ofRaw(JSONObject raw) {
        Objects.requireNonNull(raw);
        return new FungibleDetailData(PublicKey.of(raw.getString("creator")),
                Permission.ofRaw(raw.getJSONObject("issue")), Address.of(raw.getString("address")),
                raw.getJSONArray("metas"), raw.getString("name"), new DateTime(raw.getString("create_time")),
                Asset.parseFromRawBalance(raw.getString("current_supply")),
                Asset.parseFromRawBalance(raw.getString("total_supply")), raw.getString("sym"),
                Permission.ofRaw(raw.getJSONObject("manage")), raw.getString("sym_name"));
    }

    @JSONField(ordinal = 1)
    public String getCreator() {
        return creator.toString();
    }

    @JSONField(ordinal = 6)
    public Permission getIssue() {
        return issue;
    }

    @JSONField(ordinal = 3)
    public String getAddress() {
        return address.toString();
    }

    @JSONField(name = "sym_name", ordinal = 4)
    public String getSymName() {
        return symName;
    }

    @JSONField(name = "name", ordinal = 9)
    public String getName() {
        return name;
    }

    @JSONField(name = "create_time", ordinal = 5)
    public DateTime getCreatedTime() {
        return createdTime;
    }

    @JSONField(name = "current_supply", ordinal = 2)
    public String getCurrentSupply() {
        return currentSupply.toString();
    }

    @JSONField(name = "total_supply", ordinal = 7)
    public String getTotalSupply() {
        return totalSupply.toString();
    }

    @JSONField(ordinal = 8)
    public String getSym() {
        return sym;
    }

    @JSONField(ordinal = 11)
    public Permission getManage() {
        return manage;
    }

    @Override
    public JSONArray getMetas() {
        return metas;
    }
}
