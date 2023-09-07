package com.ruse.world.packages.packs.locked;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.locked.impl.*;

public class Locks {

    public static boolean handleLocks(Player player, int id){
        switch(id){
            case 29 -> {
                new SummerChest().open(player);
                return true;
            }
            case 25101 -> {
                new VoteChest().open(player);
                return true;
            }
            case 25102 -> {
                new SlayerChest().open(player);
                return true;
            }
            case 25103 -> {
                new PvMChest().open(player);
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
