package com.ruse.world.packages.voting;

import com.ruse.io.database.models.DatabaseRequestStatement;
import com.ruse.io.database.models.impl.VoteClaim;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import org.jetbrains.annotations.NotNull;

public class VoteHandler {

    public static DatabaseRequestStatement source = new VoteClaim();
    public static void processVote(@NotNull Player player){
        try {
           source.execute(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkBoss(){
        World.attributes.setAmount("vote-boss", World.attributes.getAmount("vote-boss") + 1);
        if(World.attributes.getAmount("vote-boss") >= 100){
            if(World.npcIsRegistered(8013)){
                return;
            }
            World.attributes.setAmount("vote-boss", World.attributes.getAmount("vote-boss") - 100);
            GlobalBossManager.getInstance().spawnVoteBoss();
        }
    }

    public static void progress(Player player){
        AchievementHandler.progress(player, 1, 5);
        AchievementHandler.progress(player, 1, 6);
        AchievementHandler.progress(player, 1, 17);
        AchievementHandler.progress(player, 1, 18);
        AchievementHandler.progress(player, 1, 30);
        AchievementHandler.progress(player, 1, 31);
        AchievementHandler.progress(player, 1, 54);
        AchievementHandler.progress(player, 1, 55);
        AchievementHandler.progress(player, 1, 76);
        AchievementHandler.progress(player, 1, 77);
        player.getStarter().handleVote(player.getPoints().get("voted"));
    }

    public static void add(@NotNull Player player){
        int amount = World.attributes.getSetting("vote-bonus") ? 2 : 1;
        player.getInventory().add(23020, amount);
        player.getInventory().add(4000, amount);
        if(Misc.random(10) == 1)
            player.getInventory().add(15682, amount);
        randomBox(player);
        randomTicket(player);
        player.getPoints().add("voted", 1);
        player.getSeasonPass().incrementExp(995, false);
    }

    public static void randomBox(Player player){
        int random = Misc.random(1000);
        if(random == 987)
            player.getInventory().add(25101, 1);
        else if(random >= 762 && random <= 786)
            player.getInventory().add(15682, 1);
        else if(random >= 313 && random <= 341)
            player.getInventory().add(18768, 1);
    }

    public static void randomTicket(Player player){
        int random = Misc.random(200);
        if(random == 187)
            player.getInventory().add(4001, Misc.random(1, 4));
        else if(random >= 67 && random <= 89)
            player.getInventory().add(4001, Misc.random(1, 2));
        else if(random >= 10 && random <= 50)
            player.getInventory().add(4000, Misc.random(1, 3));
        else if(random >= 10 && random <= 100)
            player.getInventory().add(4000, 1);
    }

}
