package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.dto.Charge;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class TransactionEstimatedCharge extends OkhttpApi {
    private static final String uri = "/v1/chain/get_charge";

    public TransactionEstimatedCharge() {
        super(uri);
    }

    public TransactionEstimatedCharge(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public Charge request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return Charge.ofRaw(JSONObject.parseObject(res));
    }
}
