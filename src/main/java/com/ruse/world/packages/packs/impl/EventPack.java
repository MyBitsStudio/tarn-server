package com.ruse.world.packages.packs.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.Pack;

public class EventPack extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(23210, Misc.random(1, 100));
        player.getInventory().add(23204, Misc.random(1, 10));
        player.getInventory().add(23205, Misc.random(1, 10));
    }

    @Override
    public int getPackId() {
        return 20501;
    }

    @Override
    public int openSpaces() {
        return 3;
    }
}
