package io.everitoken.sdk.java.apiResource;

import org.json.JSONObject;

import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.RequestParams;

public class AbiBin extends OkhttpApi {
    private static final String uri = "/v1/chain/abi_json_to_bin";

    public AbiBin() {
        super(uri);
    }

    public JSONObject request(RequestParams requestParams) throws ApiResponseException {
        String res = super.makeRequest(requestParams);
        JSONObject json = new JSONObject(res);

        if (!json.has("binargs")) {
            throw new ApiResponseException("Abi to bin response should have a 'binargs' field", json);
        }

        return json;
    }
}
