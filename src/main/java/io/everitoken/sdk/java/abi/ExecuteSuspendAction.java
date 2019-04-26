package io.everitoken.sdk.java.abi;

import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.everitoken.sdk.java.PublicKey;

public class ExecuteSuspendAction extends Abi {

    @JSONField(deserialize = false, serialize = false)
    private static final String domain = ".suspend";

    @JSONField(deserialize = false, serialize = false)
    private static final String name = "execsuspend";

    private final PublicKey executor;

    private ExecuteSuspendAction(String proposalName, PublicKey executor) {
        super(name, proposalName, domain);
        this.executor = executor;
    }

    @NotNull
    public static ExecuteSuspendAction of(String proposalName, String executor) {
        return new ExecuteSuspendAction(proposalName, PublicKey.of(executor));
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

    public String getExecutor() {
        return executor.toString();
    }
}
