package io.everitoken.sdk.java.service;

import java.util.Arrays;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.EveriPayAction;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;

class TransactionServiceTest {

    @Test
    void buildRawTransactionWithEveripay() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NetParams netParams = new TestNetNetParams();
            EvtLink evtLink = new EvtLink(netParams);
            EvtLink.EveriPayParam everiPayParam = new EvtLink.EveriPayParam(20, EvtLink.getUniqueLinkId(), 2000);
            String payText = evtLink.getEvtLinkForEveriPay(everiPayParam,
                    SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

            EveriPayAction action = EveriPayAction.of(payText, "0.00001 " + "S#1",
                    "EVT5cd4a3RyaVoubc4w3j3Z3YvCJgtKZPRdJHDdk7wVsMbc3yEH5U");

            TransactionService transactionService = TransactionService.of(netParams);
            TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
                    PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
                    KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"),
                    DateTime.now().toString().substring(0, 19));

            transactionService.buildRawTransaction(trxConfig, Arrays.asList(action), false);
        });
    }
}