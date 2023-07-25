package com.ruse.world.packages.packs.basic.impl;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class EventPack extends Pack {

    @Override
    public void open(Player player) {
    }

    @Override
    public int getPackId() {
        return 20501;
    }

    @Override
    public int openSpaces() {
        return 3;
    }
}
