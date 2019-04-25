package io.everitoken.sdk.java;

import java.util.List;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.jetbrains.annotations.Nullable;

import io.everitoken.sdk.java.apiResource.ApiRequestConfig;
import io.everitoken.sdk.java.apiResource.DomainDetail;
import io.everitoken.sdk.java.apiResource.DomainTokens;
import io.everitoken.sdk.java.apiResource.FungibleAction;
import io.everitoken.sdk.java.apiResource.FungibleBalance;
import io.everitoken.sdk.java.apiResource.FungibleDetail;
import io.everitoken.sdk.java.apiResource.GroupDetail;
import io.everitoken.sdk.java.apiResource.HeadBlockHeaderState;
import io.everitoken.sdk.java.apiResource.HistoryAction;
import io.everitoken.sdk.java.apiResource.HistoryDomain;
import io.everitoken.sdk.java.apiResource.HistoryFungible;
import io.everitoken.sdk.java.apiResource.HistoryGroup;
import io.everitoken.sdk.java.apiResource.HistoryToken;
import io.everitoken.sdk.java.apiResource.HistoryTransactionDetail;
import io.everitoken.sdk.java.apiResource.Info;
import io.everitoken.sdk.java.apiResource.RequiredSuspendedKeys;
import io.everitoken.sdk.java.apiResource.SignableDigest;
import io.everitoken.sdk.java.apiResource.SuspendedProposal;
import io.everitoken.sdk.java.apiResource.TokenDetail;
import io.everitoken.sdk.java.apiResource.TransactionDetailsOfPublicKeys;
import io.everitoken.sdk.java.apiResource.TransactionIds;
import io.everitoken.sdk.java.dto.ActionData;
import io.everitoken.sdk.java.dto.DomainDetailData;
import io.everitoken.sdk.java.dto.FungibleCreated;
import io.everitoken.sdk.java.dto.FungibleDetailData;
import io.everitoken.sdk.java.dto.GroupDetailData;
import io.everitoken.sdk.java.dto.NameableResource;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.TokenDetailData;
import io.everitoken.sdk.java.dto.TokenDomain;
import io.everitoken.sdk.java.dto.TransactionDetail;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.ActionQueryParams;
import io.everitoken.sdk.java.param.FungibleActionParams;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.PublicKeysParams;
import io.everitoken.sdk.java.param.RequestParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.param.TransactionDetailParams;

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

        return new TransactionDetailsOfPublicKeys(apiRequestConfig).request(RequestParams.of(netParams, () -> {
            JSONObject body = new JSONObject();
            body.put("keys", publicKeys.stream().map(PublicKey::toString).collect(Collectors.toList()));
            body.put("skip", skip);
            body.put("take", take);
            body.put("dire", direction);
            return body.toString();
        }));
    }
}
