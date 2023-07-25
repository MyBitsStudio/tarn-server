package com.ruse.world.packages.packs.basic.impl;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class CertPack2 extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(22223, 1);
        player.getInventory().add(22214, 1);
        player.getInventory().add(22218, 1);
    }

    @Override
    public int getPackId() {
        return 23253;
    }

    @Override
    public int openSpaces() {
        return 3;
    }
}
