package com.ruse.io.data.impl;

import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.ResultSet;
import com.github.jasync.sql.db.RowData;
import com.ruse.io.ThreadProgressor;
import com.ruse.io.data.model.DataHandler;
import com.ruse.io.data.model.DatabasePost;
import com.ruse.io.data.records.DonateRedeem;
import com.ruse.io.data.records.VoteRedeem;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.discordbot.JavaCord;
import com.ruse.world.packages.voting.VoteHandler;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class SQLVote implements DatabasePost {
    @Override
    public void execute(Player player, String... args) {
        String name = Misc.formatPlayerName(player.getUsername());
        List<VoteRedeem> votes = new ArrayList<>();

        ThreadProgressor.submit(true, () -> {
            try {
                QueryResult result = DataHandler.getInstance().result(statement()).get();

                final Optional<ResultSet> rowsOption = Optional.of(result.getRows());
                if (rowsOption.get().isEmpty()) {
                    throw new RuntimeException("Empty Result Set");
                } else {
                    final ResultSet set = rowsOption.get();
                    for (RowData row : set) {
                        if (row.isEmpty()) {
                            continue;
                        }
                        if (!Objects.equals(Objects.requireNonNull(row.getString("username")).toLowerCase(), name.toLowerCase())) {
                            continue;
                        }
                        if (Objects.equals((LocalDateTime)row.getDate("callback_date"), null)) {
                            continue;
                        }

                        if (row.getByte("claimed") == 1)
                            continue;

                        votes.add(new VoteRedeem(row.getInt("id"), row.getString("uid")));
                    }
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            if(votes.isEmpty()){
                player.getPacketSender().sendMessage("You have no votes to redeem.");
                return null;
            }
            for(VoteRedeem redeem : votes){
                new SQLVoteClaim().execute(player, redeem.uid());
                player.getPacketSender().sendMessage("Thank you for voting! Enjoy your reward!");
                JavaCord.sendMessage(1117224370587304057L, "**[" + player.getUsername() + "] Just voted for the server, thank you!**");
                VoteHandler.add(player);
                VoteHandler.progress(player);
                VoteHandler.checkBoss();
            }
            player.save();

            return null;
        });
    }

    @Override
    public String statement() {
        return "SELECT * FROM core_votes";
    }
}
