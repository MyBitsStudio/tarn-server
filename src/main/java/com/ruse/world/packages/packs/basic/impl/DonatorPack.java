package com.ruse.world.packages.packs.basic.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class DonatorPack extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(23058, Misc.random(1,3));
        player.getInventory().add(15003, Misc.random(1, 3));
        player.getInventory().add(15002, Misc.random(1, 2));
        player.getInventory().add(15004, Misc.random(1, 2));
    }

    @Override
    public int getPackId() {
        return 20502;
    }

    @Override
    public int openSpaces() {
        return 4;
    }
}
