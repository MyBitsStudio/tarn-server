package com.ruse.security.save.impl.server.defs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruse.model.Direction;
import com.ruse.model.Position;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.npc.NPCMovementCoordinator;

public class WorldNPCLoad extends SecureLoad {
    @Override
    public String key() {
        return null;
    }

    @Override
    public WorldNPCLoad run() {
        for (JsonElement json : array) {
            Position pos = null;
            NPCMovementCoordinator.Coordinator cord = null;
            Direction direction = null;
            int id;
            JsonObject obj = json.getAsJsonObject();

            if(obj.has("_comment"))
                continue;

            if(obj.has("id")){
                id = obj.get("id").getAsInt();

                if(obj.has("position")){
                    pos = builder.fromJson(obj.get("position"), Position.class);
                }

                if(obj.has("direction")){
                    direction = builder.fromJson(obj.get("direction"), Direction.class);
                }

                if(obj.has("movement")){
                    cord = builder.fromJson(obj.get("movement"), NPCMovementCoordinator.Coordinator.class);
                }

                NPC.startSpawn(id, direction, pos, cord);
            }

            pos = null;
            cord = null;
            direction = null;
            id = 0;
        }

        return this;
    }
}
