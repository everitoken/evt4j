package io.everitoken.sdk.java.example;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Api;
import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.apiResource.ApiRequestConfig;
import io.everitoken.sdk.java.dto.ActionData;
import io.everitoken.sdk.java.dto.DomainDetailData;
import io.everitoken.sdk.java.dto.FungibleCreated;
import io.everitoken.sdk.java.dto.FungibleDetailData;
import io.everitoken.sdk.java.dto.GroupDetailData;
import io.everitoken.sdk.java.dto.NameableResource;
import io.everitoken.sdk.java.dto.TokenDetailData;
import io.everitoken.sdk.java.dto.TokenDomain;
import io.everitoken.sdk.java.dto.TransactionDetail;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.ActionQueryParams;
import io.everitoken.sdk.java.param.MainNetNetParams;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.NetParams.NET;
import io.everitoken.sdk.java.param.PublicKeysParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.param.TransactionDetailParams;

public class ApiExample {
    public static void main(String[] args) {
        try {
            // replace this with method you want to test
            // getTransactionDetailById("93e0aa6bed4b2b768ce461jcc2cb66319aaef87bdc413cbb7148cc4690bc799f");
            // getGroupDetail();
            getFungibleBalance();
            // NetParams netParams = new TestNetNetParams();
            // JSONObject state = new Api(netParams).getHeadBlockHeaderState();
            // System.out.println(state.toString());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }

    static void testDomainTokens() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        List<TokenDetailData> domainTokens = new Api(netParams).getDomainTokens("test1122", 10, 0);
        domainTokens.stream().forEach(tokenDetailData -> {
            System.out.println(tokenDetailData.getName());
        });
    }

    static void getSuspendedProposalByName() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        String suspendedProposal = new Api(netParams).getSuspendedProposal("testProposal13");
        System.out.println(suspendedProposal);
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
        ActionQueryParams actionParams = new ActionQueryParams("testdomainfei1", null,
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
        NetParams netParams = new MainNetNetParams(NET.MAINNET1);
        List<Asset> res = new Api(netParams)
                .getFungibleBalance(Address.of("EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H"));
        res.forEach(balance -> System.out.println(balance.toString()));
    }

    static void getTransactionDetailById(String trxId) throws ApiResponseException {
        // 93e0aa6bed4b2b768ce4617cc2cb66319aaef87bdc413cbb7148cc4690bc799f
        MainNetNetParams netParams = new MainNetNetParams(NetParams.NET.MAINNET1);
        TransactionDetailParams transactionDetailParams = new TransactionDetailParams(trxId);
        TransactionDetail res = new Api(netParams).getTransactionDetailById(transactionDetailParams);
        res.getSignatures().forEach((signature) -> System.out.println(signature.toString()));
    }

    static void getFungibleSymbolDetail() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        FungibleDetailData res = new Api(netParams).getFungibleSymbolDetail(1);
        System.out.println(res.getCreator());
    }

    static void getTransactionsDetailOfPublicKeys() throws ApiResponseException {
        NetParams netParams = new TestNetNetParams();
        org.json.JSONArray res = new Api(netParams).getTransactionsDetailOfPublicKeys(
                Arrays.asList(PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND")), 1, 1, "asc");
        System.out.println(res);
    }
}
