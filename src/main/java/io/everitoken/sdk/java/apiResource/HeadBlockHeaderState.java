package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;
import okhttp3.Request;

public class HeadBlockHeaderState extends OkhttpApi {
    private static final String uri = "/v1/chain/get_head_block_header_state";
    private static final String method = "GET";

    public HeadBlockHeaderState() {
        super(uri, method, null);
    }

    public HeadBlockHeaderState(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, method, apiRequestConfig);
    }

    @Override
    protected Request buildRequest(RequestParams requestParams) {
        return new Request.Builder().url(getUrl(requestParams.getNetParams())).build();
    }

    public JSONObject request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return  JSONObject.parseObject(res);
    }
}
