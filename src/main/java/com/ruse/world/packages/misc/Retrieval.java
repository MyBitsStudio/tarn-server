package com.ruse.world.packages.misc;

import com.ruse.engine.GameEngine;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.database.model.Retrievals;
import java.util.List;

public class Retrieval {

    public static void retrieve(Player player){
        GameEngine.submit(() -> {
            player.sendMessage("Retrieving data...");
            List<Retrievals> retrievals = World.database.getRetrievals(player);
            if(retrievals.isEmpty()){
                player.sendMessage("No data to retrieve.");
                return;
            }
            for(Retrievals retrieval : retrievals){
                switch(retrieval.itemId()) {
                    case 1 -> {// crystal Monic{
                        player.getItems().addCharge("crystal-monic", retrieval.amount());
                        player.sendMessage("You have retrieved " + retrieval.amount() + " crystal monic charges.");
                    }
                    case 2 -> {
                    // Ancient Monic
                        player.getItems().addCharge("ancient-monic", retrieval.amount());
                        player.sendMessage("You have retrieved " + retrieval.amount() + " ancient monic charges.");
                    }
                    default -> {
                        player.getInventory().add(retrieval.itemId(), retrieval.amount());
                        player.sendMessage("You have retrieved x"+retrieval.amount()+" of " + ItemDefinition.forId(retrieval.itemId()).getName());
                    }
                }
                try {
                    World.database.executeStatement("UPDATE `retrievals` SET `claimed` = '1' WHERE `id` = '" + retrieval.id() + "'");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            player.save();
        });
    }
}
