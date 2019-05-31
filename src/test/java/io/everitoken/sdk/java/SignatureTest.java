package io.everitoken.sdk.java;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SignatureTest {

    @Test
    void testConstructor() {
        Signature sig = new Signature(BigInteger.ZERO, BigInteger.ONE);
        Signature sig1 = new Signature(BigInteger.ZERO, BigInteger.ONE, 0);

        assertEquals(sig.getR(), sig1.getR());
        assertEquals(sig.getS(), sig1.getS());
        assertEquals(0, sig1.getRecId());
        assertThrows(IllegalArgumentException.class, sig::getRecId);
    }

    @Test
    @DisplayName("Sign and verify")
    void SignAndVerify() {
        Assertions.assertDoesNotThrow(() -> {
            String message = "helloworld";
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");
            Signature sig = Signature.sign(message.getBytes(), key);

            boolean verifyResult = Signature.verify(message.getBytes(), sig, key.toPublicKey());
            assertTrue(verifyResult, "Able to verify");
        });
    }

    @Test
    @DisplayName("sign and signHash produces same result")
    void signAndSignHash() {
        Assertions.assertDoesNotThrow(() -> {
            String message = "helloworldwhatnot";
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");
            Signature sig = Signature.sign(message.getBytes(), key);
            Signature sig1 = Signature.signHash(Utils.hash(message.getBytes()), key);

            assertEquals(sig, sig1);
        });
    }

    @Test
    @DisplayName("signHash")
    void signHash() {
        Assertions.assertDoesNotThrow(() -> {
            PrivateKey key = PrivateKey.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D");
            Signature sig = Signature.signHash(
                    Utils.HEX.decode("08d576d1aa63a53daa610744989eb1997506c2dd9a86af67af51707ea81b8dae"), key);
            Assertions.assertEquals(
                    "SIG_K1_KfdgiuhCZFSx9ggL4sNCoKnPzQwXEq1AJxEdd9Jw27GbuZ5ieoYMdh76FKpFEoxa8jVkFYMafyorxFHSutrgmFy8VbwCfD",
                    sig.toString());
        });
    }

    @Test
    @DisplayName("signHash and verifyHash should only work with byte[] of length 32")
    void invalidHashLength() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");
            Signature.signHash(new byte[] {}, key);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            String message = "someOtherMessage";
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");
            Signature sig = Signature.sign(message.getBytes(), key);
            Signature.verifyHash(new byte[] {}, sig, key.toPublicKey());
        });
    }

    @Test
    @DisplayName("verify and verifyHash")
    void verifyAndVerifyHash() {
        Assertions.assertDoesNotThrow(() -> {
            String message = "helloworld";
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");
            Signature sig = Signature.sign(message.getBytes(), key);
            Signature sig1 = Signature.signHash(Utils.hash(message.getBytes()), key);

            boolean verifyResult = Signature.verify(message.getBytes(), sig1, key.toPublicKey());
            boolean verifyResult1 = Signature.verifyHash(Utils.hash(message.getBytes()), sig, key.toPublicKey());
            assertTrue(verifyResult, "Able to verify");
            assertTrue(verifyResult1, "Able to verify");
        });
    }

    @Test
    @DisplayName("Signature has Recover Id")
    void SignatureHasRecId() {
        Assertions.assertDoesNotThrow(() -> {
            String message = "helloworld";
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");
            Signature sig = Signature.sign((message.getBytes()), key);
            assertEquals(0, sig.getRecId());
        });
    }

    @Test
    @DisplayName("Can recover public key from signature")
    void RecoverPublicKeyFromSignatureSuccessful() {
        Assertions.assertDoesNotThrow(() -> {
            String[] messages = new String[] { "helloworld", "foo", "bar", "baz", "evt", "evtjs", "everitoken.io" };
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");

            Arrays.asList(messages).forEach(message -> {
                Signature sig = Signature.sign((message.getBytes()), key);
                PublicKey publicKey = Signature.recoverPublicKey(Utils.hash(message.getBytes()), sig);

                assertTrue(PublicKey.isValidPublicKey(publicKey.toString()));
                assertEquals(publicKey.toString(), key.toPublicKey().toString());
            });
        });
    }

    @Test
    @DisplayName("Return false when public key recovered from signature doesn't match")
    void RecoverPublicKeyFromSignatureFailed() {
        Assertions.assertDoesNotThrow(() -> {
            String message = "helloworld";
            PrivateKey key = PrivateKey.of("5JV1kctxPzU3BdRENgRyDcUWQSqqzeckzjKXJWSkBoxXmXUCqKB");
            Signature sig = Signature.sign((message.getBytes()), key);

            String wrongMessage = "foobar";
            PublicKey publicKey = Signature.recoverPublicKey(Utils.hash(wrongMessage.getBytes()), sig);
            assertNotEquals(publicKey.toString(), key.toPublicKey().toString());
        });
    }

    @Test
    @DisplayName("Init signature object from string")
    void newSignature() {
        Assertions.assertDoesNotThrow(() -> {
            String sig = "SIG_K1_KfdgiuhCZFSx9ggL4sNCoKnPzQwXEq1AJxEdd9Jw27GbuZ5ieoYMdh76FKpFEoxa8jVkFYMafyorxFHSutrgmFy8VbwCfD";
            Signature signature = Signature.of(sig);

            assertEquals("5334a049e9bd38c8f6d9a92dac39bf07e6e5a5e0c04a668f788f7c514ef9fea6",
                    Utils.HEX.encode(signature.getR().toByteArray()));
            assertEquals("14b0de5c1d94c8e7e4bc1786fbfdcd1d0307d8194d770303dddd7e38df9a2fcf",
                    Utils.HEX.encode(signature.getS().toByteArray()));
            assertEquals(1, signature.getRecId());
        });
    }

    @Test
    @DisplayName("Edge case")
    void testEdgeCase() {
        String target = "SIG_K1_KiQbpiJSi9f3MUDP9xnkHF6m3Fpj7um7kvXAvmjEqMuKctB1pupPyxHPNNZQ3MGGnjqpXP69rSoL8auvoZC7rQ6HtGU8CQ";
        PrivateKey key = PrivateKey.of("5J7KoMB4P9cQCD2P529Zmscz3sH3oNVRQ1ZnHXVk68Y15uByTNe");

        Signature sig = Signature.sign(String.valueOf(45).getBytes(), key);

        assertEquals(target, sig.toString());
    }
}