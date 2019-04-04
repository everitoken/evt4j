package io.everitoken.sdk.java.apiResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

        return StreamSupport.stream(new JSONArray(res).spliterator(), false)
                .map((name) -> NameableResource.create((String) name)).collect(Collectors.toList());
    }
}
