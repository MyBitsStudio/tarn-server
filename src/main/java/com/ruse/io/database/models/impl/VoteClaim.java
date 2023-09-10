package com.ruse.io.database.models.impl;

import com.ruse.io.ThreadProgressor;
import com.ruse.io.database.models.DatabaseRequestStatement;
import com.ruse.io.database.records.VoteRedeem;
import com.ruse.util.Misc;
import com.ruse.world.packages.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.voting.VoteHandler;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VoteClaim implements DatabaseRequestStatement {

    public static DatabaseRequestStatement finish = new VoteFinish();
    @Override
    public String statement() {
        return "SELECT * FROM core_votes WHERE username =?";
    }

    @Override
    public Object execute(@NotNull Player player, String... args) {
        String name = Misc.formatPlayerName(player.getUsername());
        List<VoteRedeem> votes = new ArrayList<>();
        ThreadProgressor.takeRequest((connection) -> {
            try(PreparedStatement stmt = connection.prepareStatement(statement())
            ) {
                stmt.setString(1, name);
                try (ResultSet rs = stmt.executeQuery()) {
                    while ( rs.next() ) {
                        if(rs.getInt("claimed") == 1) {
                            continue;
                        }
                        if(rs.getTime("callback_date") == null) {
                            continue;
                        }
                        votes.add(new VoteRedeem(rs.getInt("id"), rs.getString("uid")));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if(votes.isEmpty()){
                player.getPacketSender().sendMessage("You have no votes to redeem.");
                return;
            }
            for(VoteRedeem redeem : votes){
                finish.execute(player, redeem.uid());
                player.getPacketSender().sendMessage("Thank you for voting! Enjoy your reward!");
                JavaCord.sendMessage(1117224370587304057L, "**[" + player.getUsername() + "] Just voted for the server, thank you!**");
                VoteHandler.add(player);
                VoteHandler.progress(player);
                VoteHandler.checkBoss();
            }
            player.save();
        });
        return null;
    }
}
