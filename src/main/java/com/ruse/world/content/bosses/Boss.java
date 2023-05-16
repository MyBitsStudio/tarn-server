package com.ruse.world.content.bosses;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;

public abstract class Boss extends NPC {
    public Boss(int id, Position position) {
        super(id, position, false);
    }


}
