package com.ruse.world.packages.raids.testRaid.rooms.one.boss;

import com.ruse.model.Position;
import com.ruse.util.Misc;
import com.ruse.world.packages.raids.Raid;
import com.ruse.world.packages.raids.RaidBoss;
import com.ruse.world.packages.raids.RaidRoom;

public class RoomOneBoss extends RaidBoss {
    public RoomOneBoss(Raid raid, RaidRoom room) {
        super(raid, room, 9012,  new Position(2717 + Misc.getRandom(7), 2643 + Misc.getRandom(8)), false);
    }


}
