package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.everitoken.sdk.java.Symbol;
import io.everitoken.sdk.java.dto.Permission;

public class UpdateFungibleAction extends Abi {
    @JSONField(deserialize = false, serialize = false)
    private static final String domain = ".fungible";
    @JSONField(deserialize = false, serialize = false)
    private static final String name = "updfungible";

    private final Permission issue;
    private final Permission manage;
    private final int symbolName;

    private UpdateFungibleAction(@NotNull Symbol symbol, @Nullable Permission issue, @Nullable Permission manage) {
        super(name, Integer.toString(symbol.getId()), domain);
        symbolName = symbol.getId();
        this.issue = issue;
        this.manage = manage;
    }

    @Contract("_, _, _ -> new")
    @NotNull
    public static UpdateFungibleAction of(Symbol symbol, @Nullable JSONObject issue, @Nullable JSONObject manage) {
        return new UpdateFungibleAction(symbol, issue != null ? Permission.ofRaw(issue) : null,
                manage != null ? Permission.ofRaw(manage) : null);
    }

    public Permission getIssue() {
        return issue;
    }

    public Permission getManage() {
        return manage;
    }

    @JSONField(name = "sym_id")
    public int getSymbolName() {
        return symbolName;
    }
}
