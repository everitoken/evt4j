package io.everitoken.sdk.java.apiResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class TransactionCommit extends OkhttpApi {
    private static final String uri = "/v1/chain/push_transaction";

    public TransactionCommit() {
        super(uri);
    }

    public TransactionCommit(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public TransactionData request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return TransactionData.ofRaw(new JSONObject(res));
    }
}
