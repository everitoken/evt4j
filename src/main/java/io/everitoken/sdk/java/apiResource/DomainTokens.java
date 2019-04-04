package io.everitoken.sdk.java.apiResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import io.everitoken.sdk.java.dto.TokenDetailData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class DomainTokens extends OkhttpApi {
    private static final String uri = "/v1/evt/get_tokens";

    public DomainTokens() {
        super(uri);
    }

    public DomainTokens(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<TokenDetailData> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray array = new JSONArray(res);

        return StreamSupport.stream(array.spliterator(), true).map(raw -> TokenDetailData.create((JSONObject) raw))
                .collect(Collectors.toList());
    }
}
