package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class SigningRequiredKeys extends OkhttpApi {
    private static final String uri = "/v1/chain/get_required_keys";

    public SigningRequiredKeys() {
        super(uri);
    }

    public SigningRequiredKeys(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<String> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray array = JSONObject.parseObject(res).getJSONArray("required_keys");
        List<String> list = new ArrayList<>(array.size());

        for (int i = 0; i < array.size(); i++) {
            list.add((String) array.get(i));
        }

        return list;
    }
}
