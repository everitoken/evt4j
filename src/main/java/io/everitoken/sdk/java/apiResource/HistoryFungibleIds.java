package io.everitoken.sdk.java.apiResource;

import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.JSONArray;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class HistoryFungibleIds extends OkhttpApi {
    private static final String uri = "/v1/history/get_fungible_ids";

    public HistoryFungibleIds() {
        super(uri);
    }

    public HistoryFungibleIds(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public JSONArray request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return JSONArray.parseArray(res);
    }
}
