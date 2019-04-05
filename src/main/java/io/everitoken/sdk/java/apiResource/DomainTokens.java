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

    private ArrayList<Object> toCollection(JSONArray array) {
        ArrayList<Object> local = new ArrayList<>(array.length());
        for (Object obj : array) {
            local.add(obj);
        }
        return local;
    }

    public List<TokenDetailData> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray array = new JSONArray(res);
        List<TokenDetailData> list = new ArrayList<>(array.length());

        for (Object raw : array) {
            list.add(TokenDetailData.create((JSONObject) raw));
        }

        return list;
    }
}
