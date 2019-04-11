package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Utils;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class SignableDigest extends OkhttpApi {
    private static final String uri = "/v1/chain/trx_json_to_digest";

    public SignableDigest() {
        super(uri);
    }

    public SignableDigest(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public byte[] request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return Utils.HEX.decode(JSONObject.parseObject(res).getString("digest"));
    }
}
