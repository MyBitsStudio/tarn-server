package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;
import com.ruse.world.packages.shops.ShopHandler;
import org.jetbrains.annotations.NotNull;

public class HalloweenEvent extends Event {
    @Override
    public String name() {
        return "halloween";
    }

    @Override
    public void start() {
        World.register(new NPC(3306, new Position(2198, 4836, 0)));

    }

    @Override
    public void stop() {
        World.deregister(new NPC(3306, new Position(2198, 4836, 0)));
    }

    @Override
    public void onLogin(@NotNull Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Halloween event is active!");
    }

    @Override
    public boolean handleItem(Player player, Item item) {
        return false;
    }

    @Override
    public boolean handleNpc(Player player, int npcId, int option) {
        switch(npcId){
            case 3306 -> {
                switch(option){
                    case 1-> ShopHandler.getShop(10).ifPresent(shop -> shop.send(player, true));
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleObject(Player player, int objectId, int option) {
        switch(objectId){
            case 2488 -> {
                switch(option){
                    case 1->{
                        if(player.getPSettings().getBooleanValue("hallow-pack")){
                            player.sendMessage("You have already claimed your Halloween pack!");
                            return true;
                        }

                        if(player.getInventory().getFreeSlots() >= 5){
                            if(WorldIPChecker.getInstance().check(player, "hallow-pack")){
                                player.getInventory().add(20083, 1);
                                player.getInventory().add(17831, 1000);
                                player.getInventory().add(10835, 500);
                                player.getInventory().add(1960, 25);
                                player.getInventory().add(750, 1);
                                player.sendMessage("You have claimed the Halloween pack!");
                                player.getPSettings().setSetting("hallow-pack", true);

                            } else {
                                player.sendMessage("You have already claimed your Halloween pack!");
                            }
                        } else {
                            player.sendMessage("You need at least 5 free inventory slots to claim your Halloween pack!");
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDeath(Player player, int npcId) {
        return false;
    }
}
