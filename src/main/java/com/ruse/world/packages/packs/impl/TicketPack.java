package com.ruse.world.packages.packs.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.Pack;

public class TicketPack extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(21815, Misc.random(1, 20));
        player.getInventory().add(21814, Misc.random(1, 20));
        player.getInventory().add(21816, Misc.random(1, 10));
    }

    @Override
    public int getPackId() {
        return 20498;
    }

    @Override
    public int openSpaces() {
        return 3;
    }
}
