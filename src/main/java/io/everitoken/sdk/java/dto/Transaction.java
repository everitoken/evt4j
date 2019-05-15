package io.everitoken.sdk.java.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import io.everitoken.sdk.java.abi.Action;
import io.everitoken.sdk.java.service.TransactionConfiguration;

public class Transaction {
    @JSONField(serialize = false, deserialize = false)
    private final TransactionConfiguration trxConfig;

    private final List<Action> actions;
    private final String expiration;
    private final int refBlockNumber;
    private final long refBlockPrefix;
    private final long maxCharge;
    private final String payer;
    private final List<String> transactionExtensions = new ArrayList<>();

    @JSONField(serialize = false, deserialize = false)
    private TransactionDigest transactionDigest;

    public Transaction(final List<String> actions, final String expiration, final int refBlockNumber,
            final long refBlockPrefix, final long maxCharge, final String payer,
            @Nullable TransactionConfiguration trxConfig) {

        this.actions = actions.stream().map(JSONObject::parseObject).map(Action::ofRaw).collect(Collectors.toList());
        this.expiration = expiration;
        this.refBlockNumber = refBlockNumber;
        this.refBlockPrefix = refBlockPrefix;
        this.maxCharge = maxCharge;
        this.payer = payer;
        this.trxConfig = trxConfig;
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

    public TransactionConfiguration getTrxConfig() {
        return trxConfig;
    }
}
