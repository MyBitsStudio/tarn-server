package com.ruse.security.save.impl.server.defs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.combat.drops.CustomTable;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.packages.combat.drops.DropTable;
import com.ruse.world.packages.combat.drops.NPCDrops;

public class NPCDropsLoad extends SecureLoad {

    public NPCDropsLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public NPCDropsLoad run() {
        for (JsonElement json : array) {
            JsonObject obj = json.getAsJsonObject();

            if(obj.has("_comment"))
                continue;

            if(obj.has("id")){
                int[] ids = builder.fromJson(obj.get("id"), int[].class);
                String[] names = builder.fromJson(obj.get("tables"), String[].class);
                DropTable[] tables = new DropTable[names.length];
                for(int i = 0; i < names.length; i++){
                    tables[i] = DropManager.getManager().forName(names[i]);
                }
                CustomTable table = null;
                if(obj.has("custom")){
                    table = builder.fromJson(obj.get("custom"), CustomTable.class);
                }
                for(int npc : ids)
                    DropManager.getManager().addNPCDrops(new NPCDrops(npc, tables, table), npc);
            }
        }
        return this;
    }
}
