package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.discord.BotManager;
import com.ruse.world.packages.discord.modal.Embed;
import com.ruse.world.packages.discord.modal.MessageCreate;
import com.ruse.world.packages.event.Event;
import com.ruse.world.packages.shops.ShopHandler;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.List;

public class HalloweenEvent extends Event {
    @Override
    public String name() {
        return "halloween";
    }

    @Override
    public void start() {
        World.register(new NPC(3306, new Position(2198, 4836, 0)));

        BotManager.getInstance().sendMessage("NORMAL", 1163982165252521994L,
                new MessageCreate(List.of("** [EVENT] Halloween event is active! ** "),
                        new Embed("[EVENT]", "**[EVENT]  Halloween event is active! ** ",
                                "[EVENT]", Color.GREEN, "The season is right!", new File(SecurityUtils.DISCORD+"jack.png"), null)));
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
                                player.getPSettings().setSetting("hallow-pack", true);
                                player.getInventory().add(20083, 1);
                                player.getInventory().add(17831, 1000);
                                player.getInventory().add(10835, 500);
                                player.getInventory().add(1960, 25);
                                player.getInventory().add(750, 1);
                                player.sendMessage("You have claimed the Halloween pack!");

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

    @Override
    public void handleEventDrop(Player player, Item drop, Position pos) {
        if(Misc.random(1700) == 31){
            player.getInventory().add(20083, 1);
            player.sendMessage("@red@[EVENT] @whi@You have gotten lucky and got a Halloween Cracker!");
        }
    }
}
