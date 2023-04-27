package com.ruse.world.content.raids;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;

public class RaidBoss extends NPC {

    private final Raid raid;
    private final RaidRoom room;
    public RaidBoss(Raid raid, RaidRoom room, int id, Position position, boolean respawn) {
        super(id, position, respawn);
        this.raid = raid;
        this.room = room;
    }

    private void scaleNPC(){

    }

    public void onSpawn(){

    }

    @Override
    public void appendDeath() {
        raid.getCurrentRoom().setFinished(true);
        super.appendDeath();
    }

    public void takeDamage(){

    }
}
