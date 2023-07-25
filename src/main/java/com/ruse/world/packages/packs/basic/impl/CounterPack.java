package com.ruse.world.packages.packs.basic.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class CounterPack extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(13650, Misc.random(100, 2500));
    }

    @Override
    public int getPackId() {
        return 20500;
    }

    @Override
    public int openSpaces() {
        return 1;
    }
}
