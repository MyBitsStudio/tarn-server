package com.ruse.world.packages.packs.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.Pack;

public class YoutubePack extends Pack {

    @Override
    public void open(Player player) {
        int chance = Misc.random(4);
        switch(chance){
            case 0:
                player.getInventory().add(20498, 1);
                break;
            case 1:
                player.getInventory().add(20500, 1);
                break;
            case 2:
                player.getInventory().add(20501, 1);
                break;
            case 3:
                player.getInventory().add(20502, 1);
                break;
            case 4:
                player.getInventory().add(20489, 1);
                break;
        }
    }

    @Override
    public int getPackId() {
        return 20505;
    }

    @Override
    public int openSpaces() {
        return 4;
    }
}
