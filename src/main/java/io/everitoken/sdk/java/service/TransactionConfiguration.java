package io.everitoken.sdk.java.service;

import org.jetbrains.annotations.Nullable;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.provider.KeyProviderInterface;
import io.everitoken.sdk.java.provider.SignProvider;
import io.everitoken.sdk.java.provider.SignProviderInterface;

public class TransactionConfiguration {
    private final long maxCharge;
    private final PublicKey payer;
    private final SignProviderInterface signProvider;
    private String expiration;

    public TransactionConfiguration(final long maxCharge, final PublicKey payer, SignProviderInterface signProvider,
            @Nullable String expiration) {
        this.maxCharge = maxCharge;
        this.payer = payer;
        this.signProvider = signProvider;
        this.expiration = expiration;
    }

    public TransactionConfiguration(final long maxCharge, final PublicKey payer, KeyProviderInterface keyProvider) {
        this(maxCharge, payer, SignProvider.of(keyProvider), null);
    }

    public TransactionConfiguration(final long maxCharge, final PublicKey payer, KeyProviderInterface keyProvider,
            String expiration) {
        this(maxCharge, payer, SignProvider.of(keyProvider), expiration);
    }

    public long getMaxCharge() {
        return maxCharge;
    }

    public String getPayer() {
        return payer.toString();
    }

    public SignProviderInterface getSignProvider() {
        return signProvider;
    }

    public String getExpiration() {
        return expiration;
    }
}
