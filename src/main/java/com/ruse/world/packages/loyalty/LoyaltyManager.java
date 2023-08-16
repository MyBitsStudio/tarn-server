package com.ruse.world.packages.loyalty;

import com.ruse.world.entity.impl.player.Player;

public class LoyaltyManager {

    public static final int LOYALTY_TICKET = 5022;
    private long timeOnLogin;

    public void onLogin(){
        timeOnLogin = System.currentTimeMillis();
    }

    public void handleLoyalty(Player player){
        long time = (System.currentTimeMillis() - timeOnLogin) / 1000 > 0 ? (System.currentTimeMillis() - timeOnLogin) / 1000 : 1;

        if(time >= (400 * 60)){
            timeOnLogin = System.currentTimeMillis();
            return;
        }

        if(time % (360 * 60) == 0){
            player.getItems().addCharge("ancient-monic", 1);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 1 ancient monic charge for being online for 6 hours.");
        } else if(time % (300 * 60) == 0){
            player.getInventory().add(LOYALTY_TICKET, 10);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 10 Loyalty Tickets for being online for 5 hours.");
        } else if(time % (240 * 60) == 0){
            player.getInventory().add(LOYALTY_TICKET, 8);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 8 Loyalty Tickets for being online for 4 hours.");
        } else if(time % (180 * 60) == 0){
            player.getInventory().add(LOYALTY_TICKET, 6);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 6 Loyalty Tickets for being online for 3 hours.");
        } else if(time % (120 * 60) == 0){
            player.getInventory().add(LOYALTY_TICKET, 4);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 4 Loyalty Tickets for being online for 2 hours.");
        } else if(time % (90 * 60) == 0){
            player.getInventory().add(LOYALTY_TICKET, 3);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 3 Loyalty Tickets for being online for 1.5 hours.");
        } else if(time % (60 * 60) == 0){
            player.getInventory().add(LOYALTY_TICKET, 2);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 2 Loyalty Tickets for being online for 1 hours.");
        } else if(time % (30 * 60) == 0){
            player.getInventory().add(LOYALTY_TICKET, 1);
            player.sendMessage("@red@[LOYALTY]@whi@You have been rewarded with 1 Loyalty Tickets for being online for 30 minutes.");
        }

    }
}