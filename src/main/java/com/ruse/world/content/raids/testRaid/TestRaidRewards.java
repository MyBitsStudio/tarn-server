package com.ruse.world.content.raids.testRaid;

import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidRewards;
import com.ruse.world.entity.impl.player.Player;

public class TestRaidRewards extends RaidRewards {
    public TestRaidRewards(Raid raid) {
        super(raid);
    }

    @Override
    public void claim(Player player) {

    }
}
