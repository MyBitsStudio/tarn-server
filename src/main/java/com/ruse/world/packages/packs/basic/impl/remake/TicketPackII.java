package com.ruse.world.packages.packs.basic.impl.remake;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class TicketPackII extends Pack {
    @Override
    public void open(Player player) {
        player.getInventory().add(Misc.random(21814, 21815), Misc.random(10, 25));
    }

    @Override
    public int getPackId() {
        return 23254;
    }

    @Override
    public int openSpaces() {
        return 1;
    }
}
