package com.ruse.world.packages.starter;

import com.ruse.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.Map;

public class StarterShop {

    private final Map<StartShopItems, Integer> items = new HashMap<>();
    private final Player player;

    public StarterShop(Player player){
        this.player = player;
    }

    public void open(){
        player.getPacketSender().sendInterface(-1);

    }

    public void purchase(StartShopItems item){

    }
}
