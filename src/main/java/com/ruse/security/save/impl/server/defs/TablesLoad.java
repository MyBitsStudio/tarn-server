package com.ruse.security.save.impl.server.defs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.packages.combat.drops.DropTable;

public class TablesLoad extends SecureLoad {

    public TablesLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public TablesLoad run() {
        for (JsonElement json : array) {
            JsonObject obj = json.getAsJsonObject();

            if(obj.has("_comment"))
                continue;

            if(obj.has("name")){
                DropManager.getManager().addTables(builder.fromJson(obj, DropTable.class));
            }
        }
        return this;
    }
}
