package com.ruse.world.packages.packs.basic.impl.remake;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class CertPackII extends Pack {
    @Override
    public void open(Player player) {
        player.getInventory().add(Misc.random(22218, 22222), 1);
    }

    @Override
    public int getPackId() {
        return 23251;
    }

    @Override
    public int openSpaces() {
        return 1;
    }
}
