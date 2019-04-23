package io.everitoken.sdk.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AssetTest {
    @Test
    void parseFromString() {
        Asset asset = Asset.parseFromRawBalance("1000.00000 S#1");
        assertEquals("1000.00000 S#1", asset.toString());
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
    }
}