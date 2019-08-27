package io.everitoken.sdk.java.abi;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.annotation.JSONField;

import io.everitoken.sdk.java.Signature;

public class ApproveSuspendAction extends Abi {

    private static final String domain = ".suspend";

    @JSONField(deserialize = false, serialize = false)
    private static final String name = "aprvsuspend";

    private final List<Signature> signatures;

    private ApproveSuspendAction(String proposalName, List<Signature> signatures) {
        super(name, proposalName, domain);
        this.signatures = signatures;
    }

    @NotNull
    @Contract("_, _ -> new")
    public static ApproveSuspendAction of(String proposalName, List<String> signatures) {
        List<Signature> signatureList = new ArrayList<>();
        for (int i = 0; i < signatures.size(); i++) {
            signatureList.add(Signature.of(signatures.get(i)));
        }

        return new ApproveSuspendAction(proposalName, signatureList);
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

    public List<String> getSignatures() {
        List<String> rtn = new ArrayList<>();

        for (int i = 0; i < signatures.size(); i++) {
            rtn.add(signatures.get(i).toString());
        }

        return rtn;
    }
}
