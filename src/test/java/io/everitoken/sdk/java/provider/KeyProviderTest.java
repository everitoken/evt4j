package io.everitoken.sdk.java.provider;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.exceptions.WifFormatException;

class KeyProviderTest {
    private final String validPrivateKey = "5JswhuLDEq7BENcNsu41Eg7dZCiv4TG8WffTNvbexbC1tyCN9EU";
    private final String validPrivateKey1 = "5JswhuLDEq7BENcNsu41Eg7dZCiv4TG8WffTNvbexbC1tyCN9EU";
    private final String invalidPrivateKey = "5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER7XsAR2eCcpt3D";

    @Test
    @DisplayName("Single valid private key doesn't throw exception")
    void initWithSingleValidPrivateKey() {
        Assertions.assertDoesNotThrow(() -> {
            KeyProvider.of(validPrivateKey);
        });
    }

    @Test
    @DisplayName("Single invalid private key throws exception")
    void initWithSingleInvalidPrivateKeyGetException() {
        Assertions.assertThrows(WifFormatException.class, () -> {
            KeyProvider.of(invalidPrivateKey);
        });
    }

    @Test
    @DisplayName("Multiple valid private key doesn't throw exception")
    void initWithMultipleValidPrivateKey() {
        Assertions.assertDoesNotThrow(() -> {
            String[] keyList = { validPrivateKey, validPrivateKey1 };
            KeyProvider.of(keyList);
        });
    }

    @Test
    void testGet() {
        String[] keyList = { validPrivateKey, validPrivateKey1 };
        KeyProvider provider = KeyProvider.of(keyList);
        Assertions.assertEquals(validPrivateKey, provider.get().get(0).toWif());
    }

    @Test
    @DisplayName("Multiple Invalid private key throws exception")
    void initWithMultipleInvalidPrivateKeyGetException() {
        Assertions.assertThrows(WifFormatException.class, () -> {
            String[] keyList = { validPrivateKey, invalidPrivateKey };
            KeyProvider.of(keyList);
        });
    }

}
