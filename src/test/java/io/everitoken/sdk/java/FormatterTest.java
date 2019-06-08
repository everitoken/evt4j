package io.everitoken.sdk.java;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FormatterTest {

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
        Assertions.assertEquals("58923", Formatter.encodeName("eos", true).toString());
        Assertions.assertEquals("13760711810338383659", Formatter.encodeName("eos111111111n", true).toString());
        Assertions.assertEquals("3163706099524106174", Formatter.encodeName("eos111111111n", false).toString());

        // throw error when name length is 13 and last character is above "o"
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Formatter.encodeName("foobarbarz12p", false);
        });

        Assertions.assertDoesNotThrow(() -> {
            Formatter.encodeName("foobarbarz12o");
            Formatter.encodeName("1");
            Formatter.encodeName("2");
        });
    }
}