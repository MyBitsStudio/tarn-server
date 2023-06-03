package com.ruse.world.packages.packs.impl;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.Pack;

public class EnhancementChest extends Pack {

    @Override
    public void open(Player player) {
        player.getInventory().add(13727, Misc.random(100, 5000));
        player.getInventory().add(10835, Misc.random(1000000, 20000000));
        int crystals = Misc.random(3);
        switch(crystals){
            case 0:  player.getInventory().add(2380, 5);break;
            case 1:  player.getInventory().add(2383, 5);break;
            case 2:  player.getInventory().add(2382, 5);break;
            case 3:  player.getInventory().add(2381, 5);break;
        }
    }

    @Override
    public int getPackId() {
        return 20506;
    }

    @Override
    public int openSpaces() {
        return 3;
    }
}
