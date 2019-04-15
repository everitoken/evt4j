package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class FungibleBalance extends OkhttpApi {
    private static final String uri = "/v1/history/get_fungibles_balance";

    public FungibleBalance() {
        super(uri);
    }

    public FungibleBalance(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<Asset> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray array = JSONArray.parseArray(res);
        List<Asset> list = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++) {
            list.add(Asset.parseFromRawBalance((String) array.get(i)));
        }

        return list;
    }
}
