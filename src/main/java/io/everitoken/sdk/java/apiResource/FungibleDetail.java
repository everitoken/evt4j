package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.dto.FungibleDetailData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class FungibleDetail extends OkhttpApi {
    private static final String uri = "/v1/evt/get_fungible";

    public FungibleDetail() {
        super(uri);
    }

    public FungibleDetail(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public FungibleDetailData request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return FungibleDetailData.ofRaw(JSONObject.parseObject(res));
    }

}
