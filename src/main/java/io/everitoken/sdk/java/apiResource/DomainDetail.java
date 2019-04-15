package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.dto.DomainDetailData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class DomainDetail extends OkhttpApi {
    private static final String uri = "/v1/evt/get_domain";

    public DomainDetail() {
        super(uri);
    }

    public DomainDetail(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public DomainDetailData request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return DomainDetailData.of(JSONObject.parseObject(res));
    }
}
