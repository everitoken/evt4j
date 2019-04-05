package io.everitoken.sdk.java.apiResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

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
        return new JSONObject(res).getJSONArray("required_keys");
    }
}
