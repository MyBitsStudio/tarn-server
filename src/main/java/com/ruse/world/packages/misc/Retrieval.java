package com.ruse.world.packages.misc;

import com.ruse.io.database.models.DatabaseRequestStatement;
import com.ruse.io.database.models.impl.RetrieveClaim;
import com.ruse.world.entity.impl.player.Player;

public class Retrieval {

    public static DatabaseRequestStatement source = new RetrieveClaim();

    public static void retrieve(Player player){
        try{
            source.execute(player);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
