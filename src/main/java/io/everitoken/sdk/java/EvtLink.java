package io.everitoken.sdk.java;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;

import io.everitoken.sdk.java.apiResource.EvtLinkStatus;
import io.everitoken.sdk.java.dto.TokenDomain;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.exceptions.EvtLinkException;
import io.everitoken.sdk.java.exceptions.EvtLinkSyncTimeException;
import io.everitoken.sdk.java.param.EvtLinkStatusParam;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.PublicKeysParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.provider.SignProviderInterface;

public class EvtLink {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ$+-/:*";
    private static final int BASE = ALPHABET.length();
    private static final String QR_PREFIX = "https://evt.li/";
    private final NetParams netParams;
    private DateTime correctedTime;

    public EvtLink(final NetParams netParams) {
        this.netParams = netParams;
    }

    public static EveriPassVerificationResult validateEveriPassUnsafe(NetParams netParams, String link) {
        return validateEveriPassUnsafe(netParams, EvtLink.parseLink(link, true));
    }

    public static EveriPassVerificationResult validateEveriPassUnsafe(NetParams netParams, ParsedLink parsedLink) {
        if (!ParsedLink.isEveriPass(parsedLink)) {
            throw new EvtLinkException("Flag is not correct for everiPass");
        }

        // get timestamp
        Optional<EvtLink.Segment> timestampSegment = parsedLink.getSegments().stream()
                .filter(segment -> segment.getTypeKey() == 42).findFirst();

        if (!timestampSegment.isPresent()) {
            throw new EvtLinkException("Failed to parse EveriPass link to extract \"timestamp\"");
        }

        long timestampInMilli = getUnsignedInt(timestampSegment.get().getContent()) * 1000;

        if (Math.abs(DateTime.now().getMillis() - timestampInMilli) > 6 * 10000) {
            throw new EvtLinkException("EveriPass is already expired");
        }

        // get domain
        Optional<EvtLink.Segment> domainSegment = parsedLink.getSegments().stream()
                .filter(segment -> segment.getTypeKey() == 91).findFirst();

        if (!domainSegment.isPresent()) {
            throw new EvtLinkException("Failed to parse EveriPass link to extract \"domain\"");
        }

        // get token name
        Optional<EvtLink.Segment> tokenSegment = parsedLink.getSegments().stream()
                .filter(segment -> segment.getTypeKey() == 92).findFirst();

        if (!tokenSegment.isPresent()) {
            throw new EvtLinkException("Failed to parse EveriPass link to extract \"token name\"");
        }

        String domain = new String(domainSegment.get().getContent(), StandardCharsets.UTF_8);
        String tokenName = new String(tokenSegment.get().getContent(), StandardCharsets.UTF_8);

        if (parsedLink.getPublicKeys().size() != 1) {
            throw new EvtLinkException(
                    "For unsafe validation of everiPass, evtLink must have one and only one " + "signature.");
        }

        try {
            List<TokenDomain> ownedTokens = new Api(netParams)
                    .getOwnedTokens(PublicKeysParams.of(parsedLink.getPublicKeys()));

            boolean ownToken = ownedTokens.stream()
                    .anyMatch(token -> token.getDomain().equals(domain) && token.getName().equals(tokenName));

            return new EveriPassVerificationResult(ownToken, domain, tokenName);
        } catch (ApiResponseException ex) {
            throw new EvtLinkException(
                    String.format("Can't get owned tokens from the public keys, detailed error: " + "%s", ex.getRaw()),
                    ex);
        }
    }

    private static List<Segment> parseSegments(final byte[] segmentBytes) {
        final List<Segment> segments = new ArrayList<>();
        int offset = 0;

        while (offset < segmentBytes.length) {
            final Segment segment = parseSegment(segmentBytes, offset);
            offset += segment.getLength();
            segments.add(segment);
        }

        return segments;
    }

