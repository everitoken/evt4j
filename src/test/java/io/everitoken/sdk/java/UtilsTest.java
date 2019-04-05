package io.everitoken.sdk.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.exceptions.Base58CheckException;

class UtilsTest {

    @Test
    @DisplayName("Base58 Invalid Key")
    void base58CheckDecodeWithInvalidKey() {
        Assertions.assertThrows(Base58CheckException.class, () -> {
            String key = "76uLwUD5t6fkob9Rbc11UxHgdTVshNceyv2hmppw4d82j2zYRpa";
            Utils.base58CheckDecode(key);
        });
    }

    @Test
    @DisplayName("Base58 Valid Key, Checksum successful")
    void base58CheckDecodeWithValidKey() {
        Assertions.assertDoesNotThrow(() -> {
            Utils.base58CheckDecode("76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa");
        });
    }

    @Test
    @DisplayName("Base58 decode signature")
    void base58OnSignature() {
        Assertions.assertDoesNotThrow(() -> {
            Utils.base58CheckDecode(
                    "KfdgiuhCZFSx9ggL4sNCoKnPzQwXEq1AJxEdd9Jw27GbuZ5ieoYMdh76FKpFEoxa8jVkFYMafyorxFHSutrgmFy8VbwCfD",
                    "K1");
        });
    }

    @Test
    void random32BytesAsHex() {
        String str32BytesInHex = Utils.random32BytesAsHex();
        assertEquals(64, str32BytesInHex.length(), "Message should be 64 characters");
    }

    @Test
    void random32Bytes() {
        byte[] bytes32 = Utils.random32Bytes();
        assertEquals(32, bytes32.length, "Message should be 32 bytes long");
    }

    @Test
    void getNumHash() {
        String input = "0065250e63fa600c8af940f8bfc154286ddcbb55fc9161629a5953a77ca28292";
        assertEquals(9486, Utils.getNumHash(input));
        String input1 = "0081f0f59789f1442c18736da34a36979768a6233f60452e41e8e601ab5314db";
        assertEquals(61685, Utils.getNumHash(input1));
    }

    @Test
    void getLastIrreversibleBlockPrefix() {
        Assertions.assertDoesNotThrow(() -> {
            String input = "0065250e63fa600c8af940f8bfc154286ddcbb55fc9161629a5953a77ca28292";
            assertEquals(4165007754L, Utils.getLastIrreversibleBlockPrefix(input));
        });
    }

    @Test
    void isJsonEmptyArray() {
        Assertions.assertFalse(Utils.isJsonEmptyArray("everitoken"));
        Assertions.assertFalse(Utils.isJsonEmptyArray("{}"));
        Assertions.assertFalse(Utils.isJsonEmptyArray("[\"everitoken\"]"));
        Assertions.assertTrue(Utils.isJsonEmptyArray("[]"));
    }
}