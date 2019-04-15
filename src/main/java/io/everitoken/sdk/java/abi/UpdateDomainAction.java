package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.everitoken.sdk.java.dto.Permission;

public class UpdateDomainAction extends Abi {
    @JSONField(deserialize = false, serialize = false)
    private static final String key = ".update";
    @JSONField(deserialize = false, serialize = false)
    private static final String name = "updatedomain";
    private final Permission issue;
    private final Permission transfer;
    private final Permission manage;

    public UpdateDomainAction(@NotNull String domain, @Nullable Permission issue, @Nullable Permission transfer,
            @Nullable Permission manage) {
        super(name, key, domain);
        this.issue = issue;
        this.transfer = transfer;
        this.manage = manage;
    }

    public UpdateDomainAction(@NotNull String domain, JSONObject issue, JSONObject transfer, JSONObject manage) {
        this(domain, issue != null ? Permission.ofRaw(issue) : null,
                transfer != null ? Permission.ofRaw(transfer) : null, manage != null ? Permission.ofRaw(manage) : null);
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static UpdateDomainAction ofRaw(@NotNull String domain, @Nullable JSONObject issue,
            @Nullable JSONObject transfer, @Nullable JSONObject manage) {
        return new UpdateDomainAction(domain, issue, transfer, manage);
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
