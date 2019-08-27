package io.everitoken.sdk.java.abi;

import java.nio.ByteBuffer;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.Utils;

public class EveriPayAction extends Abi {

    @JSONField(deserialize = false, serialize = false)
    private static final String domain = ".fungible";

    @JSONField(deserialize = false, serialize = false)
    private static final String name = "everipay";

    private final String link;
    private final Asset asset;
    private final Address payee;
    private final String linkId;

    private EveriPayAction(@NotNull String link, @NotNull String symbolId, @NotNull Asset asset, @NotNull Address payee,
            @NotNull String linkId) {
        super(name, symbolId, domain);
        this.asset = asset;
        this.payee = payee;
        this.link = link;
        this.linkId = linkId;
    }

    @Contract("_, _, _ -> new")
    @NotNull
    public static EveriPayAction of(String link, String asset, String payee) {
        EvtLink.ParsedLink parsedLink = EvtLink.parseLink(link, false);

        // sanity control to make sure the link is for everipay
        if (!EvtLink.ParsedLink.isEveriPay(parsedLink)) {
            throw new IllegalArgumentException("Invalid EvtLink: This link is not for everiPay");
        }

        // get symbol from link
        EvtLink.Segment symbolIdSegment = EvtLink.findSegmentByType(parsedLink.getSegments(), 44);

        if (symbolIdSegment == null) {
            throw new IllegalArgumentException("Failed to parse EveriPay link to extract symbolId");
        }

        int symbolId = ByteBuffer.allocate(4).put(symbolIdSegment.getContent()).getInt(0);

        EvtLink.Segment linkId = EvtLink.findSegmentByType(parsedLink.getSegments(), 156);

        if (linkId == null) {
            throw new IllegalArgumentException("Failed to parse EveriPay link to extract linkId");
        }

        return new EveriPayAction(link, Integer.toString(symbolId), Asset.parseFromRawBalance(asset), Address.of(payee),
                Utils.HEX.encode(linkId.getContent()));
    }

    @Override
    @JSONField(deserialize = false, serialize = false)
    public String getDomain() {
        return super.getDomain();
    }

    @Override
    @JSONField(deserialize = false, serialize = false)
    public String getKey() {
        return super.getKey();
    }

    @JSONField(name = "number")
    public String getAsset() {
        return asset.toString();
    }

    public String getLink() {
        return link;
    }

    public String getPayee() {
        return payee.toString();
    }

    @JSONField(deserialize = false, serialize = false)
    public String getLinkId() {
        return linkId;
    }
}
