package io.everitoken.sdk.java.apiResource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import okhttp3.*;

class OkhttpApi {
    public static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static final String CUSTOM_REQUEST_HEADER = "X-EVERITOKEN-EVT4J";

    private OkHttpClient client;
    private final String uri;
    private final String method;

    public OkhttpApi(String uri, String method, @Nullable ApiRequestConfig apiRequestConfig) {
        this.uri = uri;
        this.method = method;

        ApiRequestConfig localApiReqConfig = new ApiRequestConfig();

        if (apiRequestConfig != null) {
            localApiReqConfig = apiRequestConfig;
        }

        int timeout = localApiReqConfig.getTimeout();

        this.client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS).readTimeout(timeout, TimeUnit.MILLISECONDS).build();
    }

    protected OkhttpApi(String uri) {
        this(uri, "POST", null);
    }

    protected OkhttpApi(String uri, @NotNull ApiRequestConfig apiRequestConfig) {
        this(uri, "POST", apiRequestConfig);
    }

    protected Request buildRequest(RequestParams requestParams) {
        RequestBody body = RequestBody.create(JSON_TYPE, requestParams.getApiParams().asBody());
        int timeout = requestParams.getNetParams().getNetworkTimeout();
        this.client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS).readTimeout(timeout, TimeUnit.MILLISECONDS).build();

        return new Request.Builder().header(CUSTOM_REQUEST_HEADER, "1.4.6").url(getUrl(requestParams.getNetParams()))
                .post(body).build();
    }

    protected String makeRequest(RequestParams requestParams) throws ApiResponseException {
        Request request = buildRequest(requestParams);

        try {
            Response response = this.client.newCall(request).execute();
            String body = response.body().string();
            checkResponseError(body);
            return body;
        } catch (IOException ex) {
            throw new ApiResponseException(ex.getMessage(), ex);
        }
    }

    @Contract(pure = true)
    private String getUri() {
        return uri;
    }

    @Contract(pure = true)
    private String getMethod() {
        return method;
    }

    public String getUrl(NetParams netParams) {
        return netParams.getEndpointUrl() + getUri();
    }

    private void checkResponseError(@NotNull String body) throws ApiResponseException {
        boolean isArray = false;

        try {
            JSONArray.parseArray(body);
            isArray = true;
        } catch (JSONException ex) {
        }

        if (isArray) {
            return;
        }

        JSONObject res = JSONObject.parseObject(body);

        if (res.containsKey("error")) {
            throw new ApiResponseException(String.format("Response Error for '%s'", uri), res);
        }
    }
}