package io.everitoken.sdk.java.provider;

import com.alibaba.fastjson.JSON;
import io.everitoken.sdk.java.PrivateKey;
import io.everitoken.sdk.java.Signature;
import io.everitoken.sdk.java.apiResource.SignableDigest;
import io.everitoken.sdk.java.dto.Transaction;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SignProvider implements SignProviderInterface {
    private final KeyProviderInterface keyProvider;

    private SignProvider(final KeyProviderInterface keyProvider) {
        this.keyProvider = keyProvider;
    }

    @NotNull
    @Contract("_ -> new")
    public static SignProvider of(@NotNull final KeyProviderInterface keyProvider) {
        Objects.requireNonNull(keyProvider);
        return new SignProvider(keyProvider);
    }

    public List<Signature> sign(final byte[] bufToSign) {
        Objects.requireNonNull(keyProvider);
        final List<PrivateKey> keys = keyProvider.get();
        return keys.stream()
                .map(privateKey -> Signature.signHash(bufToSign, privateKey))
                .collect(Collectors.toList());
    }

    public static byte[] getSignableDigest(final NetParams netParams, final Transaction tx) throws ApiResponseException {
        return (new SignableDigest()).request(RequestParams.of(netParams, () -> JSON.toJSONString(tx)));
    }
}