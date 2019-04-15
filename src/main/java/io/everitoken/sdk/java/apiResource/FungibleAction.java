package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.dto.ActionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class FungibleAction extends OkhttpApi {
    private static final String uri = "/v1/history/get_fungible_actions";

    public FungibleAction() {
        super(uri);
    }

    public FungibleAction(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<ActionData> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray array = JSONArray.parseArray(res);
        List<ActionData> list = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++) {
            list.add(ActionData.create((JSONObject) array.get(i)));
        }

        return list;
    }
}
