package io.everitoken.sdk.java.apiResource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.NotNull;

import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;
import okhttp3.Request;

public class Info extends OkhttpApi {
    private static final String uri = "/v1/chain/get_info";
    private static final String method = "GET";

    public Info() {
        super(uri, method, null);
    }

    public Info(@NotNull ApiRequestConfig apiRequestConfig) {
        super(uri, method, apiRequestConfig);
    }

    @Override
    protected Request buildRequest(RequestParams requestParams) {
        return new Request.Builder().url(getUrl(requestParams.getNetParams())).build();
    }

    public NodeInfo request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONObject obj = JSON.parseObject(res);

        return new NodeInfo(obj.getString("head_block_producer"), obj.getString("evt_api_version"),
                obj.getIntValue("head_block_num"), obj.getString("chain_id"),
                obj.getString("last_irreversible_block_id"), obj.getString("head_block_time"),
                obj.getIntValue("last_irreversible_block_num"), obj.getString("server_version"),
                obj.getString("head_block_id"), obj.getString("server_version_string"));
    }
}
