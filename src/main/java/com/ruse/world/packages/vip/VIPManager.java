package com.ruse.world.packages.vip;

import com.ruse.io.data.impl.SQLVip;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.impl.DonatorBoostEvent;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class VIPManager {

    public static void handleDonation(@NotNull Player player, int amount){
        player.getPlayerVIP().addDonation(amount);
        player.getPacketSender().sendMessage("@blu@[DONATE]@whi@Thank you for your donation of $" + amount + "! K3");
        World.sendMessage("<img=857><col=FF0000><shad=1>[" + player.getUsername() + "] Just Donated For " + amount+". Thank You For The Support!");
    }

    public static void claim(Player player){
        try {
            new SQLVip().execute(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void progress(@NotNull Player player, int amount){
        player.getSeasonPass().incrementExp(4950 * amount, false);
        AchievementHandler.progress(player, amount, 7, 19, 32, 33, 56, 57, 78, 79);
    }

    public static int modify(Player player, int amount){
        int amounts = amount;
        if(World.handler.eventActive("DonatorRelease")){
            amounts *= 1.5;
        }
        if(World.handler.eventActive("DonatorBonus")){
            DonatorBoostEvent event = (DonatorBoostEvent) World.handler.getEvent("DonatorBonus");
            amounts *= 1 + (event.getBoost() / 100);
        }

        return amounts;

    }

    public static void handleTickets(Player player, int amount){
//        int amt = Math.floorDiv(amount, 10);
//        if(amt >= 1){
//            player.getInventory().add(1, amt);
//            player.getPacketSender().sendMessage("@blu@[DONATION] You have received "+amt+" donation tickets for your donation!");
//        }
    }

    public static void handleSpecialActive(Player player, int amount){
        if(amount >= 50){
            if(!player.getPSettings().getBooleanValue("donator")){
                player.getPSettings().setSetting("donator", true);
                player.getPacketSender().sendMessage("You have unlocked the Donator Calendar!");
            }
        }
    }

    public static void handleDonationBoss(int amount){
        World.attributes.setAmount("donation-boss", World.attributes.getAmount("donation-boss") + amount);
        if(World.attributes.getAmount("donation-boss") >= 350) {
            if(World.npcIsRegistered(587)){
                return;
            }
            World.attributes.setAmount("donation-boss", World.attributes.getAmount("donation-boss") - 350);
            GlobalBossManager.getInstance().spawnDonationBoss();
        }
    }
}
