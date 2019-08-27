package io.everitoken.sdk.java.abi;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Address;

public class TransferAction extends Abi {

    @JSONField(deserialize = false, serialize = false)
    private static final String name = "transfer";

    private final List<Address> to;
    private final String memo;

    private TransferAction(String domain, String tokenName, List<Address> to, String memo) {
        super(name, tokenName, domain);
        this.memo = memo;
        this.to = to;
    }

    @NotNull
    @Contract("_, _, _, _ -> new")
    public static TransferAction of(String domain, String tokenName, List<String> to, String memo) {
        List<Address> addresses = new ArrayList<>();

        for (int i = 0; i < to.size(); i++) {
            addresses.add(Address.of(to.get(i)));
        }

        return new TransferAction(domain, tokenName, addresses, memo);
    }

    @Override
    @JSONField(name = "name")
    public String getKey() {
        return super.getKey();
    }

    public String getMemo() {
        return memo;
    }

    public List<String> getTo() {
        List<String> rtn = new ArrayList<>();

        for (int i = 0; i < to.size(); i++) {
            rtn.add(to.get(i).getAddress());
        }

        return rtn;
    }
}
