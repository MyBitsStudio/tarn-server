package com.ruse.world.packages.packs.basic.impl.remake;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class EnhancePackIII extends Pack {
    @Override
    public void open(Player player) {
        player.getInventory().add(13727, Misc.random(165, 825));
        player.getInventory().add(Misc.random(2380, 2383), Misc.random(9));
    }

    @Override
    public int getPackId() {
        return 20502;
    }

    @Override
    public int openSpaces() {
        return 2;
    }
}
