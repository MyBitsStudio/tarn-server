package com.ruse.world.packages.event.impl;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.GroundItem;
import com.ruse.model.Item;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.GroundItemManager;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.discord.BotManager;
import com.ruse.world.packages.discord.modal.Embed;
import com.ruse.world.packages.discord.modal.MessageCreate;
import com.ruse.world.packages.event.Event;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class StaffDropEvent extends Event {

    private final AtomicBoolean started = new AtomicBoolean(false);

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
    public static final Position[] positions = {
            new Position(2731, 3463, 0), new Position(2733, 3463, 0), new Position(2735, 3463, 0),
            new Position(2737, 3463, 0), new Position(2739, 3463, 0), new Position(2741, 3463, 0),
            new Position(2743, 3463, 0),
            new Position(2731, 3465, 0), new Position(2733, 3465, 0), new Position(2735, 3465, 0),
            new Position(2737, 3465, 0), new Position(2739, 3465, 0), new Position(2741, 3465, 0),
            new Position(2743, 3465, 0),
            new Position(2731, 3467, 0), new Position(2733, 3467, 0), new Position(2735, 3467, 0),
            new Position(2737, 3467, 0), new Position(2739, 3467, 0), new Position(2741, 3467, 0),
            new Position(2743, 3467, 0),
            new Position(2731, 3469, 0), new Position(2733, 3469, 0), new Position(2735, 3469, 0),
            new Position(2737, 3469, 0), new Position(2739, 3469, 0), new Position(2741, 3469, 0),
            new Position(2743, 3469, 0),
            new Position(2731, 3471, 0), new Position(2733, 3471, 0), new Position(2735, 3471, 0),
            new Position(2737, 3471, 0), new Position(2739, 3471, 0), new Position(2741, 3471, 0),
            new Position(2743, 3471, 0),
    };
    @Override
    public String name() {
        return "staffdrop";
    }

    @Override
    public void start() {

        BotManager.getInstance().sendMessage("NORMAL", 1163982165252521994L,
                new MessageCreate(List.of("** [EVENT] Staff Drop is starting! ** "),
                        new Embed("[EVENT]", "**[EVENT] Staff Drop is active! ** ",
                                "[EVENT]", Color.GREEN, "Get lucky and get amazing drops!", new File(SecurityUtils.DISCORD+"dollar.png"), null)));
        TaskManager.submit(new Task(true) {
            int cycle = 0;
            @Override
            protected void execute() {
                ++cycle;
                if(cycle == 1){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Staff drop is about to begin!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] Staff drop is about to begin!", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] Staff drop is about to begin!");
                    GameSettings.broadcastMessage = "[EVENT] Staff drop is about to begin!";
                    GameSettings.broadcastTime = 300;
                } else if(cycle == 21){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Mass Teleport About To Begin....");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] Mass Teleport About To Begin....", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] Mass Teleport About To Begin....");
                    GameSettings.broadcastMessage = "[EVENT] Mass Teleport About To Begin....";
                    GameSettings.broadcastTime = 300;
                } else if(cycle == 40){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Mass Teleporting....");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .filter(player -> !player.getLocation().equals(Locations.Location.AFK))
                            .forEach(player -> {
                                TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION, TeleportType.NORMAL);
                            });
                } else if(cycle >= 60){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Welcome to the event!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                TeleportHandler.teleportPlayer(player, new Position(2737, 3473, 0), TeleportType.ANCIENT);
                                player.getPacketSender().sendBroadCastMessage("[EVENT] Event has started!", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] Event has started!");
                    GameSettings.broadcastMessage = "[EVENT] Event has started!";
                    GameSettings.broadcastTime = 300;
                    this.stop();
                }
            }
        });

    }

    @Override
    public void stop() {
        TaskManager.submit(new Task(true) {

            int cycle = 0;
            @Override
            protected void execute() {
                ++cycle;
                if(cycle == 1){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Staff drop is wrapping up!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] Staff drop is wrapping up!", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] Staff drop is wrapping up!");
                    GameSettings.broadcastMessage = "[EVENT] Staff drop is wrapping up!";
                    GameSettings.broadcastTime = 300;
                } else if(cycle == 10){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Mass Teleport About To Begin....");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] Mass Teleport About To Begin....", 300);
                            });
                    World.sendBroadcastMessage("[EVENT] Mass Teleport About To Begin....");
                    GameSettings.broadcastMessage = "[EVENT] Mass Teleport About To Begin....";
                    GameSettings.broadcastTime = 300;
                } else if(cycle == 25){
                    World.sendNewsMessage("@red@[EVENT]@whi@ Thank you for coming to the event!");
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(player -> {
                                player.getPacketSender().sendBroadCastMessage("[EVENT] Thank you for coming to the event!", 300);
                                TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION, TeleportType.ANCIENT);
                            });
                    World.sendBroadcastMessage("[EVENT] Thank you for coming to the event!");
                    GameSettings.broadcastMessage = "[EVENT] Thank you for coming to the event!";
                    GameSettings.broadcastTime = 300;
                    this.stop();
                }
            }
        });
    }

    @Override
    public void onLogin(Player player) {
        TeleportHandler.teleportPlayer(player, new Position(2737, 3473, 0), TeleportType.ANCIENT);
        player.sendMessage("@red@[EVENT]@whi@ Teleporting to the Staff Event!");
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
            case 19877 -> {
                switch(option){
                    case 1->{
                        if(!player.getRank().isStaff()){
                            player.sendMessage("@red@[EVENT]@whi@ Only staff can activate the lever!");
                            return true;
                        }
                        if(started.get()){
                            player.sendMessage("@red@[EVENT]@whi@ Event has already started!");
                            return true;
                        }
                        started.set(true);
                        TaskManager.submit(new Task(2, false) {
                            int cycle = 0;
                            @Override
                            protected void execute() {
                                ++cycle;
                                if(cycle == 1){
                                    World.getPlayers().stream().filter(Objects::nonNull)
                                            .forEach(player -> {
                                                player.sendMessage("@red@[EVENT]@whi@ Drop Event is about to begin!");
                                                player.getPacketSender().sendBroadCastMessage("[EVENT] Drop Event is about to begin!", 300);
                                                World.sendBroadcastMessage("[EVENT] Drop Event is about to begin!");
                                                GameSettings.broadcastMessage = "[EVENT] Drop Event is about to begin!";
                                                GameSettings.broadcastTime = 300;
                                            });
                                } else if(cycle == 20){
                                    World.getPlayers().stream().filter(Objects::nonNull)
                                            .forEach(player -> player.sendMessage("@red@[EVENT]@whi@ Drop Event Coming!"));
                                } else if(cycle == 45){
                                    for(Position pos : positions){
                                        GroundItemManager.spawnGroundItem(player,
                                                new GroundItem(items[Misc.random(items.length - 1)], pos, null, true, 500, true, 500));
                                    }
                                    World.getPlayers().stream().filter(Objects::nonNull)
                                            .forEach(player -> {
                                                player.sendMessage("@red@[EVENT]@whi@ Drop Event has begun!");
                                                player.getPacketSender().sendBroadCastMessage("[EVENT] Drop Event has begun!", 300);
                                                World.sendBroadcastMessage("[EVENT] Drop Event has begun!");
                                                GameSettings.broadcastMessage = "[EVENT] Drop Event has begun!";
                                                GameSettings.broadcastTime = 300;
                                            });

                                    started.set(false);
                                    this.stop();
                                }
                            }
                        });
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

    }
}
