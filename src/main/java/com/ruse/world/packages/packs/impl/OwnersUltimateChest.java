package com.ruse.world.packages.packs.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.Pack;

public class OwnersUltimateChest extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(23002, 4);
        player.getInventory().add(20506, 2);
    }

    @Override
    public int getPackId() {
        return 20507;
    }

    @Override
    public int openSpaces() {
        return 6;
    }
}
