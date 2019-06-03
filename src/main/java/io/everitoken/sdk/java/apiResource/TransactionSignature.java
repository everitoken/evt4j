package io.everitoken.sdk.java.apiResource;

import org.jetbrains.annotations.NotNull;

import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class TransactionSignature extends OkhttpApi {
    private static final String uri = "/v1/wallet/sign_transaction";

    public TransactionSignature() {
        super(uri);
    }

    public TransactionSignature(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public JSONObject request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return JSONObject.parseObject(res);
    }
}
