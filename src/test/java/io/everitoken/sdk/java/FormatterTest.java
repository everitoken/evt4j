package io.everitoken.sdk.java;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FormatterTest {

    private List<String> validNames = Arrays.asList("isname111111o", "a", "1", "5", "sam5", "sam", "adam.applejjj");

    @Test
    void testGetCharIndex() {
        Assertions.assertEquals(0, Formatter.getCharIndex('.'));
        Assertions.assertEquals(31, Formatter.getCharIndex('5'));
        Assertions.assertEquals(0, Formatter.getCharIndexFor128('.'));
        Assertions.assertEquals(38, Formatter.getCharIndexFor128('A'));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Assertions.assertEquals(0, Formatter.getCharIndex('A')));
    }

    @Test
    void encodeNameTest() {
        Assertions.assertEquals("58923", Formatter.encodeName("eos").toString());
        Assertions.assertEquals("13760711810338383659", Formatter.encodeName("eos111111111n").toString());

        // throw error when name length is 13 and last character is above "o"
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Formatter.encodeName("foobarbarz12p");
        });

        Assertions.assertDoesNotThrow(() -> {
            Formatter.encodeName("foobarbarz12o");
            Formatter.encodeName("1");
            Formatter.encodeName("2");
        });
    }

    @Test
    void decodeNameTest() {
        Assertions.assertEquals(".eos", Formatter.decodeName(Formatter.encodeName(".eos")));
        validNames.forEach(name -> Assertions.assertEquals(name, Formatter.decodeName(Formatter.encodeName(name))));
    }

    @Test
    void encodeName128Test() {
        Assertions.assertEquals(Formatter.encodeName128("123"), "0c440100");
        Assertions.assertEquals(Formatter.encodeName128("12345"), "0c44611c");
        Assertions.assertEquals(Formatter.encodeName128("123456"), "0d44611c08000000");
        Assertions.assertEquals(Formatter.encodeName128("1234567890"), "0d44611c48a22c02");
        Assertions.assertEquals(Formatter.encodeName128("1234567890A"), "0e44611c48a22c8209000000");
        Assertions.assertEquals(Formatter.encodeName128("11111111111"), "0ec3300cc3300cc300000000");
        Assertions.assertEquals(Formatter.encodeName128("1234567890ABCDE"), "0e44611c48a22c8279a2a90a");
        Assertions.assertEquals(Formatter.encodeName128("1234567890ABCDEF"), "0f44611c48a22c8279a2a9ba02000000");
        Assertions.assertEquals(Formatter.encodeName128("1234567890ABCDEFGHIJK"), "0f44611c48a22c8279a2a9bab2adfbc2");
    }
}