package io.everitoken.sdk.java.abi;

class NewSuspendActionTest {

    // @Test
    // void serializationTest() {
    // NetParams netParam = new TestNetNetParams();
    // Evt2PevtAction evt2PevtAction = Evt2PevtAction.of("0.00001 S#1",
    // "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
    // "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H", "test java");
    //
    // TransactionService transactionService = TransactionService.of(netParam);
    // TransactionConfiguration trxConfig = new TransactionConfiguration(1000000,
    // PublicKey.of("EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND"),
    // KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D"),
    // "2019-04-26T02:04:43");
    // try {
    // Transaction trx = transactionService.buildRawTransaction(trxConfig,
    // Arrays.asList(evt2PevtAction), false);
    // NewSuspendAction action = NewSuspendAction.of("tp15",
    // "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND", trx);
    //
    // Assertions.assertTrue(JSON.toJSONString(action)
    // .contains("\"domain\":\".suspend\",\"key\":\"newsuspend\"," +
    // "\"name\":\"tp15\","));
    // } catch (ApiResponseException ex) {
    // Assertions.assertTrue(false);
    // }
    // }
}