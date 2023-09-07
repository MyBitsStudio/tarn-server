package com.ruse.world.packages.packs.basic.impl.remake;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.basic.Pack;

public class PetEnhancementPack extends Pack {
    @Override
    public void open(Player player) {
        if(Misc.random(10) <= 8){
            player.getInventory().add(19000, Misc.random(10, 65));
        } else {
            player.getInventory().add(19002, Misc.random(5, 45));
        }

    }

    @Override
    public int getPackId() {
        return 19001;
    }

    @Override
    public int openSpaces() {
        return 1;
    }
}
