package io.everitoken.sdk.java.apiResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class FungibleBalance extends OkhttpApi {
    private static final String uri = "/v1/history/get_fungibles_balance";

    public FungibleBalance() {
        super(uri);
    }

    public FungibleBalance(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<Asset> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);

        return StreamSupport.stream(new JSONArray(res).spliterator(), true)
                .map(balance -> Asset.parseFromRawBalance((String) balance)).collect(Collectors.toList());
    }
}
