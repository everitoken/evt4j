package io.everitoken.sdk.java.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import io.everitoken.sdk.java.abi.Action;

public class Transaction {
    private final List<Action> actions;
    private final String expiration;
    private final int refBlockNumber;
    private final long refBlockPrefix;
    private final int maxCharge;
    private final String payer;
    private final List<String> transactionExtensions = new ArrayList<>();

    public Transaction(final List<String> actions, final String expiration, final int refBlockNumber,
            final long refBlockPrefix, final int maxCharge, final String payer) {

        this.actions = actions.stream().map(JSONObject::parseObject).map(Action::ofRaw).collect(Collectors.toList());
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
    public int getRefBlockNumber() {
        return refBlockNumber;
    }

    @JSONField(name = "ref_block_prefix")
    public long getRefBlockPrefix() {
        return refBlockPrefix;
    }

    @JSONField(name = "max_charge")
    public int getMaxCharge() {
        return maxCharge;
    }

    public String getPayer() {
        return payer;
    }

    @JSONField(name = "transaction_extensions")
    public List<String> getTransactionExtensions() {
        return transactionExtensions;
    }
}