    private static String generateQRCode(final int flag, final List<byte[]> segments,
            @Nullable final SignProviderInterface signProvider) {

        // sort segments based on type key
        segments.sort(Comparator.comparingInt(a -> a[0] & 0xff));

        // put flag in first
        byte[] contentBytes = ByteBuffer.allocate(2).putShort((short) flag).array();

        for (final byte[] segment : segments) {
            contentBytes = ArrayUtils.addAll(contentBytes, segment);
        }

        String content = encode(contentBytes);

        if ((flag & 16) == 16) {
            content = QR_PREFIX + content;
        }

        if (signProvider != null) {
            byte[] signaturesBytes = new byte[] {};
            final List<Signature> signatures = signProvider.sign(Utils.hash(contentBytes));

            if (signatures.size() > 3) {
                throw new EvtLinkException(
                        String.format("Only 3 signatures are allowed, \"%d\" passed", signatures.size()));
            }

            for (final Signature signature : signatures) {
                signaturesBytes = ArrayUtils.addAll(signaturesBytes, signature.getBytes());
            }

            content = String.format("%s_%s", content, encode(signaturesBytes));
        }

        return content;
    }

    public static ParsedLink parseLink(final String rawContent, final boolean recoverPublicKey) {
        int rawContentLength = rawContent.length();

        if (rawContentLength < 3 || rawContentLength > 2000) {
            throw new EvtLinkException(String.format("Invalid EvtLink length of \"%d\"", rawContentLength));
        }

        final String[] parts = rawContent.split("_");

        // check prefix
        if (parts[0].startsWith(QR_PREFIX)) {
            parts[0] = parts[0].substring(QR_PREFIX.length());
        }

        final byte[] contentBytes = EvtLink.decode(parts[0]);

        final int flag = ByteBuffer.allocate(2).put(contentBytes, 0, 2).getShort(0) & 0xffff;

        final List<Signature> signatures = new ArrayList<>();
        final List<PublicKey> publicKeys = new ArrayList<>();

        if (parts.length > 1) {
            final byte[] sigBytes = EvtLink.decode(parts[1]);
            for (int i = 0; i < sigBytes.length; i = i + Signature.BUFFER_LENGTH) {
                final byte[] singleSigBytes = ByteBuffer.allocate(Signature.BUFFER_LENGTH)
                        .put(sigBytes, i, Signature.BUFFER_LENGTH).array();
                final Signature sig = Signature.of(singleSigBytes);
                signatures.add(sig);

                if (recoverPublicKey) {
                    final PublicKey publicKey = Signature.recoverPublicKey(Utils.hash(contentBytes), sig);
                    publicKeys.add(publicKey);
                }
            }
        }

        return new ParsedLink(flag, parseSegments(ArrayUtils.subarray(contentBytes, 2, contentBytes.length)),
                signatures, publicKeys);
    }

    public static String getUniqueLinkId() {
        final SecureRandom random = new SecureRandom();
        final byte[] values = new byte[16];
        random.nextBytes(values);

        return Utils.HEX.encode(values);
    }

    public static Segment parseSegment(final byte[] content, final int offset) {
        final int type = content[offset] & 0xff;

        if (type <= 20) {
            return new Segment(type, ByteBuffer.allocate(1).put(content[offset + 1]).array(), 2);
        } else if (type <= 40) {
            return new Segment(type, ByteBuffer.allocate(2).put(content, 1 + offset, 2).array(), 3);
        } else if (type <= 90) {
            return new Segment(type, ByteBuffer.allocate(4).put(content, 1 + offset, 4).array(), 5);
        } else if (type <= 155) {
            final int contentLength = content[offset + 1] & 0xff;
            return new Segment(type, ByteBuffer.allocate(contentLength).put(content, 2 + offset, contentLength).array(),
                    contentLength + 2);
        } else if (type <= 165) {
            final int contentLength = 16;
            return new Segment(type, ByteBuffer.allocate(contentLength).put(content, 1 + offset, contentLength).array(),
                    contentLength + 1);
        } else if (type <= 180) {
            final int contentLength = content[offset + 1] & 0xff;
            return new Segment(type, ByteBuffer.allocate(contentLength).put(content, 2 + offset, contentLength).array(),
                    contentLength + 2);
        } else {
            throw new EvtLinkException(String.format("Segment type %d is not supported", type));
        }
    }

