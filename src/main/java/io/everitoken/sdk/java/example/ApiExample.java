package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Api;
import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.apiResource.ApiRequestConfig;
import io.everitoken.sdk.java.dto.*;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.*;

public class ApiExample {
    public static void main(String[] args) {
        try {
            getCustomNetInfo();
            // getBlockDetail();
            // getFungibleIds();
            // replace this with method you want to test
            // getTransactionDetailById("d9c54f23b4358219018d508bb41507a3dc6efb759a519c08e130c434ebf37be4");
            // getSuspendedProposalByName();
            // getGroupDetail();
            // getOwnedTokens();
            // testDomainTokens();
            // getFungibleBalance();
            // getActions();
            // getFungibleSymbolDetail();
            // getFungibleActionsByAddress();
            // getTransactionsDetailOfPublicKeys();
            // getManagedGroups();
            // getCreatedDomain();
            // getCreatedFungibles();
            // NetParams netParams = new TestNetNetParams();
            // NodeInfo state = new Api(netParams).getInfo();
            // System.out.println(JSON.toJSONString(state));
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }

    static void testDomainTokens() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        List<TokenDetailData> domainTokens = new Api(netParams).getDomainTokens("test1126", 10, 0);
        domainTokens.stream()
                .forEach(detail -> System.out.println(String.format("%s: %s", detail.getDomain(), detail.getName())));
    }

    static void getSuspendedProposalByName() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        String suspendedProposal = new Api(netParams).getSuspendedProposal("tp19");
        System.out.println(suspendedProposal);
    }

    static void getFungibleIds() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        JSONArray fungibleIds = new Api(netParams).getFungibleIds(100, 0);
        System.out.println(fungibleIds);
    }

    static void getCreatedDomain() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        final PublicKeysParams publicKeysParams = new PublicKeysParams(
                new String[] { "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND" });

        List<NameableResource> createdDomains = new Api(netParams).getCreatedDomains(publicKeysParams);
        System.out.println(JSON.toJSONString(createdDomains));
    }

    static void getOwnedTokens() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        final PublicKeysParams publicKeysParams = new PublicKeysParams(
                new String[] { "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND" });

        List<TokenDomain> res = new Api(netParams).getOwnedTokens(publicKeysParams);
        System.out.println(JSON.toJSONString(res));
    }

    static void getManagedGroups() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        final PublicKeysParams publicKeysParams = new PublicKeysParams(
                new String[] { "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND" });

        List<NameableResource> res = new Api(netParams).getManagedGroups(publicKeysParams);
        System.out.println(JSON.toJSONString(res));
    }

    static void getCreatedFungibles() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        final PublicKeysParams publicKeysParams = new PublicKeysParams(
                new String[] { "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND" });

        FungibleCreated res = new Api(netParams).getCreatedFungibles(publicKeysParams);
        System.out.println(res);
    }

    static void getActions() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        ActionQueryParams actionParams = new ActionQueryParams("test1123", null,
                new String[] { "issuetoken", "transfer" });

        List<ActionData> res = new Api(netParams).getActions(actionParams);
        System.out.println(JSON.toJSONString(res));
    }

    static void getToken() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        TokenDetailData res = new Api(netParams, ApiRequestConfig.of(10000)).getToken("test1122", "t2");
        System.out.println(res.getName());
        res.getOwner().forEach(publicKey -> System.out.println(publicKey.toString()));
    }

    static void getDomainDetail() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        DomainDetailData res = new Api(netParams).getDomainDetail("test1123");
        System.out.println(JSON.toJSONString(res));
        System.out.println(res.getTransfer().getAuthorizers().get(0).getRef());
    }

    static void getGroupDetail() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        GroupDetailData res = new Api(netParams).getGroupDetail("feitestgroup2");
        System.out.println(res.getRoot());
    }

    static void getFungibleBalance() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        List<Asset> res = new Api(netParams)
                .getFungibleBalance(Address.of("EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H"));

        res.forEach(asset -> System.out.println(asset.toString()));
    }

    static void getTransactionDetailById(String trxId) throws ApiResponseException {
        // 93e0aa6bed4b2b768ce4617cc2cb66319aaef87bdc413cbb7148cc4690bc799f
        NetParams netParams = new TestNetNetParams();
        TransactionDetailParams transactionDetailParams = new TransactionDetailParams(trxId);
        TransactionDetail res = new Api(netParams).getTransactionDetailById(transactionDetailParams);
        System.out.println(JSON.toJSONString(res));
    }

    static void getFungibleSymbolDetail() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        FungibleDetailData res = new Api(netParams).getFungibleSymbolDetail(20);
        System.out.println(JSON.toJSONString(res));
    }

    static void getTransactionsDetailOfPublicKeys() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        JSONArray res = new Api(netParams).getTransactionsDetailOfPublicKeys(
                Arrays.asList(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")), 0, 2, "asc");
        System.out.println(res);
    }

    static void getBlockDetail() throws ApiResponseException {
        NetParams netParams = new MainNetNetParams(NetParams.NET.MAINNET1);
        JSONObject res = new Api(netParams)
                .getBlockDetail("030f5454f21f319936edb0270d06ca575060c83dbdddc14befc34fb448f8d8b9");
        System.out.println(res);
    }

    static void getFungibleActionsByAddress() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        FungibleActionParams params = FungibleActionParams.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                "20");

        List<ActionData> res = new Api(netParams).getFungibleActionsByAddress(params);
        System.out.println(JSON.toJSONString(res));
    }

    static void getCustomNetInfo() throws ApiResponseException {
        NetParams netParams = new CustomNetParams("http", "127.0.0.1", 8888, 15000);
        Api api = new Api(netParams);
        System.out.println(JSON.toJSONString(api.getInfo()));
    }
}
