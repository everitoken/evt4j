package io.everitoken.sdk.java.abi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.everitoken.sdk.java.EvtLink;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.provider.SignProvider;

class EveriPassActionTest {
    @Test
    void serializeTest() {
        String expected = "\"domain\":\"test1125\",\"key\":\"t20\",\"name\":\"everipass\"}";

        NetParams netParams = new TestNetNetParams();
        EvtLink evtLink = new EvtLink(netParams);
        // make sure the domain and token you use exist and has correct authorize keys
        EvtLink.EveriPassParam everiPassParam = new EvtLink.EveriPassParam(true, "test1125", "t20");
        String passText = evtLink.getEvtLinkForEveriPass(everiPassParam,
                SignProvider.of(KeyProvider.of("5J1by7KRQujRdXrurEsvEr2zQGcdPaMJRjewER6XsAR2eCcpt3D")));

        EveriPassAction everiPassAction = EveriPassAction.of(passText);
        JSONObject json = JSON.parseObject(JSON.toJSONString(everiPassAction));
        Assertions.assertTrue(json.containsKey("domain"));
        Assertions.assertTrue(json.containsKey("link"));
    }

}