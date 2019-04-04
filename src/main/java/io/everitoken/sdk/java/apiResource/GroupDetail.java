package io.everitoken.sdk.java.apiResource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import io.everitoken.sdk.java.dto.GroupDetailData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class GroupDetail extends OkhttpApi {
    private static final String uri = "/v1/evt/get_group";

    public GroupDetail() {
        super(uri);
    }

    public GroupDetail(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public GroupDetailData request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        return GroupDetailData.create(new JSONObject(res));
    }
}
