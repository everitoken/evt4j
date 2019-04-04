package io.everitoken.sdk.java.apiResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import io.everitoken.sdk.java.dto.FungibleCreated;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class HistoryFungible extends OkhttpApi {
    private static final String uri = "/v1/history/get_fungibles";

    public HistoryFungible() {
        super(uri);
    }

    public HistoryFungible(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public FungibleCreated request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);

        List<Integer> ids = StreamSupport.stream(new JSONArray(res).spliterator(), true).map(id -> (int) id)
                .collect(Collectors.toList());

        return new FungibleCreated(ids);
    }
}
