package com.ruse.security.save.impl.server.defs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.clans.Clan;
import com.ruse.world.packages.clans.ClanManager;

import java.util.List;

public class NPCDataLoad extends SecureLoad {
    public NPCDataLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public NPCDataLoad run() {
        for (JsonElement json : array) {
            JsonObject obj = json.getAsJsonObject();

            if(obj.has("_comment"))
                continue;

            if(obj.has("id")){
                int id = obj.get("id").getAsInt();
                NpcDefinition.definitions[id] = new NpcDefinition();
                NpcDefinition.definitions[id].setId(id);
                NpcDefinition.definitions[id].setName(obj.get("name").getAsString());
                NpcDefinition.definitions[id].setCombat(obj.get("combat").getAsInt());
                NpcDefinition.definitions[id].setSize(obj.get("size").getAsInt());
                NpcDefinition.definitions[id].setHitpoints(obj.get("hitpoints").getAsLong());
                NpcDefinition.definitions[id].setAttackSpeed(obj.get("attackSpeed").getAsInt());
                NpcDefinition.definitions[id].setAttackAnim(obj.get("attackAnim").getAsInt());
                NpcDefinition.definitions[id].setDefenceAnim(obj.get("defenceAnim").getAsInt());
                NpcDefinition.definitions[id].setDeathAnim(obj.get("deathAnim").getAsInt());
                NpcDefinition.definitions[id].setRespawn(obj.get("respawn").getAsInt());
                NpcDefinition.definitions[id].setAttackBonus(obj.get("attackBonus").getAsInt());
                NpcDefinition.definitions[id].setDefenceMelee(obj.get("defenceMelee").getAsInt());
                NpcDefinition.definitions[id].setDefenceRange(obj.get("defenceRange").getAsInt());
                NpcDefinition.definitions[id].setDefenceMage(obj.get("defenceMage").getAsInt());

                NpcDefinition.definitions[id].setAttackable(obj.get("attackable").getAsBoolean());
                NpcDefinition.definitions[id].setPoisonous(obj.get("poisonous").getAsBoolean());
                NpcDefinition.definitions[id].setAggressive(obj.get("aggressive").getAsBoolean());
                NpcDefinition.definitions[id].setRetreats(obj.get("retreats").getAsBoolean());

                NpcDefinition.definitions[id].setBoss(obj.get("isBoss").getAsBoolean());
                NpcDefinition.definitions[id].setPet(obj.get("isPet").getAsBoolean());

                if(object.has("slayerLevel"))
                    NpcDefinition.definitions[id].setSlayerLevel(obj.get("slayerLevel").getAsInt());


            }
        }
        return this;
    }
}
