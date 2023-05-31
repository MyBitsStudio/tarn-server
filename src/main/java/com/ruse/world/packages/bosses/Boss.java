package com.ruse.world.packages.bosses;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;

public abstract class Boss extends NPC {

    public Boss(int id, Position position, boolean respawns) {
        super(id, position, respawns);
        onSpawn();
    }

    public void scale(){}
    public void onSpawn(){}

    @Override
    public void onDeath(){
    }
}
