package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.dto.NameableResource;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class HistoryDomain extends OkhttpApi {
    private static final String uri = "/v1/history/get_domains";

    public HistoryDomain() {
        super(uri);
    }

    public HistoryDomain(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<NameableResource> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray array =  JSONArray.parseArray(res);
        List<NameableResource> list = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++) {
            list.add(NameableResource.create((String) array.get(i)));
        }

        return list;
    }
}
