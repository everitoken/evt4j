package io.everitoken.sdk.java.abi;

import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.exceptions.EvtLinkException;

public class EveriPassAction extends Abi {
    @JSONField(deserialize = false, serialize = false)
    private static final String name = "everipass";

    private final String link;

    private EveriPassAction(@NotNull String link, @NotNull String domain, @NotNull String tokenName) {
        super(name, tokenName, domain);
        this.link = link;
    }

    @Contract("_ -> new")
    @NotNull
    public static EveriPassAction of(String link) {
        EvtLink.ParsedLink parsedLink = EvtLink.parseLink(link, false);

        // sanity control to make sure the link is for everipass
        if (!EvtLink.ParsedLink.isEveriPass(parsedLink)) {
            throw new EvtLinkException("Invalid EvtLink: This link is not for everiPass");
        }

        // get domain
        EvtLink.Segment domainSegment = EvtLink.findSegmentByType(parsedLink.getSegments(), 91);

        if (domainSegment == null) {
            throw new EvtLinkException("Failed to parse EveriPass link to extract \"domain\"");
        }

        // get token name
        EvtLink.Segment tokenSegment = EvtLink.findSegmentByType(parsedLink.getSegments(), 92);

        if (tokenSegment == null) {
            throw new EvtLinkException("Failed to parse EveriPass link to extract \"token name\"");
        }

        String domain = new String(domainSegment.getContent(), StandardCharsets.UTF_8);
        String tokenName = new String(tokenSegment.getContent(), StandardCharsets.UTF_8);

        return new EveriPassAction(link, domain, tokenName);
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

    public String getLink() {
        return link;
    }
}
