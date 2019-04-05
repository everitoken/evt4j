package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

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
        List<TokenDetailData> list = new ArrayList<>(array.length());

        for (int i = 0; i < array.length(); i++) {
            list.add(TokenDetailData.create((JSONObject) array.get(i)));
        }

        return list;
    }
}
