package com.ruse.world.packages.afk;

import com.ruse.world.entity.impl.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public class AFKSystem {

    private Player player;
    private final AtomicBoolean afk = new AtomicBoolean(false);
    private int timer = 0;
    public AFKSystem(Player player){
        this.player = player;
    }

    public void process(){
        if(afk.get()){
            timer++;

            if(timer % (100 * 11) == 0){
                player.getInventory().add(5020, 1);
                player.getPacketSender().sendMessage("You have been given a reward for being AFK.");
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
