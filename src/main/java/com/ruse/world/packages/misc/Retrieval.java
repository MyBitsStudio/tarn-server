package com.ruse.world.packages.misc;

import com.ruse.io.data.impl.SQLRetrieve;
import com.ruse.world.entity.impl.player.Player;

public class Retrieval {

    public static void retrieve(Player player){
        try{
            new SQLRetrieve().execute(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
