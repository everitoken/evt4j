package io.everitoken.sdk.java.param;

public class CustomNetParams extends NetParams {
    public CustomNetParams(String protocol, String host, int port, int networkTimeout) {
        super(protocol, host, port, networkTimeout);
    }
}