package com.ruse.world.packages.packs.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.Pack;

public class CertPack1 extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(22215, 1);
        player.getInventory().add(22228, 1);
        player.getInventory().add(22224, 1);
    }

    @Override
    public int getPackId() {
        return 23252;
    }

    @Override
    public int openSpaces() {
        return 3;
    }
}
