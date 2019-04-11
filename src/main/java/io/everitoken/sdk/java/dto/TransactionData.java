package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TransactionData implements Transactable {
    private final String trxId;
    private final JSONObject processed;

    private TransactionData(String trxId, JSONObject processed) {
        this.trxId = trxId;
        this.processed = processed;
    }

    @NotNull
    @Contract("_ -> new")
    public static TransactionData ofRaw(@NotNull JSONObject raw) {
        return new TransactionData(raw.getString("transaction_id"), raw.getJSONObject("processed"));
    }

    @Override
    @JSONField(name = "transaction_id")
    public String getTrxId() {
        return trxId;
    }

    public JSONObject getProcessed() {
        return processed;
    }

    public boolean isExecuted() {
        if (processed == null) {
            return false;
        }

        try {
            JSONObject receipt = processed.getJSONObject("receipt");
            return receipt.getString("status").equals("executed");
        } catch (Exception ex) {
            // invalid structure -> false
            return false;
        }
    }
}
