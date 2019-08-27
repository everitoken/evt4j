package io.everitoken.sdk.java;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.apiResource.*;
import io.everitoken.sdk.java.dto.*;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.*;

public class Api {
    private final NetParams netParams;
    private ApiRequestConfig apiRequestConfig = new ApiRequestConfig();

    public Api(NetParams netParams, @Nullable ApiRequestConfig apiRequestConfig) {
        this.netParams = netParams;

        if (apiRequestConfig != null) {
            this.apiRequestConfig = apiRequestConfig;
        }
    }

    public Api(NetParams netParams) {
        this(netParams, null);
    }

    public Api() {
        this(new TestNetNetParams(), null);
    }

    public NodeInfo getInfo() throws ApiResponseException {
        return new Info(apiRequestConfig).request(RequestParams.of(netParams));
    }

    public JSONObject getHeadBlockHeaderState() throws ApiResponseException {
        return new HeadBlockHeaderState(apiRequestConfig).request(RequestParams.of(netParams));
    }

    public List<NameableResource> getCreatedDomains(PublicKeysParams publicKeysParams) throws ApiResponseException {
        return new HistoryDomain(apiRequestConfig).request(RequestParams.of(netParams, publicKeysParams));
    }

    public List<TokenDomain> getOwnedTokens(PublicKeysParams publicKeysParams) throws ApiResponseException {
        return new HistoryToken(apiRequestConfig).request(RequestParams.of(netParams, publicKeysParams));
    }

    public List<NameableResource> getManagedGroups(PublicKeysParams publicKeysParams) throws ApiResponseException {
        return new HistoryGroup(apiRequestConfig).request(RequestParams.of(netParams, publicKeysParams));
    }

    public FungibleCreated getCreatedFungibles(PublicKeysParams publicKeysParams) throws ApiResponseException {
        return new HistoryFungible(apiRequestConfig).request(RequestParams.of(netParams, publicKeysParams));
    }

    public List<ActionData> getActions(ActionQueryParams actionQueryParams) throws ApiResponseException {
        return new HistoryAction(apiRequestConfig).request(RequestParams.of(netParams, actionQueryParams));
    }

    public JSONArray getFungibleIds(int take, int skip) throws ApiResponseException {
        return new HistoryFungibleIds(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("take", take);
            body.put("skip", skip);
            return body.toString();
        }));
    }

    public JSONArray getTransactionIdsInBlock(String blockId) throws ApiResponseException {
        return new TransactionIds(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("block_id", blockId);
            return body.toString();
        }));
    }

    public TokenDetailData getToken(String domain, String name) throws ApiResponseException {
        return new TokenDetail(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("domain", domain);
            body.put("name", name);
            return body.toString();
        }));
    }

    public String getSuspendedProposal(String name) throws ApiResponseException {
        return new SuspendedProposal(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("name", name);
            return body.toString();
        }));
    }

    public List<Asset> getFungibleBalance(Address address) throws ApiResponseException {
        return new FungibleBalance(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("addr", address.getAddress());
            return body.toString();
        }));
    }

    public TransactionDetail getTransactionDetailById(TransactionDetailParams transactionDetailParams)
            throws ApiResponseException {
        return new HistoryTransactionDetail(apiRequestConfig)
                .request(RequestParams.of(netParams, transactionDetailParams));
    }

    public DomainDetailData getDomainDetail(String name) throws ApiResponseException {
        return new DomainDetail(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("name", name);
            return body.toString();
        }));
    }

    public List<ActionData> getFungibleActionsByAddress(FungibleActionParams fungibleActionParams)
            throws ApiResponseException {
        return new FungibleAction(apiRequestConfig).request(RequestParams.of(netParams, fungibleActionParams));
    }

    public GroupDetailData getGroupDetail(String name) throws ApiResponseException {
        return new GroupDetail(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("name", name);
            return body.toString();
        }));
    }

    public FungibleDetailData getFungibleSymbolDetail(int id) throws ApiResponseException {
        return new FungibleDetail(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("id", id);
            return body.toString();
        }));
    }

    public List<TokenDetailData> getDomainTokens(String domain, int take, int skip) throws ApiResponseException {
        return new DomainTokens(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("domain", domain);
            body.put("take", take);
            body.put("skip", skip);

            return body.toString();
        }));
    }

    public byte[] getSignableDigest(String data) throws ApiResponseException {
        return new SignableDigest(apiRequestConfig).request(RequestParams.of(netParams, () -> data)).getDigest();
    }

    public JSONArray getSuspendRequiredKeys(String name, List<String> keys) throws ApiResponseException {
        return new RequiredSuspendedKeys(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("name", name);
            body.put("available_keys", keys);
            return body.toString();
        }));
    }

    public JSONArray getTransactionsDetailOfPublicKeys(List<PublicKey> publicKeys, int skip, int take, String direction)
            throws ApiResponseException {

        if (!direction.equals("asc") && !direction.equals("desc")) {
            throw new IllegalArgumentException(
                    String.format("Direct \"%s\" is not supported, only asc and desc are supported", direction));
        }

        List<String> publicKeyList = new ArrayList<>();

        for (int i = 0; i < publicKeys.size(); i++) {
            publicKeyList.add(publicKeys.get(i).toString());
        }

        return new TransactionDetailsOfPublicKeys(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("keys", publicKeyList);
            body.put("skip", skip);
            body.put("take", take);
            body.put("dire", direction);
            return body.toString();
        }));
    }

    private JSONObject getBlockDetailByNumberOrId(String numberOrId) throws ApiResponseException {
        return new BlockDetail(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("block_num_or_id", numberOrId);
            return body.toString();
        }));
    }

    public JSONObject getBlockDetail(long blockNumber) throws ApiResponseException {
        return getBlockDetailByNumberOrId(String.valueOf(blockNumber));
    }

    public JSONObject getBlockDetail(String blockId) throws ApiResponseException {
        return getBlockDetailByNumberOrId(String.valueOf(blockId));
    }
}
