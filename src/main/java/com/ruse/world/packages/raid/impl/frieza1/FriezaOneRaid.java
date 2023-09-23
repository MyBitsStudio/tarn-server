package com.ruse.world.packages.raid.impl.frieza1;

import com.ruse.model.GameObject;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.raid.Raid;

public class FriezaOneRaid extends Raid {


    @Override
    public void start() {

    }

    @Override
    public void rewardPlayer(Player player) {

    }

    @Override
    public boolean handleObject(Player player, GameObject object, int option) {
        return false;
    }

    @Override
    public boolean handleDeath(Player player) {
        return false;
    }
}
