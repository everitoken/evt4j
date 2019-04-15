package io.everitoken.sdk.java.dto;

import com.alibaba.fastjson.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FungibleDetailDataTest {
    private static final String raw = "{\"metas\":[],"
            + "\"creator\":\"EVT6MRyAjQq8ud7hVNYcfnVPJqcVpscN5So8BhtHuGYqET5GDW5CV\",\"current_supply\":\"10750.00000 "
            + "S#1\",\"address\":\"EVT000000E9qWyKaPjd7o1KLFhaogA1Hn849zfb1UvcRLF4vp64gf\",\"sym_name\":\"EVT\","
            + "\"create_time\":\"2018-05-31T12:00:00\",\"issue\":{\"name\":\"issue\",\"threshold\":1,"
            + "\"authorizers\":[{\"ref\":\"[G] .everiToken\",\"weight\":1}]},\"total_supply\":\"1000000000.00000 S#1\","
            + "\"sym\":\"5,S#1\",\"name\":\"EVT_name\",\"manage\":{\"name\":\"manage\",\"threshold\":0,"
            + "\"authorizers\":[]}}";

    @Test
    @DisplayName("Deserialize should be correct")
    void deserialize() {
        Assertions.assertDoesNotThrow(() -> {
            FungibleDetailData.ofRaw(JSONObject.parseObject(raw));
        });
    }
}