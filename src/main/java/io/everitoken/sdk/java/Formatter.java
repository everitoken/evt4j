package io.everitoken.sdk.java;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;

public class Formatter {
    private static String CHAR_MAP = ".abcdefghijklmnopqrstuvwxyz12345";

    public static void main(String[] args) {
        String s = decodeName(encodeName(".eos"));
        System.out.println(s);
        // System.out.println(new BigInteger(1, encodeName("eos", true)));
    }

    public static String decodeName(BigInteger nameValue) {
        Long aLong = Long.parseUnsignedLong(nameValue.toString(2), 2);
        ByteBuffer bs = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(aLong);

        String binaryString = new BigInteger(1, bs.array()).toString(2);
        String pad = String.join("", Collections.nCopies(64 - binaryString.length(), "0"));
        String paddedBinaryString = pad + binaryString;

        int i = 0;
        int step = 5;

        String name = "";

        while (i < paddedBinaryString.length()) {
            if (i == 60) {
                step = 4;
            }

            int index = Integer.parseInt(paddedBinaryString.substring(i, i + step), 2);
            i = i + step;

            if (index >= CHAR_MAP.length()) {
                throw new IllegalArgumentException("Failed to decode name");
            }

            name = String.format("%s%s", name, CHAR_MAP.charAt(index));
        }

        // replace all the dots "." at end, in binary "00000" will result a "."
        return name.replaceAll("\\.+$", "");
    }

    public static BigInteger encodeName(@NotNull String name) {

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

        ByteBuffer bf = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(parseLong);

        return new BigInteger(1, bf.array());
    }

    public static int getCharIndex(char c) {
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
