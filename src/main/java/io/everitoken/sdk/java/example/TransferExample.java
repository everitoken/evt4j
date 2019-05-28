package io.everitoken.sdk.java.example;

import java.util.Arrays;

import io.everitoken.sdk.java.abi.TransferAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

public class TransferExample {
    public static void main(String[] args) {
        NetParams netParam = new TestNetNetParams();
        TransferAction transferAction = TransferAction.of("test1126", "t1",
                Arrays.asList("EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H"), "");

        // try {
        // TransactionService transactionService = TransactionService.of(netParam);
        // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
        // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
        //
        // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"));
        //
        // TransactionData txData = transactionService.push(trxConfig,
        // Arrays.asList(transferAction));
        // System.out.println(txData.getTrxId());
        // } catch (ApiResponseException ex) {
        // System.out.println(ex.getRaw());
        // }
    }
}
