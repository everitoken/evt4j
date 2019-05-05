package io.everitoken.sdk.java.param;

import javax.annotation.Nullable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class RequestParams {
    private NetParams netParams;
    private ApiParams apiParams;

    public RequestParams(NetParams netParams, @Nullable ApiParams apiParams) {
        this.netParams = netParams;
        this.apiParams = apiParams;
    }

    public RequestParams(NetParams netParams) {
        this(netParams, null);
    }

    @NotNull
    @Contract("_ -> new")
    public static RequestParams of(NetParams netParams) {
        return new RequestParams(netParams, () -> "{}");
    }

    @NotNull
    @Contract("_, _ -> new")
    public static RequestParams of(NetParams netParams, ApiParams apiParams) {
        return new RequestParams(netParams, apiParams);
    }

    public NetParams getNetParams() {
        return netParams;
    }

    public void setNetParams(NetParams netParams) {
        this.netParams = netParams;
    }

    public ApiParams getApiParams() {
        return apiParams;
    }

    public void setApiParams(ApiParams apiParams) {
        this.apiParams = apiParams;
    }
}
