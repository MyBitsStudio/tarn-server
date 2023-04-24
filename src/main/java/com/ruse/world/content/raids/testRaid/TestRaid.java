package com.ruse.world.content.raids.testRaid;

import com.ruse.model.GameObject;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidParty;
import com.ruse.world.entity.impl.player.Player;

public class TestRaid extends Raid {

    @Override
    public void start(RaidParty party) {

    }

    @Override
    public void rewardPlayer(Player player) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean handleObject(Player player, GameObject object, int option) {

        return false;
    }

    @Override
    public boolean handleDeath(Player player) {

        return true;
    }
}
