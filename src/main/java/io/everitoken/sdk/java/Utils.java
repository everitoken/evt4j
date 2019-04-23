package io.everitoken.sdk.java;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Hashtable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.io.BaseEncoding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.apache.commons.lang3.ArrayUtils;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Sha256Hash;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

import io.everitoken.sdk.java.exceptions.Base58CheckException;

public class Utils {
    public static final BaseEncoding HEX = BaseEncoding.base16().lowerCase();

    private static byte[] ripemd160(byte[] data) {
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(data, 0, data.length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);
        return out;
    }

    public static void main(String[] args) {
        System.out.println((int) 1_000_000_000);
        System.out.println(Integer.MAX_VALUE);
    }

    public static String base58Check(byte[] key) {
        return base58Check(key, null);
    }

    @NotNull
    public static String base58Check(byte[] key, @Nullable String keyType) {
        byte[] check = key;

        if (keyType != null) {
            check = ArrayUtils.addAll(key, keyType.getBytes());
        }

        byte[] hash = ripemd160(check);
        byte[] concat = ArrayUtils.addAll(key, ArrayUtils.subarray(hash, 0, 4));
        return Base58.encode(concat);
    }

    public static byte[] base58CheckDecode(String key) throws Base58CheckException {
        return base58CheckDecode(key, null);
    }

    public static byte[] base58CheckDecode(String key, @Nullable String keyType) throws Base58CheckException {
        byte[] decoded;

        try {
            // base58 decode
            decoded = Base58.decode(key);
        } catch (AddressFormatException ex) {
            throw new Base58CheckException(ex.getMessage(), ex);
        }
        // split the byte slice
        byte[] data = ArrayUtils.subarray(decoded, 0, decoded.length - 4);
        byte[] checksum = ArrayUtils.subarray(decoded, decoded.length - 4, decoded.length);

        if (keyType != null) {
            data = ArrayUtils.addAll(data, keyType.getBytes());
        }

        // ripemd160 input, sign 4 bytes to compare
        byte[] hash = ripemd160(data);

        // if pass, return data, otherwise throw ex
        // compare two checksum
        boolean isEqual = true;

        for (int i = 0; i < checksum.length; i++) {
            if (hash[i] != checksum[i]) {
                isEqual = false;
            }
        }

        if (!isEqual) {
            throw new Base58CheckException();
        }

        if (keyType != null) {
            return ArrayUtils.subarray(data, 0, data.length - keyType.getBytes().length);
        }

        return data;
    }

    @NotNull
    public static String randomName128() {
        String candidates = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.-";
        int length = candidates.length();
        StringBuilder sb = new StringBuilder();
        byte[] random = random32Bytes();

        for (int i = 0; i < 21; i++) {
            sb.append(candidates.charAt(random[i] & 0xff % length));
        }

        return sb.toString();
    }

    public static String random32BytesAsHex() {
        byte[] randomBytes = random32Bytes();
        return HEX.encode(randomBytes);
    }

    public static byte[] random32Bytes() {
        SecureRandom random = new SecureRandom();
        byte[] values = new byte[32];
        random.nextBytes(values);
        return values;
    }

    public static boolean isJsonEmptyArray(String string) {
        try {
            JSONArray array = JSONArray.parseArray(string);
            return array.size() == 0;
        } catch (JSONException ex) {
            return false;
        }
    }

    public static byte[] hash(byte[] data) {
        return Sha256Hash.hash(data);
    }

    public static String jsonPrettyPrint(Object raw) {
        return JSON.toJSONString(raw, SerializerFeature.PrettyFormat);
    }

    public static int getNumHash(String hash) {
        return Integer.parseUnsignedInt(hash.substring(4, 8), 16);
    }

    public static long getLastIrreversibleBlockPrefix(String hash) {
        byte[] input = Utils.HEX.decode(hash);
        return Integer
                .toUnsignedLong(ByteBuffer.wrap(input, 8, input.length - 8).order(ByteOrder.LITTLE_ENDIAN).getInt());
    }

    public static DateTime getCorrectedTime(String referenceTime) {

        DateTime dateTime = new DateTime(referenceTime);

        // TODO: Dirty hack to sync local time
        DateTime local = new DateTime();
        LocalDateTime utc = local.withZone(DateTimeZone.UTC).toLocalDateTime();
        DateTime newLocal = new DateTime(utc.toString());
        // Dirty hack

        Duration diff = Duration.millis(dateTime.getMillis() + 70 - newLocal.getMillis());
        return dateTime.minus(diff);
    }

    public static String getQrImageDataUri(String rawText) throws WriterException, IOException {
        byte[] qr = getQrImageInBytes(rawText);

        String data = new String(Base64.getEncoder().encode(qr), StandardCharsets.UTF_8);

        return String.format("data:image/png;base64,%s", data);
    }

    public static byte[] getQrImageInBytes(String rawText) throws WriterException, IOException {
        int width = 600;
        int height = 600;

        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(rawText, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }
}
