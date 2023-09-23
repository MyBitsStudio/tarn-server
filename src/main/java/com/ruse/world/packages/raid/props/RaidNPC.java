package com.ruse.world.packages.raid.props;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;

public class RaidNPC extends NPC {
    public RaidNPC(int id, Position position, boolean respawn) {
        super(id, position, respawn);
    }
}
