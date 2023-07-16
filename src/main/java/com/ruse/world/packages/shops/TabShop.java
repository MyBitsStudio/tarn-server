package com.ruse.world.packages.shops;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TabShop {

    private final int id;

    private final String name;

    private final boolean canSell;

    private final List<Shop> shops;

    private final List<Player> players = new ArrayList<>();

    private boolean needsUpdate = false, forceUpdate = false;

    public TabShop(int id, String name, boolean canSell, List<Shop> shops) {
        this.id = id;
        this.name = name;
        this.canSell = canSell;
        this.shops = shops;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Shop getShop(int id) {
        return this.shops.get(id);
    }

    public void process(){
        for(Shop shop : shops){
            if(shop == null)
                continue;

            shop.process();
        }

    }

    public void send(Player player){

    }
}
