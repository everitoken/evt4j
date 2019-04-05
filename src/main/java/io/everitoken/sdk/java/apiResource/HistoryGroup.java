package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import io.everitoken.sdk.java.dto.NameableResource;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class HistoryGroup extends OkhttpApi {
    private static final String uri = "/v1/history/get_groups";

    public HistoryGroup() {
        super(uri);
    }

    public HistoryGroup(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<NameableResource> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray array = new JSONArray(res);
        List<NameableResource> list = new ArrayList<>(array.length());

        for (int i = 0; i < array.length(); i++) {
            list.add(NameableResource.create((String) array.get(i)));
        }

        return list;
    }
}
