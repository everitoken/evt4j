package io.everitoken.sdk.java.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.Utils;
import io.everitoken.sdk.java.dto.NodeInfo;

public class TransactionConfiguration {
    private final long maxCharge;
    private final long blockNum;
    private final long blockPrefix;
    private final PublicKey payer;
    private String expiration;

    private TransactionConfiguration(final long blockNum, final long blockPrefix, final String blockTime,
            final long maxCharge, final PublicKey payer, final boolean hasEveriPay, @Nullable String expiration) {
        this.blockNum = blockNum;
        this.blockPrefix = blockPrefix;
        this.maxCharge = maxCharge;
        this.payer = payer;

        if (expiration == null) {
            this.expiration = getExpirationTime(blockTime, hasEveriPay ? "everipay" : null);
        } else {
            this.expiration = expiration;
        }
    }

    public static TransactionConfiguration of(NodeInfo info, final long maxCharge, final PublicKey payer) {
        return TransactionConfiguration.of(info, maxCharge, payer, false, null);
    }

    public static TransactionConfiguration of(final long blockNum, final long blockPrefix, final String blockTime,
            final long maxCharge, final PublicKey payer, final boolean hasEveriPay, @Nullable String expiration) {
        return new TransactionConfiguration(blockNum, blockPrefix, blockTime, maxCharge, payer, hasEveriPay,
                expiration);
    }

    public static TransactionConfiguration of(NodeInfo info, final long maxCharge, final PublicKey payer,
            final boolean hasEveriPay, @Nullable String expiration) {

        int blockNum = Utils.getNumHash(info.getLastIrreversibleBlockId());
        long blockPrefix = Utils.getLastIrreversibleBlockPrefix(info.getLastIrreversibleBlockId());
        String blockTime = info.getHeadBlockTime();

        return new TransactionConfiguration(blockNum, blockPrefix, blockTime, maxCharge, payer, hasEveriPay,
                expiration);
    }

    public static String getExpirationTime(@NotNull String referenceTime, @Nullable String type) {
        int TIMESTAMP_LENGTH = 19;
        Duration expireDuration = Duration.standardSeconds(100);

        if (type != null && type.equals("everipay")) {
            expireDuration = Duration.standardSeconds(10);
        }

        DateTime dateTime = Utils.getCorrectedTime(referenceTime);
        DateTime expiration = dateTime.plus(expireDuration);

        return expiration.toString().substring(0, TIMESTAMP_LENGTH);
    }

    public long getBlockNum() {
        return blockNum;
    }

    public long getBlockPrefix() {
        return blockPrefix;
    }

    public long getMaxCharge() {
        return maxCharge;
    }

    public String getPayer() {
        return payer.toString();
    }

    public String getExpiration() {
        return expiration;
    }
}
