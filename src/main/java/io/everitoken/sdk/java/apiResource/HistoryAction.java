package io.everitoken.sdk.java.apiResource;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import io.everitoken.sdk.java.dto.ActionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class HistoryAction extends OkhttpApi {
    private static final String uri = "/v1/history/get_actions";

    public HistoryAction() {
        super(uri);
    }

    public HistoryAction(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, apiRequestConfig);
    }

    public List<ActionData> request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONArray payload = new JSONArray(res);

        return StreamSupport.stream(payload.spliterator(), true).map(raw -> ActionData.create((JSONObject) raw))
                .collect(Collectors.toList());
    }
}
