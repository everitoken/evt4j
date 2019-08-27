package io.everitoken.sdk.java.dto;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import io.everitoken.sdk.java.abi.Action;

public class Transaction {
    private final List<Action> actions;
    private final String expiration;
    private final long refBlockNumber;
    private final long refBlockPrefix;
    private final long maxCharge;
    private final String payer;
    private final List<String> transactionExtensions = new ArrayList<>();

    @JSONField(serialize = false, deserialize = false)
    private TransactionDigest transactionDigest;

    public Transaction(final List<String> actions, final String expiration, final long refBlockNumber,
            final long refBlockPrefix, final long maxCharge, final String payer) {

        List<Action> actionList = new ArrayList<>();

        for (int i = 0; i < actions.size(); i++) {
            actionList.add(Action.ofRaw(JSONObject.parseObject(actions.get(i))));
        }

        this.actions = actionList;
        this.expiration = expiration;
        this.refBlockNumber = refBlockNumber;
        this.refBlockPrefix = refBlockPrefix;
        this.maxCharge = maxCharge;
        this.payer = payer;
    }

    @JSONField(name = "actions")
    public List<Action> getActions() {
        return actions;
    }

    public String getExpiration() {
        return expiration;
    }

    @JSONField(name = "ref_block_num")
    public long getRefBlockNumber() {
        return refBlockNumber;
    }

    @JSONField(name = "ref_block_prefix")
    public long getRefBlockPrefix() {
        return refBlockPrefix;
    }

    @JSONField(name = "max_charge")
    public long getMaxCharge() {
        return maxCharge;
    }

    public String getPayer() {
        return payer;
    }

    public TransactionDigest getTransactionDigest() {
        return transactionDigest;
    }

    public void setTransactionDigest(TransactionDigest transactionDigest) {
        this.transactionDigest = transactionDigest;
    }

    @JSONField(name = "transaction_extensions")
    public List<String> getTransactionExtensions() {
        return transactionExtensions;
    }
}
