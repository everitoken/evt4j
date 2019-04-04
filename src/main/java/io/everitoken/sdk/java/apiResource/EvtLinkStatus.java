package io.everitoken.sdk.java.apiResource;

import org.apache.http.conn.HttpHostConnectException;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class EvtLinkStatus extends OkhttpApi {

    public EvtLinkStatus(boolean block) {
        super(block ? "/v1/evt_link/get_trx_id_for_link_id" : "/v1/chain/get_trx_id_for_link_id");
    }

    public EvtLinkStatus(boolean block, @NotNull ApiRequestConfig apiRequestConfig) {
        super(block ? "/v1/evt_link/get_trx_id_for_link_id" : "/v1/chain/get_trx_id_for_link_id", apiRequestConfig);
    }

    public JSONObject request(RequestParams requestParams) throws ApiResponseException, HttpHostConnectException {
        String res = super.makeRequest(requestParams);
        return new JSONObject(res);
    }
}
