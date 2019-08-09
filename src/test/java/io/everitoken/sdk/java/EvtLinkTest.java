package io.everitoken.sdk.java;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.param.MainNetNetParams;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;

class EvtLinkTest {
    static void createSegment40To90() {
        final byte[] content = ByteBuffer.allocate(4).putInt(100000).array();
        Assertions.assertEquals("2a000186a0", Utils.HEX.encode(EvtLink.createSegment(42, content)));
    }

    @Test
    void createSegment20() {
        final byte[] content = ByteBuffer.allocate(1).put((byte) 256).array();
        Assertions.assertEquals("1400", Utils.HEX.encode(EvtLink.createSegment(20, content)));

        final byte[] c = new byte[1];
        c[0] = (byte) 255;
        Assertions.assertEquals("14ff", Utils.HEX.encode(EvtLink.createSegment(20, c)));
    }

    @Test
    void createSegment90() {
        Assertions.assertEquals("5b0a68656c6c6f776f726c64",
                Utils.HEX.encode(EvtLink.createSegment(91, "helloworld".getBytes())));
    }

    @Test
    void createSegment90ExceedLimit() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EvtLink.createSegment(98,
                    "helloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworldhelloworld"
                            .getBytes());
        });
    }

    @Test
    void parseSegment20() {
        final byte[] bs = Utils.HEX.decode("142a");
        final EvtLink.Segment segment = EvtLink.parseSegment(bs, 0);
        Assertions.assertEquals(42, segment.getContent()[0]);
        Assertions.assertEquals(2, segment.getLength());
    }

    @Test
    void parseSegment40To90() {
        final byte[] bs = Utils.HEX.decode("2a000186a0");
        final EvtLink.Segment segment = EvtLink.parseSegment(bs, 0);
        Assertions.assertEquals(100000, ByteBuffer.wrap(segment.getContent()).getInt());
    }

    @Test
    void parseString() {
        final byte[] bs = Utils.HEX.decode("5b0a68656c6c6f776f726c64");
        final EvtLink.Segment segment = EvtLink.parseSegment(bs, 0);
        Assertions.assertEquals("helloworld", new String(segment.getContent(), StandardCharsets.UTF_8));
    }

    @Test
    void parseUuid() {
        final byte[] bs = EvtLink.createSegment(156, Utils.HEX.decode("753b0605fbe19dd39460590ce867999c"));
        final EvtLink.Segment segment = EvtLink.parseSegment(bs, 0);
        Assertions.assertEquals(156, segment.getTypeKey());

        Assertions.assertEquals("753b0605fbe19dd39460590ce867999c", Utils.HEX.encode(segment.getContent()));
        Assertions.assertEquals(17, segment.getLength());
    }

    @Test
    void decode() {
        final String input = "FXXJORONB8B58VU9Z2MZKZ5*:NP3::K7UYKD:Y9I1V508HBQZK2AE*ZS85PJZ2N47/41LQ-MZ/4Q6THOX"
                + "**YN0VMQ*3"
                + "/CG9-KX2:E7C-OCM*KJJT:Z7640Q6B*FWIQBYMDPIXB4CM:-8*TW-QNY$$AY5$UA3+N-7L/ZSDCWO1I7M*3Q6*SMAYOWWTF5RJAJ"
                + ":NG**8U5J6WC2VM5Z:OLZPVJXX*12I*6V9FL1HX095$5:$*C3KGCM3FIS-WWRE14E:7VYNFA-3QCH5ULZJ*CRH91BTXIK-N+J1";

        Assertions.assertEquals(
                "1f55bba92913fd086a2bc4e498a07c57534f70ce836d9a2592824a9cfac1503bcffd0cd068577fddf85ef64ce5420742a1b8a97ee10b05925f2091a7d181847fa74b685a6dc95d82532d77a8b1940ed4078a9ecb796d0c22f4ce6a0641c342b4c094b6d4618e52c69103f905cebf397026861b5949632d26d923268fd9321da4937f60d48e63c2d3b966d36f603739e6b2c8ae7f70e62466a027f61daae470476038d43859ef811f5c6e6a57d00f1602b9001546208f98527edd65c96e47656547114b",
                Utils.HEX.encode(EvtLink.decode(input)));
    }

    @Test
    void parseLink() {
        final String link = "https://evt.li/" + "0DFYZXZO9-:Y:JLF*3/4JCPG7V1346OZ:R/G2M93-2L*BBT9S0YQ0+JNRIW95*HF"
                + "*94J0OVUN$KS01-GZ"
                + "-N7FWK9_FXXJORONB7B58VU9Z2MZKZ5*:NP3::K7UYKD:Y9I1V508HBQZK2AE*ZS85PJZ2N47/41LQ-MZ"
                + "/4Q6THOX**YN0VMQ*3/CG9-KX2:E7C-OCM*KJJT:Z7640Q6B*FWIQBYMDPIXB4CM:-8*TW-QNY$$AY5$UA3"
                + "+N-7L/ZSDCWO1I7M*3Q6*SMAYOWWTF5RJAJ:NG**8U5J6WC2VM5Z:OLZPVJXX*12I*6V9FL1HX095$5:$"
                + "*C3KGCM3FIS-WWRE14E:7VYNFA-3QCH5ULZJ*CRH91BTXIK-N+J1";

        final EvtLink.ParsedLink parsed = EvtLink.parseLink(link, true);
        final List<Signature> signatures = parsed.getSignatures();
        final List<PublicKey> publicKeys = parsed.getPublicKeys();
        final List<EvtLink.Segment> segments = parsed.getSegments();

        // segment 1
        final EvtLink.Segment segment1 = segments.get(0);
        Assertions.assertEquals(42, segment1.getTypeKey());
        Assertions.assertEquals(1532709368, ByteBuffer.wrap(segment1.getContent()).getInt());

        // segment 2
        final EvtLink.Segment segment2 = segments.get(1);
        Assertions.assertEquals(91, segment2.getTypeKey());
        Assertions.assertEquals("nd1532709365718", new String(segment2.getContent(), StandardCharsets.UTF_8));

        // segment 3
        final EvtLink.Segment segment3 = segments.get(2);
        Assertions.assertEquals(92, segment3.getTypeKey());
        Assertions.assertEquals("tk3065418732.2981", new String(segment3.getContent(), StandardCharsets.UTF_8));

        // segment 3
        final EvtLink.Segment segment4 = segments.get(3);
        Assertions.assertEquals(156, segment4.getTypeKey());
        Assertions.assertEquals("8b5a5a5bf96abebf3f8f7184f522a1b9", Utils.HEX.encode(segment4.getContent()));

        Assertions.assertEquals(
                "SIG_K1_K6UKhSMgMdZkm1M6JUNaK6XBGgvpVWuexhUzrg9ARgJCsWiN2A5PeH9K9YUpuE8ZArYXvSWMwBSEVh8dFhHPriQh6raEVc",
                signatures.get(0).toString());
        Assertions.assertEquals(
                "SIG_K1_KfdYEC6GnvgkrDPLPN4tFsTACc4nnpEopBdwBsg9fwzG8zu489hCma5gYeW3zsvabbCfMQL4vu9QVbyTHHDLjp43NCNFtD",
                signatures.get(1).toString());
        Assertions.assertEquals(
                "SIG_K1_K3CZKdq28aNkGwU9bL57aW45kvWj3CagGgarShLYFg8MVoTTHRbXZwPvyfBf9WN93VGXBPDLdFMmtbKA814XVvQ3QZRVJn",
                signatures.get(2).toString());

        Assertions.assertEquals("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND", publicKeys.get(0).toString());
        Assertions.assertEquals("EVT6MYSkiBHNDLxE6JfTmSA1FxwZCgBnBYvCo7snSQEQ2ySBtpC6s", publicKeys.get(1).toString());
        Assertions.assertEquals("EVT7bUYEdpHiKcKT9Yi794MiwKzx5tGY3cHSh4DoCrL4B2LRjRgnt", publicKeys.get(2).toString());
    }

    @Test
    void testAppendSignature() {
        String sig = "SIG_K1_KfdgiuhCZFSx9ggL4sNCoKnPzQwXEq1AJxEdd9Jw27GbuZ5ieoYMdh76FKpFEoxa8jVkFYMafyorxFHSutrgmFy8VbwCfD";
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EvtLink.appendSignautresToEvtLink("link_sig", Arrays.asList(Signature.of(sig)));
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EvtLink.appendSignautresToEvtLink("", Arrays.asList(Signature.of(sig)));
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            EvtLink.appendSignautresToEvtLink("link", new ArrayList<>());
        });

        String content = EvtLink.appendSignautresToEvtLink("link", Arrays.asList(Signature.of(sig)));
        Assertions.assertEquals(
                "link_QXXYQY$HXP+JWF8K32YW-EZPRCVMD+X6GO2*I0QQOCB*JK77O/ZDSPV2Y71APKN$0M159OCFD1741NQF4-E3:DY01+5-E2V1",
                content);
    }

    @Test
    void checkMaxAmountOfEveryPay() {
        final long maxAmount = 4294967294L;
        final MainNetNetParams netParams = new MainNetNetParams(NetParams.NET.MAINNET1);
        final EvtLink evtLink = new EvtLink(netParams);
        final EvtLink.EveriPayParam everiPayParam1 = new EvtLink.EveriPayParam(1, EvtLink.getUniqueLinkId(), maxAmount);
        final String payText1 = evtLink.getEvtLinkForEveriPay(everiPayParam1, null);

        final EvtLink.ParsedLink parsedLink = EvtLink.parseLink(payText1, false);
        final EvtLink.Segment segment = parsedLink.getSegments().stream().filter(s -> s.getTypeKey() == 43).findFirst()
                .orElse(null);

        if (segment != null) {
            Assertions.assertEquals(43, segment.getTypeKey());
            Assertions.assertEquals(maxAmount, EvtLink.getUnsignedInt(segment.getContent()));
        }

        final long maxAmount2 = 4_294_967_299L;
        final EvtLink.EveriPayParam everiPayParam2 = new EvtLink.EveriPayParam(1, EvtLink.getUniqueLinkId(),
                4_294_967_299L);
        final String payText2 = evtLink.getEvtLinkForEveriPay(everiPayParam2, null);

        final EvtLink.ParsedLink parsedLink2 = EvtLink.parseLink(payText2, false);
        final EvtLink.Segment segment2 = parsedLink2.getSegments().stream().filter(s -> s.getTypeKey() == 94)
                .findFirst().orElse(null);

        if (segment2 != null) {
            Assertions.assertEquals(94, segment2.getTypeKey());
            Assertions.assertEquals(Long.toString(maxAmount2),
                    new String(segment2.getContent(), StandardCharsets.UTF_8));
        }
    }

    @Test
    void getUnsignedInt() {
        final String link = "YH*T1M7";
        final EvtLink.Segment segment = EvtLink.parseSegment(EvtLink.decode(link), 0);
        Assertions.assertEquals(4294967295L, EvtLink.getUnsignedInt(segment.getContent()));
    }

    @Test
    void everiLinkPayeeCodeParam() {
        Address address = Address.of("EVT00000000000000000000000000000000000000000000000000");
        Assertions.assertThrows(NullPointerException.class, () -> {
            new EvtLink.EveriLinkPayeeCodeParam(address, null, "1.00000");
        });

        EvtLink.EveriLinkPayeeCodeParam param = new EvtLink.EveriLinkPayeeCodeParam(address);
        Assertions.assertNull(param.getFungibleId());
        Assertions.assertNull(param.getAmount());
        Assertions.assertEquals(address.toString(), param.getAddress());
    }

    @Test
    void parseEvtLinkForPayeeCodeOnlyWithAddress() {
        // here is encoded with only the address
        // "EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa"
        String qrText = "https://evt.li/03$5CLY539FEQR3NBG*NQ4W70P7W0EU$0$GAM5:1GNK-E5A0L+++5JAU*4PEA64ZTRDGOO"
                + "/7LGLW2C6JI289";

        EvtLink.ParsedLink parsedLink = EvtLink.parseLink(qrText, false);
        Assertions.assertEquals(1, parsedLink.getSegments().size());
        Assertions.assertEquals("EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa",
                new String(parsedLink.getSegments().get(0).getContent(), StandardCharsets.UTF_8));
    }

    @Test
    void verifyLinkForPayeeCode() {
        // here is encoded with the address
        // "EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa", fungibleId
        // and amount
        String qrText = "https://evt.li/01P-:EM95I7F1W9*5BZZ049FS*3SW3F4K-:1UM**KL98VK*XHBW2VIZYZAAG3FO-JIGQ0A9KB2Z5"
                + "/QT2UKPP14BGCN7ZXLXG21A5JTJ7C";

        EvtLink.ParsedLink parsedLink = EvtLink.parseLink(qrText, false);
        Assertions.assertEquals(3, parsedLink.getSegments().size());

        Assertions.assertEquals(1, ByteBuffer.allocate(4).put(parsedLink.getSegments().get(0).getContent()).getInt(0));
        Assertions.assertEquals("EVT76uLwUD5t6fkob9Rbc9UxHgdTVshNceyv2hmppw4d82j2zYRpa",
                new String(parsedLink.getSegments().get(1).getContent(), StandardCharsets.UTF_8));

        Assertions.assertEquals("1.23456",
                new String(parsedLink.getSegments().get(2).getContent(), StandardCharsets.UTF_8));
    }

    @Test
    void getEvtLinkForPayeeCodeWithOnlyAddress() {
        NetParams netParams = new TestNetNetParams();
        EvtLink evtlink = new EvtLink(netParams);

        Address address = Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND");
        EvtLink.EveriLinkPayeeCodeParam params = new EvtLink.EveriLinkPayeeCodeParam(address);
        assertEquals(
                "https://evt.li/03$5CLY539F2O48KKZUT6FAUU8ND5M03-M*+2N5LK3O2-3/PB+SD$O7CI-UY64/IS7Z73*SG+O8PAT-DB1V2",
                evtlink.getEvtLinkForPayeeCode(params));
    }

    @Test
    void getEvtLinkForPayeeCode() {
        NetParams netParams = new TestNetNetParams();
        EvtLink evtlink = new EvtLink(netParams);

        Address address = Address.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND");
        EvtLink.EveriLinkPayeeCodeParam params = new EvtLink.EveriLinkPayeeCodeParam(address, 1, "100");
        assertEquals(
                "https://evt.li/022Y5C1$TY/8T8O6QT:V:C+H$HHLWK7XC4FEJ-J8JXWNB1VBAP0BIM+DZYRIJCEK0RH*K+HOJA2$UF:U3AOK-V7CF6REB4QSZ*A",
                evtlink.getEvtLinkForPayeeCode(params));
    }

    @Test
    void getEvtLinkForEveriPassWithMemo() {
        NetParams netParams = new TestNetNetParams();
        EvtLink evtlink = new EvtLink(netParams);
        EvtLink.EveriPassParam param = new EvtLink.EveriPassParam(true, "domain", "token", "memo");

        String link = evtlink.getEvtLinkForEveriPass(param,
                SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

        EvtLink.ParsedLink parsedLink = EvtLink.parseLink(link, false);
        Assertions.assertTrue(EvtLink.ParsedLink.isEveriPass(parsedLink));
        Assertions.assertEquals("domain",
                new String(parsedLink.getSegments().get(1).getContent(), StandardCharsets.UTF_8));
        Assertions.assertEquals("token",
                new String(parsedLink.getSegments().get(2).getContent(), StandardCharsets.UTF_8));
        Assertions.assertEquals("memo",
                new String(parsedLink.getSegments().get(3).getContent(), StandardCharsets.UTF_8));
    }

}
