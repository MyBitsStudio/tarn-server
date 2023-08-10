package com.ruse.world.packages.packs.basic.impl.remake;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class EnhancePackII extends Pack {
    @Override
    public void open(Player player) {
        player.getInventory().add(13727, Misc.random(75, 460));
        player.getInventory().add(Misc.random(2380, 2382), Misc.random(6));
    }

    @Override
    public int getPackId() {
        return 20501;
    }

    @Override
    public int openSpaces() {
        return 2;
    }
}
