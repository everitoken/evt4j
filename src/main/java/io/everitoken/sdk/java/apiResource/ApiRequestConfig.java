package io.everitoken.sdk.java.apiResource;

public class ApiRequestConfig {
    public static int DEFAULT_TIMEOUT = 15 * 1000;
    private int timeout;

    public ApiRequestConfig(int timeout) {
        this.timeout = timeout;
    }

    public ApiRequestConfig() {
        this(DEFAULT_TIMEOUT);
    }

    public static ApiRequestConfig of(int timeout) {
        return new ApiRequestConfig(timeout);
    }

    public int getTimeout() {
        return timeout;
    }
}