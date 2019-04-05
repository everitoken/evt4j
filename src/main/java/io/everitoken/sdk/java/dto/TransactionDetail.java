package io.everitoken.sdk.java.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.everitoken.sdk.java.Signature;

public class TransactionDetail {
    private final int blockNum;
    private final String packedTrx;
    private final String id;
    private final String compression;
    private final List<Signature> signatures = new ArrayList<>();
    private final JSONObject transaction;
    private final String blockId;

    private TransactionDetail(@NotNull JSONObject raw) throws JSONException {
        blockNum = raw.getInt("block_num");
        packedTrx = raw.getString("packed_trx");
        id = raw.getString("id");
        compression = raw.getString("compression");
        JSONArray signaturesArray = raw.getJSONArray("signatures");

        for (int i = 0; i < signaturesArray.length(); i++) {
            signatures.add(Signature.of((String) signaturesArray.get(i)));
        }

        transaction = raw.getJSONObject("transaction");
        blockId = raw.getString("block_id");
    }

    @NotNull
    @Contract("_ -> new")
    public static TransactionDetail create(JSONObject raw) throws JSONException {
        Objects.requireNonNull(raw);
        return new TransactionDetail(raw);
    }

    @JSONField(name = "block_num")
    public int getBlockNum() {
        return blockNum;
    }

    @JSONField(name = "packed_trx")
    public String getPackedTrx() {
        return packedTrx;
    }

    public String getId() {
        return id;
    }

    public String getCompression() {
        return compression;
    }

    public List<Signature> getSignatures() {
        return signatures;
    }

    public JSONObject getTransaction() {
        return transaction;
    }

    @JSONField(name = "block_id")
    public String getBlockId() {
        return blockId;
    }
}