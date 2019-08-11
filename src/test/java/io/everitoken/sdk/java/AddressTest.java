package io.everitoken.sdk.java;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.exceptions.InvalidAddressException;

class AddressTest {
    private final String validPublicKey = "EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa";

    @Test
    @DisplayName("Valid and invalid addresses")
    void isValidAddress() {
        assertTrue(Address.isValidAddress("EVT00000000000000000000000000000000000000000000000000"));
        assertTrue(Address.isValidAddress("EVT0000008T1TPuhA6gciep9YFw3vAibChMEMqJrmZxi3j5UAkgFq"));
        assertTrue(Address.isValidAddress("EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa"));
        assertTrue(Address.isValidAddress("EVT0000005ZbVoKRDdy6N4r22sSn3WDyB4YkTcf5R1dSjAsmRnFEF"));
        assertFalse(Address.isValidAddress("EOS6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"));
        assertFalse(Address.isValidAddress("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDWFRvsv2FxgND"));
    }

    @Test
    @DisplayName("Constructor with public key")
    void constructAddressWithPublicKey() {
        Assertions.assertDoesNotThrow(() -> {
            PublicKey key = PublicKey.of(validPublicKey);
            Address addr = Address.of(key);
            assertEquals(addr.toString(), key.toString());
        });

        Assertions.assertDoesNotThrow(() -> {
            Address.of("EVT00000000000000000000000000000000000000000000000000");
        });
    }

    @Test
    @DisplayName("Throw exceptions when having invalid addresses")
    void errorOutWithInvalidAddress() {
        Assertions.assertThrows(InvalidAddressException.class,
                () -> Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDWFRvsv2FxgN"));
    }

}
