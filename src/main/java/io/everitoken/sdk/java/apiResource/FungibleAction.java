package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

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
        JSONArray array = new JSONArray(res);
        List<ActionData> list = new ArrayList<>(array.length());

        for (Object raw : array) {
            list.add(ActionData.create((JSONObject) raw));
        }

        return list;
    }
}
