package io.everitoken.sdk.java;

import java.math.BigDecimal;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongycastle.util.Strings;

import io.everitoken.sdk.java.exceptions.InvalidFungibleBalanceException;

public class Asset {
    private final BigDecimal balance;
    private final Symbol symbol;

    private Asset(Symbol symbol, BigDecimal balance) {
        this.symbol = symbol;
        this.balance = balance;
    }

    @NotNull
    @Contract("_, _ -> new")
    private static Asset of(Symbol symbol, BigDecimal balance) {
        return new Asset(symbol, balance);
    }

    @NotNull
    @Contract("_ -> new")
    public static Asset parseFromRawBalance(String balance) {
        return parseFromRawBalance(balance, null);
    }

    @NotNull
    public static Asset parseFromRawBalance(String balance, @Nullable Symbol symbol) {
        String[] parts = Strings.split(balance, ' ');
        int id;
        int precision;

        if (parts.length != 2) {
            throw new IllegalArgumentException(
                    String.format("Failed to parse balance: \"1.00000 S#1\" is expected, %s is passed in.", balance));
        }

        String[] balanceParts = Strings.split(parts[0], '.');

        if (balanceParts.length != 2) {
            throw new IllegalArgumentException(String.format(
                    "Failed to parse precision in balance. A \".\" is " + "expected, \"%s\" is passed in", balance));
        }

        precision = balanceParts[1].length();

        try {
            String[] symbolArray = Strings.split(parts[1], '#');
            id = Integer.parseInt(symbolArray[1], 10);
        } catch (Exception ex) {
            throw new InvalidFungibleBalanceException(String.format("Failed to parse symbol id %s", parts[1]), ex);
        }

        Symbol localSymbol = symbol;

        if (symbol == null) {
            localSymbol = Symbol.of(id, precision);
        }

        BigDecimal balanceInFloat = new BigDecimal(parts[0]);

        return Asset.of(localSymbol, balanceInFloat);
    }

    public Symbol getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return String.format("%." + symbol.getPrecision() + "f S#%d", balance, symbol.getId());
    }
}
