package io.everitoken.sdk.java.abi;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UpdateGroupAction extends GroupAction {
    @JSONField(deserialize = false, serialize = false)
    private static final String name = "updategroup";

    protected UpdateGroupAction(String groupName, JSONObject groupDef) {
        super(name, groupName, groupDef);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static UpdateGroupAction ofRaw(@NotNull String groupName, @NotNull JSONObject groupDef) {
        return new UpdateGroupAction(groupName, groupDef);
    }
}
