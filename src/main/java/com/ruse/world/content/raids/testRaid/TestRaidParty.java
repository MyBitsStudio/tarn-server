package com.ruse.world.content.raids.testRaid;

import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidParty;
import com.ruse.world.entity.impl.player.Player;

public class TestRaidParty extends RaidParty {
    public TestRaidParty(Player player, Raid raid) {
        super(player, raid);
    }

    @Override
    public void start() {

    }

    @Override
    public String key() {
        return "TESTING";
    }
}
