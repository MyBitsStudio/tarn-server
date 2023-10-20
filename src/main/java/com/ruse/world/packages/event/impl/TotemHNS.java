package com.ruse.world.packages.event.impl;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.GameObject;
import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.content.CustomObjects;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.discord.BotManager;
import com.ruse.world.packages.discord.modal.Embed;
import com.ruse.world.packages.discord.modal.MessageCreate;
import com.ruse.world.packages.event.Event;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;

public class TotemHNS extends Event {

    private final Player owner;
    private Position pos;

    public TotemHNS(Player player){
        this.owner = player;
    }

    public static final Position[] positions = {
            new Position(2898, 3618), new Position(2209, 3729), new Position(2178, 3732),
            new Position(3026, 10258, 36),  new Position(3026, 10258, 72),
            new Position(3002, 9498, 24),  new Position(2628, 3836, 4),
            new Position(2628, 3836, 20), new Position(2866, 2696, 0),
            new Position(2154, 5014, 12)
    };
    public static final Item[] items = {
            new Item(995, 5000), new Item(995, 10000), new Item(995, 15000), new Item(995, 25000),
            new Item(995, 50000), new Item(995, 75000), new Item(995, 100000), new Item(995, 250000),
            new Item(10835, 50), new Item(10835, 100), new Item(10835, 150), new Item(10835, 200),
            new Item(10835, 250), new Item(10835, 500),
            new Item(23250, 1), new Item(23251, 1), new Item(23252, 1),
            new Item(20500, 1), new Item(20500, 2), new Item(20500, 3),
            new Item(20501, 1), new Item(20501, 2), new Item(20501, 3),
            new Item(23253, 1), new Item(23253, 2), new Item(23253, 3),
            new Item(23254, 1), new Item(23254, 2), new Item(23254, 3),
            new Item(23256, 1), new Item(23256, 2), new Item(23256, 3),
            new Item(23257, 1), new Item(23257, 2), new Item(23258, 1),
            new Item(20488, 1), new Item(14490, 1),
            new Item(10946, 3), new Item(10946, 5), new Item(10946, 10),
            new Item(23057, 1), new Item(604, 5), new Item(604, 10),

    };

    //57437
    @Override
    public String name() {
        return "totemhns";
    }

    @Override
    public void start() {

        BotManager.getInstance().sendMessage("NORMAL", 1163982165252521994L,
                new MessageCreate(List.of("** [EVENT] Staff Totem HNS is active! ** "),
                        new Embed("[EVENT]", "**[EVENT]  Staff Totem HNS is active! ** ",
                                "[EVENT]", Color.GREEN, "Catch the totem!", new File(SecurityUtils.DISCORD+"totem.png"), null)));

        TaskManager.submit(new Task(true) {
            int cycle = 0;
            @Override
            protected void execute() {
                ++cycle;
                if(cycle == 1){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Staff Totem HNS is about to begin!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] Staff Totem HNS is about to begin!", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] Staff Totem HNS is about to begin!");
                    GameSettings.broadcastMessage = "[EVENT] Staff Totem HNS is about to begin!";
                    GameSettings.broadcastTime = 300;
                } else if(cycle == 30){
                    World.sendNewsMessage("@red@[EVENT]@whi@ The rules are about to begin! Please pay attention!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] The rules are about to begin! Please pay attention!", 300);

                            });
                    World.sendBroadcastMessage("[EVENT] The rules are about to begin! Please pay attention!");
                    GameSettings.broadcastMessage = "[EVENT] The rules are about to begin! Please pay attention!";
                    GameSettings.broadcastTime = 300;
                } else if(cycle == 60){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Here are the rules for Totem HNS Event.");
                    World.sendNewsMessage("@red@[EVENT]@whi@ The totem will be spawned in a random location.");
                    World.sendNewsMessage("@red@[EVENT]@whi@ The first person to find the totem and click it wins.");
                    World.sendNewsMessage("@red@[EVENT]@whi@ The winner will receive a reward automatically..");
                } else if(cycle == 90){
                    World.sendNewsMessage("@red@[EVENT]@whi@ The totem is about to be spawned!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] The totem is about to be spawned!", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] The totem is about to be spawned!");
                    GameSettings.broadcastMessage = "[EVENT] The totem is about to be spawned!";
                    GameSettings.broadcastTime = 300;
                } else if(cycle == 120){
                    pos = positions[Misc.random(positions.length - 1)];
                    GameObject totem = new GameObject(55350, pos);
                    CustomObjects.spawnGlobalObject(totem);
                    World.sendNewsMessage("@red@[EVENT]@whi@ The totem has spawned! Good Luck Hunting!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] The totem has spawned! Good Luck Hunting!", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] The totem has spawned! Good Luck Hunting!");
                    GameSettings.broadcastMessage = "[EVENT] The totem has spawned! Good Luck Hunting!";
                    GameSettings.broadcastTime = 300;
                    owner.sendMessage("The totem has spawned at : "+pos.getX()+"-"+pos.getY()+"-"+pos.getZ());
                    this.stop();
                }
            }
        });
    }

    @Override
    public void stop() {
        World.deregister(new GameObject(55350, pos));
        CustomObjects.deleteGlobalObject(new GameObject(55350, pos));
    }

    @Override
    public void onLogin(@NotNull Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Totem HNS Event is active! Start hunting!");
    }

    @Override
    public boolean handleItem(Player player, Item item) {
        return false;
    }

    @Override
    public boolean handleNpc(Player player, int npcId, int option) {
        return false;
    }

    @Override
    public boolean handleObject(Player player, int objectId, int option) {
        switch(objectId){
            case 55350 -> {
                World.deregister(new GameObject(55350, pos));
                CustomObjects.deleteGlobalObject(new GameObject(55350, pos));
                owner.sendMessage("The totem has been found by "+player.getUsername()+"!");
                player.sendMessage("@red@You have found the totem! Congratulations!");
                player.getInventory().add(items[Misc.random(items.length - 1)]);
                World.sendNewsMessage("@red@[EVENT]@whi@ The totem has been found! Thank you for playing!");
                World.getPlayers().stream().filter(Objects::nonNull)
                        .forEach(players -> {
                            players.getPacketSender().sendBroadCastMessage("[EVENT] The totem has been found! Thank you for playing!", 300);

                        });
                World.sendBroadcastMessage("[EVENT] The totem has been found! Thank you for playing!");
                GameSettings.broadcastMessage = "[EVENT] The totem has been found! Thank you for playing!";
                GameSettings.broadcastTime = 300;
                this.stop();
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

    }
}
