package com.ruse.io.database.models.impl;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.database.models.DatabaseRequestStatement;
import com.ruse.io.database.records.Retrievals;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RetrieveClaim implements DatabaseRequestStatement {

    public static DatabaseRequestStatement finish = new RetrieveFinish();

    @Override
    public String statement() {
        return "SELECT * FROM retrievals WHERE username =?";
    }

    @Override
    public Object execute(Player player, String... args) {
        String name = Misc.formatPlayerName(player.getUsername());
        List<Retrievals> retrieve = new ArrayList<>();

        ThreadProgressor.takeRequest((connection) -> {
            try (PreparedStatement stmt = connection.prepareStatement(statement())
            ) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    while ( rs.next() ) {
                        if(rs.getInt("claimed") == 1)
                            continue;
                        retrieve.add(new Retrievals( rs.getInt("id"), rs.getInt("item_id"), rs.getInt("item_amount")));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(retrieve.isEmpty()){
                player.sendMessage("No data to retrieve.");
                return;
            }
            for(Retrievals retrieval : retrieve){
                finish.execute(player, String.valueOf(retrieval.id()));
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
            }
            player.save();
        });
        return null;
    }
}
