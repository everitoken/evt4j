package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.Utils;
import io.everitoken.sdk.java.dto.TokenDomain;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class HistoryToken extends OkhttpApi {
    private static final String uri = "/v1/history/get_tokens";

    public HistoryToken() {
        super(uri);
    }

    public HistoryToken(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<TokenDomain> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);

        if (Utils.isJsonEmptyArray(res)) {
            return new ArrayList<>();
        }

        JSONObject payload = JSONObject.parseObject(res);
        System.out.println(payload);

        List<TokenDomain> tokens = new ArrayList<>();

        Set<String> domains = payload.keySet();

        for (String key : domains) {
            JSONArray tokensInDomain = payload.getJSONArray(key);
            tokensInDomain.forEach(tokenInDomain -> tokens.add(new TokenDomain((String) tokenInDomain, key)));
        }

        return tokens;
    }
}
