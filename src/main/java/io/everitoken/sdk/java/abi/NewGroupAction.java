package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class NewGroupAction extends GroupAction {
    @JSONField(deserialize = false, serialize = false)
    private static final String name = "newgroup";

    protected NewGroupAction(String groupName, JSONObject groupDef) {
        super(name, groupName, groupDef);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static NewGroupAction ofRaw(@NotNull String groupName, @NotNull JSONObject groupDef) {
        return new NewGroupAction(groupName, groupDef);
    }
}
