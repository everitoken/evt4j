package io.everitoken.sdk.java.apiResource;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class SuspendedProposal extends OkhttpApi {
    private static final String uri = "/v1/evt/get_suspend";

    public SuspendedProposal() {
        super(uri);
    }

    public SuspendedProposal(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public String request(RequestParams requestParams) throws ApiResponseException {
        return super.makeRequest(requestParams);
    }
}
