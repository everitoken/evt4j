package io.everitoken.sdk.java.abi;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Address;

public class IssueTokenAction extends Abi {

    @JSONField(deserialize = false, serialize = false)
    private static final String key = ".issue";

    @JSONField(deserialize = false, serialize = false)
    private static final String name = "issuetoken";

    private final List<String> names;
    private final List<Address> owner;

    private IssueTokenAction(String domain, List<String> names, List<Address> owner) {
        super(name, key, domain);
        this.names = names;
        this.owner = owner;
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static IssueTokenAction of(String domain, List<String> names, List<Address> owner) {
        return new IssueTokenAction(domain, names, owner);
    }

    @Override
    @JSONField(name = "domain")
    public String getDomain() {
        return super.getDomain();
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getOwner() {
        List<String> rtn = new ArrayList<>();

        for (int i = 0; i < owner.size(); i++) {
            rtn.add(owner.get(i).getAddress());
        }

        return rtn;
    }
}
