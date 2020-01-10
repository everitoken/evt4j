package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import org.bouncycastle.util.Strings;

import io.everitoken.sdk.java.exceptions.InvalidFungibleBalanceException;

public class FungibleBalanceData {
    private final double balance;
    private final int id;
    @JSONField(serialize = false, deserialize = false)
    private final String raw;

    public FungibleBalanceData(int id, double balance) {
        this.id = id;
        this.balance = balance;
        raw = format(id, balance);
    }

    public FungibleBalanceData(String rawBalance) {
        String[] parts = Strings.split(rawBalance, ' ');
        try {
            balance = Double.parseDouble(parts[0]);
        } catch (NumberFormatException ex) {
            throw new InvalidFungibleBalanceException(String.format("Failed to parse balance %s", parts[0]), ex);
        }

        try {
            String[] symbol = Strings.split(parts[1], '#');
            id = Integer.parseInt(symbol[1], 10);
        } catch (Exception ex) {
            throw new InvalidFungibleBalanceException(String.format("Failed to parse symbol id %s", parts[1]), ex);
        }

        raw = rawBalance;
    }

    private static String format(int id, double balance) {
        return String.format("%.5f S#%d", balance, id);
    }

    public static FungibleBalanceData ofRaw(String raw) {
        return new FungibleBalanceData(raw);
    }

    public static FungibleBalanceData of(int id, double balance) {
        return new FungibleBalanceData(id, balance);
    }

    public String getRaw() {
        return raw;
    }

    @JSONField(deserialize = false)
    public int getId() {
        return id;
    }

    @JSONField
    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
