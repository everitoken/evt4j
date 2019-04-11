package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import org.jetbrains.annotations.NotNull;

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
        JSONArray array =  JSONArray.parseArray(res);
        List<Integer> ids = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++) {
            ids.add((int) array.get(i));
        }

        return new FungibleCreated(ids);
    }
}