    public static byte[] createSegment(final int type, final byte[] content) {
        if (type < 0 || type > 255) {
            throw new EvtLinkException("Invalid type value, it must be within 0 to 255");
        }

        if (type <= 20) {
            final ByteBuffer b = ByteBuffer.wrap(new byte[2]);
            b.put((byte) type);
            b.put(content, 0, 1);
            return b.array();
        } else if (type <= 40) {
            final ByteBuffer b = ByteBuffer.wrap(new byte[3]);
            b.put((byte) type);
            b.put(content, 0, 2);
            return b.array();
        } else if (type <= 90) {
            final ByteBuffer b = ByteBuffer.wrap(new byte[5]);
            b.put((byte) type);
            b.put(content, 0, 4);
            return b.array();
        } else if (type <= 155) {
            final ByteBuffer b = ByteBuffer.wrap(new byte[2]);
            b.put((byte) type);
            b.put((byte) content.length);
            return ArrayUtils.addAll(b.array(), content);
        } else if (type <= 165) {
            final ByteBuffer b = ByteBuffer.wrap(new byte[1]);
            b.put((byte) type);
            return ArrayUtils.addAll(b.array(), content);
        } else if (type <= 180) {
            final ByteBuffer b = ByteBuffer.wrap(new byte[2]);
            b.put((byte) type);
            b.put((byte) content.length);
            return ArrayUtils.addAll(b.array(), content);
        } else {
            throw new EvtLinkException(String.format("Segment type %d is not supported", type));
        }
    }

    public static String encode(final byte[] input) {
        if (input.length == 0) {
            return "";
        }

        // build prefix string buffer
        final StringBuilder prefix = new StringBuilder();

        for (final byte i : input) {
            if ((int) i != 0) {
                break;
            }
            prefix.append("0");
        }

        BigInteger bigInteger = new BigInteger(input);
        final BigInteger baseBn = BigInteger.valueOf(BASE);
        final StringBuilder sb = new StringBuilder();

        while (bigInteger.compareTo(BigInteger.ZERO) > 0) {
            final BigInteger mod = bigInteger.mod(baseBn);
            bigInteger = bigInteger.subtract(mod).divide(baseBn);
            sb.append(ALPHABET.charAt(mod.intValue()));
        }

        return prefix.append(sb.reverse()).toString();
    }

    public static byte[] decode(final String base42EncodedString) {
        if (base42EncodedString.length() == 0) {
            return new byte[] {};
        }

        BigInteger resultBn = BigInteger.ZERO;
        final BigInteger baseBn = BigInteger.valueOf(BASE);

        int leadingZerosCount = 0;

        for (int i = 0; i < base42EncodedString.length(); i++) {
            if (base42EncodedString.charAt(i) != '0') {
                break;
            }

            leadingZerosCount++;
        }

        for (int i = 0; i < base42EncodedString.length(); i++) {
            final char c = base42EncodedString.charAt(i);
            final int index = ALPHABET.indexOf(c);

            if (ALPHABET.indexOf(c) == -1) {
                throw new EvtLinkException(String.format("Illegal character found \"%s\" at index %d", c, i));
            }

            resultBn = resultBn.multiply(baseBn).add(BigInteger.valueOf(index));
        }

        return ArrayUtils.addAll(new byte[leadingZerosCount], resultBn.toByteArray());
    }

    public String getEvtLinkForPayeeCode(@NotNull final EveriLinkPayeeCodeParam param) {
        int flag = 1 + 16;
        byte[] addressBytes = createSegment(95, param.getAddress().getBytes());
        byte[] fungibleIdBytes = {};
        byte[] amountBytes = {};

        if (param.getFungibleId() != null) {
            fungibleIdBytes = createSegment(45, ByteBuffer.allocate(4).putInt(param.getFungibleId()).array());
        }

        if (param.getAmount() != null) {
            amountBytes = createSegment(96, param.getAmount().getBytes());
        }

        if (param.getFungibleId() != null && param.getAmount() != null) {
            return generateQRCode(flag, Arrays.asList(addressBytes, fungibleIdBytes, amountBytes), null);
        }

        return generateQRCode(flag, Arrays.asList(addressBytes), null);
    }

