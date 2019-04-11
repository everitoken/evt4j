package io.everitoken.sdk.java.exceptions;

import com.alibaba.fastjson.JSONObject;

public class ApiResponseException extends Exception implements EvtException {
    private JSONObject raw;

    public ApiResponseException(String message, Throwable cause) {
        super(message, cause);

        if (cause instanceof ApiResponseException) {
            this.raw = ((ApiResponseException) cause).getRaw();
        }
    }

    public ApiResponseException(String message, JSONObject raw) {
        super(message);
        this.raw = raw;
    }

    public JSONObject getRaw() {
        return raw;
    }
}
