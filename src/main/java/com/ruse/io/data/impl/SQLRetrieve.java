package com.ruse.io.data.impl;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.ResultSet;
import com.github.jasync.sql.db.RowData;
import com.ruse.io.ThreadProgressor;
import com.ruse.io.data.model.DataHandler;
import com.ruse.io.data.model.DatabasePost;
import com.ruse.io.data.records.Retrievals;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class SQLRetrieve implements DatabasePost {

    @Override
    public void execute(@NotNull Player player, String... args) {
        String name = Misc.formatPlayerName(player.getUsername());
        List<Retrievals> retrieve = new ArrayList<>();

        ThreadProgressor.submit(true, () -> {
            try {
                QueryResult result = DataHandler.getInstance().result(statement()).get();

                final Optional<ResultSet> rowsOption = Optional.of(result.getRows());
                if(rowsOption.get().isEmpty()){
                    throw new RuntimeException("Empty Result Set");
                } else {
                    final ResultSet set = rowsOption.get();
                    for (RowData row : set) {
                        if(row.isEmpty()){
                            continue;
                        }
                        if (!Objects.equals(Objects.requireNonNull(row.getString("username")).toLowerCase(), name.toLowerCase())) {
                            continue;
                        }

                        if (row.getByte("claimed") == 1)
                            continue;

                        retrieve.add(new Retrievals(row.getInt("id"), row.getInt("item_id"), row.getInt("item_amount")));
                    }
                }

            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(retrieve.isEmpty()){
                player.sendMessage("No data to retrieve.");
                return null;
            }
            for(Retrievals retrieval : retrieve){
                new SQLRetrieveClaim().execute(player, String.valueOf(retrieval.id()));
                switch(retrieval.itemId()) {
                    case 1 -> {// crystal Monic{
                        player.getItems().addCharge("crystal-monic",retrieval.amount());
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
            return null;
        });
    }

    @Override
    public String statement() {
        return "SELECT * FROM retrievals";
    }
}
