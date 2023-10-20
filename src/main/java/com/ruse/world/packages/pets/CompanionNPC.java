package com.ruse.world.packages.pets;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;

public class CompanionNPC extends NPC {

    public CompanionNPC(int id, Position position) {
        super(id, position, false);
    }
}
