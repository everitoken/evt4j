package io.everitoken.sdk.java.param;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.PublicKey;

public class PublicKeysParams implements ApiParams {
    private List<PublicKey> publicKeys = new ArrayList<>();

    public PublicKeysParams(@NotNull String[] publicKeys) {
        for (String publicKey : publicKeys) {
            this.publicKeys.add(PublicKey.of(publicKey));
        }
    }

    public PublicKeysParams(List<PublicKey> publicKeys) {
        this.publicKeys = publicKeys;
    }

    public static PublicKeysParams of(List<PublicKey> publicKeys) {
        return new PublicKeysParams(publicKeys);
    }

    @JSONField(name = "keys")
    public List<String> getPublicKeys() {
        List<String> rtn = new ArrayList<>();

        for (int i = 0; i < publicKeys.size(); i++) {
            rtn.add(publicKeys.get(i).toString());
        }

        return rtn;
    }

    @Override
    public String asBody() {
        return JSON.toJSONString(this);
    }
}
