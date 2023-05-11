package com.ruse.world.content.raids.firefight.npc.one;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.SimpleScript;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidNPC;
import com.ruse.world.content.raids.RaidRoom;

public class FireFightRoomOneMinion extends RaidNPC {
    public FireFightRoomOneMinion(Raid raid, RaidRoom room, Position position) {
        super(raid, room, 2, position, false);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new SimpleScript();
    }


}
