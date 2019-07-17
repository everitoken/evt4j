package io.everitoken.sdk.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AssetTest {
    @Test
    void parseFromString() {
        Asset asset = Asset.parseFromRawBalance("1000.00000 S#1");
        assertEquals("1000.00000 S#1", asset.toString());
        assertEquals(5, asset.getSymbol().getPrecision());

        Asset asset1 = Asset.parseFromRawBalance("1 S#4");
        assertEquals(0, asset1.getSymbol().getPrecision());
    }

    @Test
    void testExceptions() {
        Assertions.assertDoesNotThrow(() -> {
            Asset asset1 = Asset.parseFromRawBalance("1 S#4");
            assertEquals(0, asset1.getSymbol().getPrecision());
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Asset.parseFromRawBalance("1.0.0 S#4");
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Asset.parseFromRawBalance("1,0 S#4");
        });
    }

    @Test
    void balanceBiggerThanInt32() {

        Asset asset = Asset.parseFromRawBalance("10000000000.00000 S#1");
        assertEquals("10000000000.00000 S#1", asset.toString());

        Asset asset1 = Asset.parseFromRawBalance("10000000000.1234 S#1");
        assertEquals("10000000000.1234 S#1", asset1.toString());
        assertEquals(4, asset1.getSymbol().getPrecision());

        Asset asset2 = Asset.parseFromRawBalance("1000000000000.123456789 S#1");
        assertEquals("1000000000000.123456789 S#1", asset2.toString());
        assertEquals(9, asset2.getSymbol().getPrecision());

        Asset asset3 = Asset.parseFromRawBalance("1 S#1");
        assertEquals("1 S#1", asset3.toString());
    }

    @Test
    void getBalance() {
        Asset asset = Asset.parseFromRawBalance("10000000000.00000 S#1");
        assertEquals("10000000000.00000", asset.getBalance());

        Asset asset1 = Asset.parseFromRawBalance("1000000000000.123456789 S#1");
        assertEquals("1000000000000.123456789", asset1.getBalance());

        Asset asset2 = Asset.parseFromRawBalance("1123 S#1");
        assertEquals("1123", asset2.getBalance());
    }
}