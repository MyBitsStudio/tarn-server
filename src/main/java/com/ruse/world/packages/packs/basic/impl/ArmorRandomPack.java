package com.ruse.world.packages.packs.basic.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class ArmorRandomPack extends Pack {

    @Override
    public void open(Player player) {
        int chance = Misc.random(20);
        if(chance == 0)
            player.getInventory().add(23127, 1);
        else if(chance == 1)
            player.getInventory().add(23128, 1);
        else if(chance == 2)
            player.getInventory().add(23129, 1);
        else if(chance == 3)
            player.getInventory().add(23130, 1);
        else if(chance == 4)
            player.getInventory().add(23131, 1);
        else if(chance == 5)
            player.getInventory().add(23132, 1);
        else if(chance == 6)
            player.getInventory().add(23133, 1);
        else if(chance == 7)
            player.getInventory().add(23128, 1);
        else if(chance == 8)
            player.getInventory().add(24000, 1);
        else if(chance == 9)
            player.getInventory().add(24001, 1);
        else if(chance == 10)
            player.getInventory().add(24002, 1);
        else if(chance == 11)
            player.getInventory().add(24003, 1);
        else if(chance == 12)
            player.getInventory().add(24004, 1);
        else if(chance == 13)
            player.getInventory().add(24005, 1);
        else if(chance == 14)
            player.getInventory().add(24006, 1);
        else if(chance == 15)
            player.getInventory().add(24007, 1);
        else if(chance == 16)
            player.getInventory().add(24008, 1);
        else if(chance == 17)
            player.getInventory().add(24009, 1);
        else if(chance == 18)
            player.getInventory().add(24010, 1);
    }

    @Override
    public int getPackId() {
        return 23225;
    }

    @Override
    public int openSpaces() {
        return 3;
    }
}
