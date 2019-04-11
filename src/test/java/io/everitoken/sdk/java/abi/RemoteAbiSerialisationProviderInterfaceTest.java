package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import io.everitoken.sdk.java.exceptions.AbiSerialisationFailureException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;

class RemoteAbiSerialisationProviderInterfaceTest {

    @Test
    @Tag("Integration")
    @EnabledIfEnvironmentVariable(named = "ENV", matches = "INTEGRATION")
    @DisplayName("Serialize Abi via node")
    void serializeViaAbi() {
        String data = "{\"action\":\"newgroup\",\"args\":{\"name\":\"testgroupcreationfei\",\"group\":{\"name\":"
                + "\"testgroupcreationfei\",\"key\":\"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\","
                + "\"root\":{\"threshold\":6,\"weight\":0,\"nodes\":[{\"threshold\":1,\"weight\":3,\"nodes\":"
                + "[{\"key\":\"EVT6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV\",\"weight\":1},{\"key\""
                + ":\"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\"weight\":1}]},{\"key\":"
                + "\"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\"weight\":3},{\"threshold\":"
                + "1,\"weight\":3,\"nodes\":[{\"key\":\"EVT6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV\","
                + "\"weight\":"
                + "1},{\"key\":\"EVT8MGU4aKiVzqMtWi9zLpu8KuTHZWjQQrX475ycSxEkLd6aBpraX\",\"weight\":1}]}]}}}}";
        String newDomain = "{\"args\":\"{\"name\":\"testingTmpData\","
                + "\"creator\":\"EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\","
                + "\"issue\":{\"name\":\"issue\",\"threshold\":1,"
                + "\"authorizers\":[{\"ref\":\"[A] EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND\","
                + "\"weight\":1}]},\"transfer\":{\"name\":\"transfer\",\"threshold\":1,"
                + "\"authorizers\":[{\"ref\":\"[G] .OWNER\",\"weight\":1}]},"
                + "\"manage\":{\"name\":\"manage\",\"threshold\":1," + "\"authorizers\":[{\"ref\":\"[A] "
                + "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgNDy\",\"weight\":1}]}}\","
                + "\"action\":\"newdomain\"}";

        Assertions.assertDoesNotThrow(() -> {
            NetParams netParams = new TestNetNetParams();
            RemoteAbiSerialisationProvider provider = new RemoteAbiSerialisationProvider(netParams);
            JSONObject.parseObject(provider.serialize(data));
        });
    }

    @Test
    @DisplayName("Throw if input are not valid json")
    void throwEx() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            NetParams netParams = new TestNetNetParams();
            RemoteAbiSerialisationProvider provider = new RemoteAbiSerialisationProvider(netParams);
            JSONObject.parseObject(provider.serialize(""));
        });
    }

    @Test
    @DisplayName("Throw if input can't be serialised by Api")
    void throwExAgain() {
        Throwable throwable = Assertions.assertThrows(AbiSerialisationFailureException.class, () -> {
            NetParams netParams = new TestNetNetParams();
            RemoteAbiSerialisationProvider provider = new RemoteAbiSerialisationProvider(netParams);
            JSONObject.parseObject(provider.serialize("{\"action\":\"newgroup\", \"args\": {}}\""));
        });
        Assertions.assertTrue(throwable.getMessage().contains("Failed to serialize Abi"));
    }
}