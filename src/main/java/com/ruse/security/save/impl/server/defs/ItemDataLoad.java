package com.ruse.security.save.impl.server.defs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.definitions.NpcDefinition;
import com.ruse.security.save.SecureLoad;

public class ItemDataLoad extends SecureLoad {
    public ItemDataLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public ItemDataLoad run() {
        for (JsonElement json : array) {
            JsonObject obj = json.getAsJsonObject();

            if(obj.has("_comment"))
                continue;

            if(obj.has("id")){
                int id = obj.get("id").getAsInt();
                ItemDefinition.definitions[id] = new ItemDefinition();
                ItemDefinition.definitions[id].setId(id);
                ItemDefinition.definitions[id].setName(obj.get("name").getAsString());
                ItemDefinition.definitions[id].setDescription(obj.get("examine").getAsString());
                ItemDefinition.definitions[id].setStackable(obj.get("stackable").getAsBoolean());
                ItemDefinition.definitions[id].setNoted(obj.get("noted").getAsBoolean());
                ItemDefinition.definitions[id].setTwoHanded(obj.get("2h").getAsBoolean());
                ItemDefinition.definitions[id].setEquipmentType(ItemDefinition.EquipmentType.valueOf(obj.get("type").getAsString()));
                ItemDefinition.definitions[id].setWeapon(obj.get("weapon").getAsBoolean());

                if(obj.has("reqs")){
                    ItemDefinition.definitions[id].setRequirement(builder.fromJson(obj.get("reqs"), int[].class));
                }

                if(obj.has("bonus")){
                    ItemDefinition.definitions[id].setBonus(builder.fromJson(obj.get("bonus"), double[].class));
                }


            }
        }
        return this;
    }
}
