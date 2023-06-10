package com.ruse.world.packages.voting;

import com.ruse.motivote3.doMotivote;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.achievement.Achievements;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class VoteHandler {

    public static void processVote(@NotNull Player player){
        final String playerName = player.getUsername();

        com.everythingrs.vote.Vote.service.execute(() -> {
            try {
                com.everythingrs.vote.Vote[] reward = com.everythingrs.vote.Vote.reward("7qLCv34KB0NVyOqHYad6pxEuNqKkGXxVPPULMFR8yml5uKJbFzP3VbbrUT0I8Lgbzl8AONjs",
                        playerName, String.valueOf(1),"all");
                if (reward[0].message != null) {
                    player.getPacketSender().sendMessage(reward[0].message);
                    return;
                }
                player.getInventory().add(reward[0].reward_id, reward[0].give_amount);
                player.getPacketSender().sendMessage("Thank you for voting! You now have " + reward[0].vote_points + " vote points.");
                JavaCord.sendMessage("\uD83E\uDD16│\uD835\uDDEE\uD835\uDDF0\uD835\uDE01\uD835\uDDF6\uD835\uDE03\uD835\uDDF6\uD835\uDE01\uD835\uDE06", "**[" + player.getUsername() + "] Just voted for the server, thank you!**");
                doMotivote.setVoteCount(doMotivote.getVoteCount() + reward[0].give_amount);
                VoteBossDrop.save();
                player.getSeasonPass().incrementExp(36200 * reward[0].give_amount, false);
                Achievements.doProgress(player, Achievements.Achievement.VOTE_10_TIMES, reward[0].give_amount);
                Achievements.doProgress(player, Achievements.Achievement.VOTE_50_TIMES, reward[0].give_amount);
                Achievements.doProgress(player, Achievements.Achievement.VOTE_100_TIMES, reward[0].give_amount);
                randomBox(player);
                randomTicket(player);
                if (doMotivote.getVoteCount() >= 50) {
                    VoteBossDrop.handleSpawn();
                }
            } catch (Exception e) {
                player.getPacketSender().sendMessage("Api Services are currently offline. Please check back shortly");
                e.printStackTrace();
            }
        });
    }

    public static void randomBox(Player player){
        if(Misc.random(352) == 213){
            player.getInventory().add(15002, 1);
            player.getPacketSender().sendMessage("You have received a random Gracious box for voting!");
            World.sendMessage("<img=857><col=FF0000><shad=1>[" + player.getUsername() + "] Just got lucky and got a Gracious box from voting!");
        } else if(Misc.random(597) == 197){
            player.getInventory().add(15004, 1);
            player.getPacketSender().sendMessage("You have received a random Majestic box for voting!");
            World.sendMessage("<img=857><col=FF0000><shad=1>[" + player.getUsername() + "] Just got lucky and got a Majestic box from voting!");
        } else if(Misc.random(1117) == 732){
            player.getInventory().add(20489, 1);
            player.getPacketSender().sendMessage("You have received a random Infamous box for voting!");
            World.sendMessage("<img=857><col=FF0000><shad=1>[" + player.getUsername() + "] Just got lucky and got a Infamous box from voting!");
        }
    }

    public static void randomTicket(Player player){
        if(Misc.random(10) == 8){
            player.getInventory().add(23205, Misc.random(1,3));
            player.getPacketSender().sendMessage("You have received a random Ticket for voting!");
            World.sendMessage("<img=857><col=FF0000><shad=1>[" + player.getUsername() + "] Just got lucky and got a Ticket from voting!");
        }
    }

}
