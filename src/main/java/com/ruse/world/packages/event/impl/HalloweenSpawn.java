package com.ruse.world.packages.event.impl;

import com.ruse.GameSettings;
import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.special.event.EventBoss;
import com.ruse.world.packages.event.Event;
import com.ruse.world.packages.event.impl.props.HallowEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class HalloweenSpawn extends Event {

    public int spawns = 8, bosses = 3;

    public static int[] skeles = {
            6330, 6093, 5422, 5411
                    }, zombies = {
            8162, 5408, 5401
                    };

    public static Position[] spread = {
            new Position(3661, 3503), new Position(3659, 3497), new Position(3664, 3497), new Position(3662, 3493),
            new Position(3660, 3487), new Position(3669, 3487), new Position(3680, 3486), new Position(3689, 3490),
            new Position(3683, 3503), new Position(3672, 3502), new Position(3665, 3479), new Position(3670, 3473),
            new Position(3663, 3467), new Position(3657, 3470), new Position(3682, 3474), new Position(3697, 3475)

    };

    private boolean isZombies = false;
    @Override
    public String name() {
        return "halloween-spawn";
    }

    @Override
    public void start() {
        if(Misc.random(100) <= 49)
            isZombies = true;

        if(isZombies){
            for (int i = 0; i < spawns; i++) {
                World.register(new HallowEvent(zombies[Misc.random(zombies.length - 1)], spread[i]));
            }
        } else {
            for (int i = 0; i < spawns; i++) {
                World.register(new HallowEvent(skeles[Misc.random(skeles.length - 1)], spread[i]));
            }
        }

        World.getPlayers().stream().filter(Objects::nonNull)
                .forEach(player -> {
                    player.sendMessage("@red@[EVENT]@whi@ "+(isZombies? "Zombies" : "Skeletons") + " have taken over a town! ::takeover");
                    player.getPacketSender().sendBroadCastMessage("[EVENT] "+(isZombies? "Zombies" : "Skeletons") + " have taken over a town! ::takeover", 300);
                    World.sendBroadcastMessage("[EVENT] "+(isZombies? "Zombies" : "Skeletons") + " have taken over a town! ::takeover");
                    GameSettings.broadcastMessage = "[EVENT] "+(isZombies? "Zombies" : "Skeletons") + " have taken over a town! ::takeover";
                    GameSettings.broadcastTime = 300;
                });
    }

    @Override
    public void stop() {
        for (NPC npc : World.getNpcs()) {
            if (npc == null)
                continue;
            Arrays.stream(skeles).forEach(id -> {
                if (npc.getId() == id) {
                    World.deregister(npc);
                }
            });
            Arrays.stream(zombies).forEach(id -> {
                if (npc.getId() == id) {
                    World.deregister(npc);
                }
            });
            if(npc.getId() == 6104|| npc.getId() == 6105 || npc.getId() == 6100 || npc.getId() == 5665){
                World.deregister(npc);
            }
        }
    }

    @Override
    public void onLogin(@NotNull Player player) {
        player.sendMessage("@red@[EVENT]@whi@ "+(isZombies? "Zombies" : "Skeletons") + " have taken over a town! Defend the town!");
    }

    @Override
    public boolean handleItem(Player player, Item item) {
        return false;
    }

    @Override
    public boolean handleNpc(Player player, int npcId, int option) {
        switch(npcId){
        }
        return false;
    }

    @Override
    public boolean handleObject(Player player, int objectId, int option) {
        switch(objectId){
        }
        return false;
    }

    @Override
    public boolean onDeath(Player player, int id) {
        if(isZombies){
            if(Arrays.stream(zombies).anyMatch(i -> i == id)){
                spawns--;
                if(spawns == 0){
                    spawnBosses();
                }
                return true;
            }
            if(id == 6100 || id == 5665){
                bosses--;
                if(bosses <= 0){
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(p -> {
                                p.sendMessage("@red@[EVENT]@whi@ The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!");
                                p.getPacketSender().sendBroadCastMessage("[EVENT] The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!", 300);
                                World.sendBroadcastMessage("[EVENT] The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!");
                                GameSettings.broadcastMessage = "[EVENT] The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!";
                                GameSettings.broadcastTime = 300;
                            });
                    stop();
                }
                return true;
            }
        } else {
            if(Arrays.stream(skeles).anyMatch(i -> i == id)){
                spawns--;
                if(spawns == 0){
                    spawnBosses();
                }
                return true;
            }
            if(id == 6105 || id == 6104){
                bosses--;
                if(bosses <= 0){
                    World.getPlayers().stream().filter(Objects::nonNull)
                            .forEach(p -> {
                                p.sendMessage("@red@[EVENT]@whi@ The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!");
                                p.getPacketSender().sendBroadCastMessage("[EVENT] The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!", 300);
                                World.sendBroadcastMessage("[EVENT] The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!");
                                GameSettings.broadcastMessage = "[EVENT] The "+(isZombies? "Zombie Pirate" : "Skeleton Warlord")+" has been defeated! The town is safe!";
                                GameSettings.broadcastTime = 300;
                            });
                    stop();
                }
                return true;
            }
        }
        return true;
    }

    private void spawnBosses(){
        if(isZombies){
            TaskManager.submit(new Task(3, false) {
                int cycle = 0;
                @Override
                protected void execute() {
                    switch(++cycle){
                        case 1 -> World.getPlayers().stream().filter(Objects::nonNull)
                                .forEach(player -> {
                                    player.sendMessage("@red@[EVENT]@whi@ All zombies have been killed. There is a disturbance though...");
                                    player.getPacketSender().sendBroadCastMessage("[EVENT] All zombies have been killed. There is a disturbance though...", 300);
                                    World.sendBroadcastMessage("[EVENT] All zombies have been killed. There is a disturbance though...");
                                    GameSettings.broadcastMessage = "[EVENT] All zombies have been killed. There is a disturbance though...";
                                    GameSettings.broadcastTime = 300;
                                });

                        case 10 -> World.getPlayers().stream().filter(Objects::nonNull)
                                .forEach(player -> {
                                    player.sendMessage("@red@[EVENT]@whi@ The Zombie Pirate is coming! Be Prepared!");
                                    player.getPacketSender().sendBroadCastMessage("[EVENT] The Zombie Pirate is coming! Be Prepared!", 300);
                                    World.sendBroadcastMessage("[EVENT] The Zombie Pirate is coming! Be Prepared!");
                                    GameSettings.broadcastMessage = "[EVENT] The Zombie Pirate is coming! Be Prepared!";
                                    GameSettings.broadcastTime = 300;
                                });

                        case 15 -> {
                            World.register(new HallowEvent(5665, new Position(3659, 3496)));
                            World.register(new HallowEvent(6100, new Position(3662, 3494)));
                            World.register(new HallowEvent(6100, new Position(3662, 3498)));
                            World.getPlayers().stream().filter(Objects::nonNull)
                                    .forEach(player -> {
                                        player.sendMessage("@red@[EVENT]@whi@ The Zombie Pirate has spawned! Take it down!");
                                        player.getPacketSender().sendBroadCastMessage("[EVENT] The Zombie Pirate has spawned! Take it down!", 300);
                                        World.sendBroadcastMessage("[EVENT] The Zombie Pirate has spawned! Take it down!");
                                        GameSettings.broadcastMessage = "[EVENT] The Zombie Pirate has spawned! Take it down!";
                                        GameSettings.broadcastTime = 300;
                                    });
                            stop();
                        }
                    }
                }
            });
        } else {
            TaskManager.submit(new Task(3, false) {
                int cycle = 0;
                @Override
                protected void execute() {
                    switch(++cycle){
                        case 1 -> World.getPlayers().stream().filter(Objects::nonNull)
                                .forEach(player -> {
                                    player.sendMessage("@red@[EVENT]@whi@ All skeletons have been killed. There is a disturbance though...");
                                    player.getPacketSender().sendBroadCastMessage("[EVENT] All skeletons have been killed. There is a disturbance though...", 300);
                                    World.sendBroadcastMessage("[EVENT] All skeletons have been killed. There is a disturbance though...");
                                    GameSettings.broadcastMessage = "[EVENT] All skeletons have been killed. There is a disturbance though...";
                                    GameSettings.broadcastTime = 300;
                                });

                        case 10 -> World.getPlayers().stream().filter(Objects::nonNull)
                                .forEach(player -> {
                                    player.sendMessage("@red@[EVENT]@whi@ The Skeleton Warlord is coming! Be Prepared!");
                                    player.getPacketSender().sendBroadCastMessage("[EVENT] The Skeleton Warlord is coming! Be Prepared!", 300);
                                    World.sendBroadcastMessage("[EVENT] The Skeleton Warlord is coming! Be Prepared!");
                                    GameSettings.broadcastMessage = "[EVENT] The Skeleton Warlord is coming! Be Prepared!";
                                    GameSettings.broadcastTime = 300;
                                });

                        case 15 -> {
                            World.register(new HallowEvent(6105, new Position(3659, 3496)));
                            World.register(new HallowEvent(6104, new Position(3662, 3494)));
                            World.register(new HallowEvent(6104, new Position(3662, 3498)));
                            World.getPlayers().stream().filter(Objects::nonNull)
                                    .forEach(player -> {
                                        player.sendMessage("@red@[EVENT]@whi@ The Skeleton Warlord has spawned! Take it down!");
                                        player.getPacketSender().sendBroadCastMessage("[EVENT] The Skeleton Warlord has spawned! Take it down!", 300);
                                        World.sendBroadcastMessage("[EVENT] The Skeleton Warlord has spawned! Take it down!");
                                        GameSettings.broadcastMessage = "[EVENT] The Skeleton Warlord has spawned! Take it down!";
                                        GameSettings.broadcastTime = 300;
                                    });
                            stop();
                        }
                    }
                }
            });
        }
    }
}
