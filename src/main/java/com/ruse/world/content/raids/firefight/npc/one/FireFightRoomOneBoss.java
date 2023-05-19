package com.ruse.world.content.raids.firefight.npc.one;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.SimpleScript;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidBoss;
import com.ruse.world.content.raids.RaidRoom;

public class FireFightRoomOneBoss extends RaidBoss {

    public FireFightRoomOneBoss(Raid raid, RaidRoom room) {
        super(raid, room, 589, new Position(3056, 2866), false);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new SimpleScript();
    }

}
