package io.everitoken.sdk.java;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.jetbrains.annotations.NotNull;

public class Formatter {

    public static void main(String[] args) {
        BigInteger name = encodeName("1");
        System.out.println(name.toString());
    }

    public static BigInteger encodeName(@NotNull String name) {
        return encodeName(name, true);
    }

    public static BigInteger encodeName(@NotNull String name, boolean littleEndian) {

        if (name.length() > 13) {
            throw new IllegalArgumentException("A name can be up to 13 characters long.");
        }

        String binaryString = "";
        String pad = "00000";

        // convert to binary string
        for (int i = 0; i <= 12; i++) {
            // pad with 0 if name is less than 13 chars
            int charIndex = i < name.length() ? getCharIndex(name.charAt(i)) : 0;
            int desiredByteLength = i < 12 ? 5 : 4; // 5 * 12 + 4 = 64

            // the last 4 bits can only hold up to "o", but not "p"
            if (desiredByteLength == 4 && charIndex > 15) {
                throw new IllegalArgumentException("Invalid name");
            }

            String partBinaryString = Integer.toBinaryString(charIndex);
            binaryString = String.format("%s%s%s", binaryString,
                    pad.substring(0, desiredByteLength - partBinaryString.length()), partBinaryString);
        }

        // convert to BigInteger
        long parseLong = Long.parseUnsignedLong(binaryString, 2);
        ByteBuffer bf = ByteBuffer.allocate(8).order(littleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN)
                .putLong(parseLong);

        return new BigInteger(1, bf.array());
    }

    public static int getCharIndex(char c) {
        String CHAR_MAP = ".abcdefghijklmnopqrstuvwxyz12345";
        int index = CHAR_MAP.indexOf(c);

        if (index == -1) {
            throw new IllegalArgumentException(String.format("Character \"%s\" is not valid.", c));
        }

        return index;
    }

    public static int getCharIndexFor128(char c) {
        String CHAR_MAP_128 = ".-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = CHAR_MAP_128.indexOf(c);

        if (index == -1) {
            throw new IllegalArgumentException(String.format("Character \"%s\" is not valid.", c));
        }

        return index;
    }
}
