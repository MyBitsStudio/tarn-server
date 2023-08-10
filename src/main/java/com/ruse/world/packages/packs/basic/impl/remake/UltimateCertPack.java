package com.ruse.world.packages.packs.basic.impl.remake;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class UltimateCertPack extends Pack {
    @Override
    public void open(Player player) {
        player.getInventory().add(22214, 1);
        player.getInventory().add(22215, 1);
        player.getInventory().add(22216, 1);
        player.getInventory().add(22217, 1);
        player.getInventory().add(22218, 1);
        player.getInventory().add(22219, 1);
        player.getInventory().add(22220, 1);
        player.getInventory().add(22221, 1);
        player.getInventory().add(22222, 1);
        player.getInventory().add(22223, 1);
        player.getInventory().add(22224, 1);
        player.getInventory().add(22225, 1);
        player.getInventory().add(22226, 1);
        player.getInventory().add(22227, 1);
        player.getInventory().add(22228, 1);
    }

    @Override
    public int getPackId() {
        return 23276;
    }

    @Override
    public int openSpaces() {
        return 16;
    }
}
