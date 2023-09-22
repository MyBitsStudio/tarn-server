package com.ruse.world.packages.afk;

import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class AFKSystem {

    private Player player;
    private final AtomicBoolean afk = new AtomicBoolean(false);
    private int timer = 0;
    public AFKSystem(Player player){
        this.player = player;
    }

    public void process(){
        if(!World.attributes.getSetting("afk")){
            return;
        }
        if(afk.get()){
            timer++;

            if(timer % (100 * 11) == 0){
                player.getInventory().add(5020, 1);
                player.getPacketSender().sendMessage("You have been given a reward for being AFK.");
                AchievementHandler.progress(player, 1, 8, 20, 34, 35, 58, 59, 80);
            }
        } else {
            timer = 0;
        }
    }

    public void setAFK(boolean afk){
        if(this.afk.get() && !afk){
            player.sendMessage("You are no longer AFK.");
        } else if(!this.afk.get() && afk){
            player.sendMessage("You are now AFK.");
        }
        this.afk.set(afk);
    }

    public boolean isAFK(){
        return afk.get();
    }
}
