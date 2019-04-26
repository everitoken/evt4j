package io.everitoken.sdk.java.abi;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.dto.Transaction;

public class NewSuspendAction extends Abi {

    @JSONField(deserialize = false, serialize = false)
    private static final String domain = ".suspend";

    @JSONField(deserialize = false, serialize = false)
    private static final String name = "newsuspend";

    private final PublicKey proposer;
    private final Transaction trx;

    private NewSuspendAction(String proposalName, PublicKey proposer, Transaction trx) {
        super(name, proposalName, domain);
        this.proposer = proposer;
        this.trx = trx;
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static NewSuspendAction of(String proposalName, String proposer, Transaction tx) {
        return new NewSuspendAction(proposalName, PublicKey.of(proposer), tx);
    }

    public Transaction getTrx() {
        return trx;
    }

    @JSONField(name = "key")
    public String getName() {
        return super.getName();
    }

    @JSONField(name = "name")
    public String getKey() {
        return super.getKey();
    }

    public String getProposer() {
        return proposer.toString();
    }
}
