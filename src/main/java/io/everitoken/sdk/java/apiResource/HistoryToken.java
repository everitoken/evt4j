package io.everitoken.sdk.java.apiResource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

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

        JSONObject payload = new JSONObject(res);

        List<TokenDomain> tokens = new ArrayList<>();

        Iterator<String> domains = payload.keys();

        while (domains.hasNext()) {
            String domainName = domains.next();
            JSONArray tokensInDomain = payload.getJSONArray(domainName);
            tokensInDomain.forEach(tokenInDomain -> tokens.add(new TokenDomain((String) tokenInDomain, domainName)));
        }

        return tokens;
    }
}
