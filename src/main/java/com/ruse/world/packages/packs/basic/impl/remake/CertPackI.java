package com.ruse.world.packages.packs.basic.impl.remake;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class CertPackI extends Pack {
    @Override
    public void open(Player player) {
        player.getInventory().add(Misc.random(22214, 22217), 1);
    }

    @Override
    public int getPackId() {
        return 23250;
    }

    @Override
    public int openSpaces() {
        return 1;
    }
}
