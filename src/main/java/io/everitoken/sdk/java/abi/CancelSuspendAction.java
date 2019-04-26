package io.everitoken.sdk.java.abi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

public class CancelSuspendAction extends Abi {

    @JSONField(deserialize = false, serialize = false)
    private static final String domain = ".suspend";

    @JSONField(deserialize = false, serialize = false)
    private static final String name = "cancelsuspend";

    private CancelSuspendAction(String proposalName) {
        super(name, proposalName, domain);
    }

    @NotNull
    @Contract("_ -> new")
    public static CancelSuspendAction of(String proposalName) {
        return new CancelSuspendAction(proposalName);
    }

    @Override
    @JSONField(name = "name")
    public String getKey() {
        return super.getKey();
    }

    @Override
    @JSONField(name = "key")
    public String getName() {
        return super.getName();
    }
}
