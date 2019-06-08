package io.everitoken.sdk.java;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public class Formatter {
    private static String CHAR_MAP = ".abcdefghijklmnopqrstuvwxyz12345";
    private static String CHAR_MAP_128 = ".-0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String decodeName(BigInteger nameValue) {
        return decodeName(nameValue, true);
    }

    public static String decodeName(BigInteger nameValue, boolean littleEndian) {
        long aLong = Long.parseUnsignedLong(nameValue.toString(2), 2);
        ByteBuffer bs = ByteBuffer.allocate(8).order(littleEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN)
                .putLong(aLong);

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

    public static String encodeName128(@NotNull String name) {

        if (name.length() > 21) {
            throw new IllegalArgumentException("A name can be up to 21 characters long.");
        }

        String binaryString = "";
        String pad = "000000";

        // convert to binary string
        for (int i = 0; i < 21; i++) {
            // pad with 0 if name is less than 13 chars
            int charIndex = i < name.length() ? getCharIndexFor128(name.charAt(i)) : 0;

            String partBinaryString = Integer.toBinaryString(charIndex);

            if (partBinaryString.length() > 6) {
                throw new IllegalArgumentException("Invalid name");
            }

            binaryString = String.format("%s%s%s", pad.substring(0, 6 - partBinaryString.length()), partBinaryString,
                    binaryString);
        }

        int cutSize = 4;
        int nameLen = name.length();

        if (nameLen <= 5) {
            binaryString = binaryString + "00";
        } else if (nameLen <= 10) {
            binaryString = binaryString + "01";
            cutSize = 8;
        } else if (nameLen <= 15) {
            binaryString = binaryString + "10";
            cutSize = 12;
        } else {
            binaryString = binaryString + "11";
            cutSize = 16;
        }

        BigInteger bigInteger = new BigInteger(binaryString, 2);

        byte[] byteBufferArray = bigInteger.toByteArray();

        if (byteBufferArray.length > 16) {
            byteBufferArray = ArrayUtils.subarray(byteBufferArray, 1, byteBufferArray.length);
            System.out.println("Warning: encodeName128 generated hex more than 32 chars");
        }

        ArrayUtils.reverse(byteBufferArray);

        String hex = Utils.HEX.encode(byteBufferArray);
        return String.format("%s%s", hex, pad.substring(0, cutSize * 2 - hex.length()));
    }

    public static String decodeName128(String hexString) {
        byte[] bytes = Utils.HEX.decode(hexString);

        ByteBuffer bs = ByteBuffer.allocate(bytes.length).put(bytes);
        String byteString = "";

        bs.rewind();

        while (bs.hasRemaining()) {
            String binaryString = Integer.toBinaryString(bs.get() & 0xFF);
            String pad = String.join("", Collections.nCopies(8 - binaryString.length(), "0"));
            byteString = String.format("%s%s%s", pad, binaryString, byteString);
        }

        int length = Integer.valueOf(byteString.substring(byteString.length() - 2), 2);

        byteString = byteString.substring(0, byteString.length() - 2);

        String pad = String.join("", Collections.nCopies(126 - byteString.length(), "0"));

        String byteStringPadded = pad + byteString;

        String name = "";

        for (int i = 0; i < byteStringPadded.length(); i = i + 6) {
            int charIndex = Integer.valueOf(byteStringPadded.substring(i, i + 6), 2) % CHAR_MAP_128.length();
            name = CHAR_MAP_128.charAt(charIndex) + name;
        }

        return name.replaceAll("\\.+$", "");
    }

    public static int getCharIndex(char c) {
        int index = CHAR_MAP.indexOf(c);

        if (index == -1) {
            throw new IllegalArgumentException(String.format("Character \"%s\" is not valid.", c));
        }

        return index;
    }

    public static int getCharIndexFor128(char c) {
        int index = CHAR_MAP_128.indexOf(c);

        if (index == -1) {
            throw new IllegalArgumentException(String.format("Character \"%s\" is not valid.", c));
        }

        return index;
    }
}
