package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class RequiredSuspendedKeys extends OkhttpApi {
    private static final String uri = "/v1/chain/get_suspend_required_keys";

    public RequiredSuspendedKeys() {
        super(uri);
    }

    public RequiredSuspendedKeys(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public JSONArray request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return JSONObject.parseObject(res).getJSONArray("required_keys");
    }
}
