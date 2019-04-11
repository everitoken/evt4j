package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.Symbol;
import io.everitoken.sdk.java.dto.Permission;

public class NewFungibleAction extends Abi {
    @JSONField(deserialize = false, serialize = false)
    private static final String domain = ".fungible";
    @JSONField(deserialize = false, serialize = false)
    private static final String name = "newfungible";

    private final String fungibleName;
    private final PublicKey creator;
    private final Permission issue;
    private final Permission manage;
    private final Asset totalSupply;
    private final String symbolName;
    private final Symbol symbol;

    private NewFungibleAction(@NotNull Symbol symbol, String fungibleName, PublicKey creator, Permission issue,
            Permission manage, Asset totalSupply) {
        super(name, Integer.toString(symbol.getId()), domain);
        this.symbol = symbol;
        symbolName = Integer.toString(symbol.getId());
        this.creator = creator;
        this.issue = issue;
        this.manage = manage;
        this.totalSupply = totalSupply;
        this.fungibleName = fungibleName;
    }

    @Contract("_, _, _, _, _, _ -> new")
    @NotNull
    public static NewFungibleAction of(Symbol symbol, String fungibleName, String creator, JSONObject issue,
            JSONObject manage, String totalSupply) {
        return new NewFungibleAction(symbol, fungibleName, PublicKey.of(creator), Permission.ofRaw(issue),
                Permission.ofRaw(manage), Asset.parseFromRawBalance(totalSupply));
    }

    public String getCreator() {
        return creator.toString();
    }

    public Permission getIssue() {
        return issue;
    }

    public Permission getManage() {
        return manage;
    }

    @JSONField(name = "total_supply")
    public String getTotalSupply() {
        return totalSupply.toString();
    }

    @JSONField(name = "name")
    public String getFungibleName() {
        return fungibleName;
    }

    @JSONField(name = "sym_name")
    public String getSymbolName() {
        return symbolName;
    }

    @JSONField(name = "sym")
    public String getSymbol() {
        return symbol.toString();
    }
}
