package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONArray;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class TransactionDetailsOfPublicKeys extends OkhttpApi {
    private static final String uri = "/v1/history/get_transactions";

    public TransactionDetailsOfPublicKeys() {
        super(uri);
    }

    public TransactionDetailsOfPublicKeys(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public JSONArray request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return JSONArray.parseArray(res);
    }
}
