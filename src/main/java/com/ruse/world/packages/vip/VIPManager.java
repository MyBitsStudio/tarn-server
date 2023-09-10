package com.ruse.world.packages.vip;

import com.ruse.engine.GameEngine;
import com.ruse.io.ThreadProgressor;
import com.ruse.security.save.impl.server.PlayerDonationSave;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.database.model.DonateRedeem;
import com.ruse.world.packages.event.impl.DonatorBoostEvent;
import com.ruse.world.packages.globals.GlobalBossManager;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;

public class VIPManager {

    private static void handleDonation(@NotNull Player player, int amount){
        player.getPlayerVIP().addDonation(amount);
        player.getPacketSender().sendMessage("@blu@[DONATE]@whi@Thank you for your donation of $" + amount + "! K3");
        World.sendMessage("<img=857><col=FF0000><shad=1>[" + player.getUsername() + "] Just Donated For " + amount+". Thank You For The Support!");
    }

    public static void claim(Player player){
        try {
            ThreadProgressor.submit(true, () -> {
                List<DonateRedeem> donateRedeems = World.database.redeemDonate(player);
                if(donateRedeems.isEmpty()){
                    player.getPacketSender().sendMessage("You have no donations to redeem.");
                    return null;
                }
                for(DonateRedeem redeem : donateRedeems){
                    try {
                        World.database.executeStatement("UPDATE `payments` SET `claimed` = '1' WHERE `payment_id` = '" + redeem.payment_id() + "'");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    int amount = modify(player, redeem.amount());
                    handleDonation(player, amount);
                    new PlayerDonationSave(player, redeem.amount(), redeem.payment_id()).create().save();
                    progress(player, redeem.amount());
                    handleTickets(player, redeem.amount());
                    handleSpecialActive(player, redeem.amount());
                    handleDonationBoss(redeem.amount());
                }
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void progress(@NotNull Player player, int amount){
        player.getSeasonPass().incrementExp(265 * amount, false);
    }

    private static int modify(Player player, int amount){
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

    private static void handleTickets(Player player, int amount){
//        int amt = Math.floorDiv(amount, 10);
//        if(amt >= 1){
//            player.getInventory().add(1, amt);
//            player.getPacketSender().sendMessage("@blu@[DONATION] You have received "+amt+" donation tickets for your donation!");
//        }
    }

    private static void handleSpecialActive(Player player, int amount){
        if(amount >= 50){
            if(!player.getPSettings().getBooleanValue("donator")){
                player.getPSettings().setSetting("donator", true);
                player.getPacketSender().sendMessage("You have unlocked the Donator Calendar!");
            }
        }
    }

    private static void handleDonationBoss(int amount){
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
