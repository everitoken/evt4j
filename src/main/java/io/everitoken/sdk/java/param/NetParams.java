package io.everitoken.sdk.java.param;

import org.jetbrains.annotations.Contract;

public abstract class NetParams {
    private final String protocol;
    private final String host;
    private final int port;
    private final int networkTimeout; // millisecond

    protected NetParams(String protocol, String host, int port, int networkTimeout) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.networkTimeout = networkTimeout;

    }

    public String getEndpointUrl() {
        return String.format("%s://%s:%s", getProtocol(), getHost(), getPort());
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getNetworkTimeout() {
        return networkTimeout;
    }

    public static enum NET {
        MAINNET1("mainnet1.everitoken.io"), MAINNET2("mainnet2.everitoken.io"), MAINNET3("mainnet3.everitoken.io"),
        MAINNET4("mainnet4.everitoken.io"), MAINNET5("mainnet5.everitoken.io"), MAINNET6("mainnet6.everitoken.io"),
        MAINNET7("mainnet7.everitoken.io"), MAINNET8("mainnet8.everitoken.io"), MAINNET9("mainnet9.everitoken.io"),
        MAINNET10("mainnet10.everitoken.io"), MAINNET11("mainnet11.everitoken.io"),
        MAINNET12("mainnet12.everitoken.io"), MAINNET13("mainnet13.everitoken.io"), MAINNET14("mainnet14.evtnd.com"),
        MAINNET15("mainnet15.everitoken.io"), TESTNET("testnet1.everitoken.io");

        private final String url;

        NET(String url) {
            this.url = url;
        }

        @Contract(pure = true)
        public String getUrl() {
            return url;
        }
    }
}
