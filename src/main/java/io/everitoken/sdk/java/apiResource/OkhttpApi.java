package io.everitoken.sdk.java.apiResource;

import java.io.IOException;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.RequestParams;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class OkhttpApi {
    public static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final String uri;
    private final String method;

    public OkhttpApi(String uri, String method, @Nullable ApiRequestConfig apiRequestConfig) {
        this.uri = uri;
        this.method = method;
        this.client = new OkHttpClient();

        ApiRequestConfig localApiReqConfig = new ApiRequestConfig();

        if (apiRequestConfig != null) {
            localApiReqConfig = apiRequestConfig;
        }

        // TODO add timeout support
        int timeout = localApiReqConfig.getTimeout();
    }

    protected OkhttpApi(String uri) {
        this(uri, "POST", null);
    }

    protected OkhttpApi(String uri, @NotNull ApiRequestConfig apiRequestConfig) {
        this(uri, "POST", apiRequestConfig);
    }

    protected Request buildRequest(RequestParams requestParams) {
        RequestBody body = RequestBody.create(JSON_TYPE, requestParams.getApiParams().asBody());

        return new Request.Builder().url(getUrl(requestParams.getNetParams())).post(body).build();
    }

    protected String makeRequest(RequestParams requestParams) throws ApiResponseException {
        Request request = buildRequest(requestParams);

        Response response;
        try {
            response = this.client.newCall(request).execute();
            return response.body().string();
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

    public static void main(String[] args) {
    }

}