    public static long getUnsignedInt(final byte[] bytes) {
        return Long.parseUnsignedLong(Utils.HEX.encode(bytes), 16);
    }

    public String getEvtLinkForEveriPay(@NotNull final EveriPayParam param,
            @Nullable final SignProviderInterface signProvider) {
        final int flag = 1 + 4;

        DateTime localTime = new DateTime();
        final byte[] timestampBytes = createSegment(42,
                ArrayUtils.subarray(ByteBuffer.allocate(8).putLong(localTime.getMillis() / 1000).array(), 4, 8));

        final byte[] symbolBytes = createSegment(44, ByteBuffer.allocate(4).putInt(param.getSymbol()).array());
        final byte[] maxAmountBytes;

        final long MAX_32_BITS_UNSIGNED_VALUE = 4_294_967_295L;

        if (param.getMaxAmount() >= MAX_32_BITS_UNSIGNED_VALUE) {
            maxAmountBytes = createSegment(94, Long.toString(param.getMaxAmount()).getBytes());
        } else {
            // FIXME: Here comes the fucking hack, trying to pack long type in to 4 bytes
            final byte[] lBytes = ArrayUtils.subarray(ByteBuffer.allocate(8).putLong(param.getMaxAmount()).array(), 4,
                    8);

            maxAmountBytes = createSegment(43, lBytes);
        }

        final byte[] linkIdBytes = createSegment(156, Utils.HEX.decode(param.getLinkId()));

        return generateQRCode(flag, Arrays.asList(timestampBytes, symbolBytes, maxAmountBytes, linkIdBytes),
                signProvider);
    }

    public String getEvtLinkForEveriPass(@NotNull final EveriPassParam param,
            @Nullable final SignProviderInterface signProvider) {

        final int flag = 1 + 2 + (param.isAutoDestroy() ? 8 : 0);
        DateTime localTime = new DateTime();
        final byte[] timestampBytes = createSegment(42,
                ArrayUtils.subarray(ByteBuffer.allocate(8).putLong(localTime.getMillis() / 1000).array(), 4, 8));

        final byte[] domainBytes = createSegment(91, param.getDomain().getBytes());
        final byte[] tokenBytes = createSegment(92, param.getToken().getBytes());

        return generateQRCode(flag, Arrays.asList(timestampBytes, domainBytes, tokenBytes), signProvider);
    }

    private DateTime getCorrectedTime() {
        if (correctedTime == null) {
            try {
                final Api api = new Api(netParams);
                correctedTime = Utils.getCorrectedTime(api.getInfo().getHeadBlockTime());
            } catch (final ApiResponseException ex) {
                throw new EvtLinkSyncTimeException("Unable sync time with node", ex);
            }
        }

        return correctedTime;
    }

    public Map<String, String> getStatusOfEvtLink(EvtLinkStatusParam params) throws ApiResponseException {
        Map<String, String> rst = new HashMap<>();
        boolean isOnline = false;
        DateTime startTime = DateTime.now();
        rst.put("success", "false");

        do {
            try {
                JSONObject json = new EvtLinkStatus(params.isBlock()).request(RequestParams.of(netParams, () -> {
                    JSONObject payload = new JSONObject();
                    payload.put("link_id", params.getLinkId());
                    return payload.toString();
                }));

                isOnline = true;

                if (json.getString("trx_id") != null && json.getInteger("block_num") > 0) {
                    rst.put("pending", "false");
                    rst.put("success", "true");
                    rst.put("trx_id", json.getString("trx_id"));
                    rst.put("block_num", Integer.toString(json.getInteger("block_num")));
                    return rst;
                }
            } catch (Exception ex) {
                if (params.isThrowException()) {
                    throw new EvtLinkException("EveriPay can not be confirmed", ex);
                }

                rst.put("pending", "true");

                if (ex instanceof ApiResponseException) {
                    isOnline = true;
                    rst.put("error", ((ApiResponseException) ex).getRaw().toString());
                } else if (ex instanceof JSONException) {
                    isOnline = true;
                    rst.put("error", ex.getMessage());
                } else {
                    rst.put("error", ex.getMessage());
                }
            }

        } while (params.isBlock() && (DateTime.now().getMillis() - startTime.getMillis()) < 15000);

        if (!isOnline) {
            rst.put("pending", "true");
            rst.put("error", "Network is not available");
        }

        return rst;
    }

