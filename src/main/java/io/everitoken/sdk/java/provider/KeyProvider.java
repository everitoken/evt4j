package io.everitoken.sdk.java.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.PrivateKey;

public class KeyProvider implements KeyProviderInterface {
    private final List<PrivateKey> keys;

    private KeyProvider(@NotNull final List<String> keys) {
        List<PrivateKey> keyList = new ArrayList<>();
        for (String key : keys) {
            keyList.add(PrivateKey.of(key));
        }

        this.keys = keyList;
    }

    public static KeyProvider of(String key) {
        return new KeyProvider(Collections.singletonList(key));
    }

    public static KeyProvider of(String[] keys) {
        return new KeyProvider(Arrays.asList(keys));
    }

    public List<PrivateKey> get() {
        return keys;
    }
}
