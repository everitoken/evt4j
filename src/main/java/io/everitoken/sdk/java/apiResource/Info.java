package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSON;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;
import okhttp3.Request;

public class Info extends OkhttpApi {
    private static final String uri = "/v1/chain/get_info";
    private static final String method = "GET";

    public Info() {
        super(uri, method, null);
    }

    public Info(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, method, apiRequestConfig);
    }

    @Override
    protected Request buildRequest(RequestParams requestParams) {
        return new Request.Builder().url(getUrl(requestParams.getNetParams())).build();
    }

    public NodeInfo request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return JSON.parseObject(res, NodeInfo.class);
    }
}
