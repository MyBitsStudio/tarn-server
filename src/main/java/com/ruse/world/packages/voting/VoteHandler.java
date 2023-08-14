package com.ruse.world.packages.voting;

import com.ruse.engine.GameEngine;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.achievement.Achievements;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.database.model.VoteRedeem;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class VoteHandler {

    public static void processVote(@NotNull Player player){
        try {
            GameEngine.submit(() -> {
                List<VoteRedeem> voteRedeems = World.database.redeemVotes(player);
                if(voteRedeems.isEmpty()){
                    player.getPacketSender().sendMessage("You have no votes to redeem.");
                    return;
                }
                for(VoteRedeem redeem : voteRedeems){
                    try {
                        World.database.executeStatement("UPDATE `core_votes` SET `claimed` = '1' WHERE `uid` = '" + redeem.uid() + "'");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    player.getPoints().add("voted", 1);
                    player.getPacketSender().sendMessage("Thank you for voting! Enjoy your reward!");
                    JavaCord.sendMessage(1117224370587304057L, "**[" + player.getUsername() + "] Just voted for the server, thank you!**");
                    add(player);
                    progress(player);
                    player.save();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void progress(Player player){
        Achievements.doProgress(player, Achievements.Achievement.VOTE_10_TIMES, 1);
        Achievements.doProgress(player, Achievements.Achievement.VOTE_50_TIMES, 1);
        Achievements.doProgress(player, Achievements.Achievement.VOTE_100_TIMES, 1);
        player.getStarter().handleVote(player.getPoints().get("voted"));
    }

    private static void add(Player player){
        player.getInventory().add(23020, 1);
        player.getInventory().add(4000, 1);
        if(Misc.random(10) == 1)
            player.getInventory().add(15682, 1);
        randomBox(player);
        randomTicket(player);
    }

    public static void randomBox(Player player){
        int random = Misc.random(1000);
        if(random == 987)
            player.getInventory().add(23258, 1);
        else if(random >= 762 && random <= 786)
            player.getInventory().add(23257, 1);
        else if(random >= 313 && random <= 341)
            player.getInventory().add(23256, 1);
    }

    public static void randomTicket(Player player){
        int random = Misc.random(200);
        if(random == 187)
            player.getInventory().add(4001, Misc.random(3, 7));
        else if(random >= 67 && random <= 89)
            player.getInventory().add(4001, Misc.random(2, 5));
        else if(random >= 10 && random <= 50)
            player.getInventory().add(4001, Misc.random(1, 3));
        else if(random >= 10 && random <= 100)
            player.getInventory().add(4001, 1);
    }

}
