package com.ruse.world.packages.loyalty;

import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class LoyaltyManager {

    public static final int LOYALTY_TICKET = 5022;
    private long timeOnLogin;

    @Getter
    private int xp, level = 1;

    public void onLogin(){
        timeOnLogin = System.currentTimeMillis();
    }

    private void calculateLevel(){
        level = 1;
        xp = 0;
        int points = 2500;
        while(xp < points){
            xp += (level * 250);
            level++;
        }
        if(level >= 10)
            level = 10;
    }

    public int timeOnInstance(){
        return level * 5;
    }

    public int dropChance(){
        return (int) (level * 2.4);
    }

    public int doubleDropChance(){
        return (int) (level * 1.2);
    }

    public void handleLoyalty(@NotNull Player player){
        long time = (System.currentTimeMillis() - timeOnLogin) / 1000 > 0 ? (System.currentTimeMillis() - timeOnLogin) / 1000 : 1;

        if(!World.attributes.getSetting("loyalty")){
            return;
        }
        if(player.getAfk().isAFK()){
            timeOnLogin = System.currentTimeMillis();
            return;
        }
        if(time >= (400 * 60)){
            timeOnLogin = System.currentTimeMillis();
            return;
        }

        int multiplier = 1;

        if(player.getEquipment().contains(15584)){
            multiplier += 1;
        }

        int amount = 0;

        if(time % (360 * 60) == 0){
            player.getItems().addCharge("ancient-monic", 1);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 1 ancient monic charge for being online for 6 hours.");
        } else if(time % (300 * 60) == 0){
            amount = 10 * multiplier;
        } else if(time % (240 * 60) == 0){
            amount = 8 * multiplier;
        } else if(time % (120 * 60) == 0){
            amount = 6 * multiplier;
        } else if(time % (60 * 60) == 0){
           amount = 4 * multiplier;
        } else if(time % (30 * 60) == 0){
           amount = 3 * multiplier;
        } else if(time % (20 * 60) == 0){
            amount = 2 * multiplier;
        } else if(time % (10 * 60) == 0){
            amount = multiplier;
        }

        if(amount > 0) {
            player.getInventory().add(LOYALTY_TICKET, amount);
            xp += amount;
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with "+amount+" Loyalty Tickets for being online.");
            AchievementHandler.progress(player, amount, 9);
            AchievementHandler.progress(player, amount, 21);
            AchievementHandler.progress(player, amount, 36);
            AchievementHandler.progress(player, amount, 37);
            AchievementHandler.progress(player, amount, 60);
            AchievementHandler.progress(player, amount, 61);
            AchievementHandler.progress(player, amount, 81);
            AchievementHandler.progress(player, amount, 82);
        }


        calculateLevel();

    }
}