    public static class Segment {
        private final int typeKey;
        private final byte[] content;
        private final int length;

        private Segment(final int typeKey, final byte[] content, final int length) {
            this.typeKey = typeKey;
            this.content = content;
            this.length = length;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public byte[] getContent() {
            return content;
        }

        public int getLength() {
            return length;
        }
    }

    public static class ParsedLink {
        private final List<Signature> signatures;
        private final List<PublicKey> publicKeys;
        private final List<Segment> segments;
        private final int flag;

        private ParsedLink(final int flag, final List<Segment> segments, final List<Signature> signatures,
                final List<PublicKey> publicKeys) {
            this.flag = flag;
            this.segments = segments;
            this.signatures = signatures;
            this.publicKeys = publicKeys;
        }

        public static boolean isEveriPass(ParsedLink link) {
            return (link.getFlag() & 2) == 2;
        }

        public static boolean isEveriPay(ParsedLink link) {
            return (link.getFlag() & 4) == 4;
        }

        public List<Signature> getSignatures() {
            return signatures;
        }

        public List<PublicKey> getPublicKeys() {
            return publicKeys;
        }

        public List<Segment> getSegments() {
            return segments;
        }

        public int getFlag() {
            return flag;
        }
    }

    public static class EveriPayParam {
        private final int symbol;
        private final String linkId;
        private final long maxAmount;

        public EveriPayParam(final int symbol, @NotNull final String linkId, final long maxAmount) {
            Objects.requireNonNull(linkId);

            if (linkId.length() != 32) {
                throw new EvtLinkException(String.format("LinkId must be with length 32, \"%s\" passed", linkId));
            }

            this.symbol = symbol;
            this.linkId = linkId;
            this.maxAmount = maxAmount;
        }

        public int getSymbol() {
            return symbol;
        }

        public String getLinkId() {
            return linkId;
        }

        public long getMaxAmount() {
            return maxAmount;
        }
    }

    public static class EveriPassParam {
        private final boolean autoDestroy;
        private final String domain;
        private final String token;

        public EveriPassParam(final boolean autoDestroy, final String domain, final String token) {
            Objects.requireNonNull(domain);
            Objects.requireNonNull(token);

            this.autoDestroy = autoDestroy;
            this.domain = domain;
            this.token = token;
        }

        public boolean isAutoDestroy() {
            return autoDestroy;
        }

        public String getDomain() {
            return domain;
        }

        public String getToken() {
            return token;
        }
    }

    public static class EveriLinkPayeeCodeParam {
        private Integer fungibleId;
        private Address address;
        private String amount;

        public EveriLinkPayeeCodeParam(Address address, @Nullable Integer fungibleId, @Nullable String amount) {
            Objects.requireNonNull(address);

            if (amount != null) {
                Objects.requireNonNull(fungibleId);
            }

            this.fungibleId = fungibleId;
            this.address = address;
            this.amount = amount;
        }

        public EveriLinkPayeeCodeParam(Address address) {
            this(address, null, null);
        }

        public Integer getFungibleId() {
            return fungibleId;
        }

        public String getAddress() {
            return address.toString();
        }

        public String getAmount() {
            return amount;
        }
    }

    public static class EveriPassVerificationResult {
        private boolean valid;
        private String domain;
        private String tokenName;

        public EveriPassVerificationResult(boolean valid, String domain, String tokenName) {
            this.valid = valid;
            this.domain = domain;
            this.tokenName = tokenName;
        }

        public boolean isValid() {
            return valid;
        }

        public String getDomain() {
            return domain;
        }

        public String getTokenName() {
            return tokenName;
        }
    }
}
