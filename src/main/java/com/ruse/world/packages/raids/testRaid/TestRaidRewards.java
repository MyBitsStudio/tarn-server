package com.ruse.world.packages.raids.testRaid;

import com.ruse.world.packages.raids.Raid;
import com.ruse.world.packages.raids.RaidRewards;
import com.ruse.world.entity.impl.player.Player;

public class TestRaidRewards extends RaidRewards {
    public TestRaidRewards(Raid raid) {
        super(raid);
    }

    @Override
    public void claim(Player player) {

    }
}
