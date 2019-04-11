package io.everitoken.sdk.java.abi;

import java.util.Objects;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.dto.Permission;

public class NewDomainAction extends Abi {
    @JSONField(deserialize = false, serialize = false)
    private static final String key = ".create";
    @JSONField(deserialize = false, serialize = false)
    private static final String name = "newdomain";
    private final PublicKey creator;
    private final Permission issue;
    private final Permission transfer;
    private final Permission manage;

    protected NewDomainAction(String domain, PublicKey creator, Permission issue, Permission transfer,
            Permission manage) {
        super(name, key, domain);
        this.creator = creator;
        this.issue = issue;
        this.transfer = transfer;
        this.manage = manage;
    }

    protected NewDomainAction(String domain, String creator, JSONObject issue, JSONObject transfer, JSONObject manage) {
        this(domain, PublicKey.of(creator), Permission.ofRaw(issue), Permission.ofRaw(transfer),
                Permission.ofRaw(manage));
    }

    @NotNull
    @Contract("_, _, _, _, _ -> new")
    public static NewDomainAction ofRaw(String domain, String creator, JSONObject issue, JSONObject transfer,
            JSONObject manage) {
        Objects.requireNonNull(issue);
        Objects.requireNonNull(transfer);
        Objects.requireNonNull(manage);

        return new NewDomainAction(domain, creator, issue, transfer, manage);
    }

    public String getCreator() {
        return creator.toString();
    }

    public Permission getIssue() {
        return issue;
    }

    public Permission getTransfer() {
        return transfer;
    }

    public Permission getManage() {
        return manage;
    }

    @Override
    @JSONField(name = "name")
    public String getDomain() {
        return super.getDomain();
    }
}
