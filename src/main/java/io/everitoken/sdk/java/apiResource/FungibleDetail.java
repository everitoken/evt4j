package io.everitoken.sdk.java.apiResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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
        return FungibleDetailData.ofRaw(new JSONObject(res));
    }
}
