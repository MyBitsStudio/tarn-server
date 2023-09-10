package com.ruse.world.packages.commands;

import com.ruse.GameSettings;
import com.ruse.model.Flag;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.security.PlayerLock;
import com.ruse.security.PlayerSecurity;
import com.ruse.security.save.impl.player.PlayerSecureLoad;
import com.ruse.util.Misc;
import com.ruse.util.NameUtils;
import com.ruse.world.World;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.mode.impl.*;
import com.ruse.world.entity.impl.player.Player;

import static com.ruse.security.tools.SecurityUtils.PLAYER_FILE;

public class AdminCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){

        Player player2, targets;
        String name;
        boolean found;

        switch (commands[0]) {
            case "pnpc" -> {
            int npcID = Integer.parseInt(commands[1]);
            player.setNpcTransformationId(npcID);
            player.getStrategy(npcID);
            player.getUpdateFlag().flag(Flag.APPEARANCE);
            return true;
             }
            case "find" -> {
                name = command.substring(5).toLowerCase().replaceAll("_", " ");
                player.getPacketSender().sendMessage("Finding item id for item - " + name);
                found = false;
                for (int i = 0; i < ItemDefinition.getMaxAmountOfItems(); i++) {
                    if (ItemDefinition.forId(i).getName().toLowerCase().contains(name)) {
                        player.getPacketSender().sendMessage("Found item with name ["
                                + ItemDefinition.forId(i).getName().toLowerCase() + "] - id: " + i);
                        found = true;
                    }
                }
                if (!found) {
                    player.getPacketSender().sendMessage("No item with name [" + name + "] has been found!");
                }
                return true;
            }
            case "server" -> {
                if(commands.length == 1 || commands.length == 2){
                    player.sendMessage("Usage: ::server ATTRIBUTE VALUE");
                    return true;
                } else {
                    World.attributes.setSetting(commands[1], Boolean.parseBoolean(commands[2]));
                }
                return true;
            }
            case "broadcast" -> {
                int time = Integer.parseInt(commands[1]);
                String message = command.substring(commands[0].length() + commands[1].length() + 2);
                for (Player players : World.getPlayers()) {
                    if (players == null) {
                        continue;
                    }
                    players.getPacketSender().sendBroadCastMessage(message, time);
                }
                World.sendBroadcastMessage(message);
                GameSettings.broadcastMessage = message;
                GameSettings.broadcastTime = time;
                return true;
            }
            case "unban" -> {
                return true;
            }
            case "spawn" -> {
                if (commands.length >= 2) {
                    switch (commands[1]) {
                        case "donation":
                        case "donate":
                            GlobalBossManager.getInstance().spawnDonationBoss();
                            player.getPacketSender().sendMessage("Spawning donation boss.");
                            return true;
                        case "vote":
                            GlobalBossManager.getInstance().spawnVoteBoss();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
//                        case "meruem":
//                            WorldBosses3.handleForcedSpawn();
//                            player.getPacketSender().sendMessage("Spawning vote boss.");
//                            return true;
//                        case "veigar":
//                            WorldBosses4.handleForcedSpawn();
//                            player.getPacketSender().sendMessage("Spawning vote boss.");
//                            return true;
//                        case "golden":
//                            WorldBosses.handleForcedSpawn();
//                            player.getPacketSender().sendMessage("Spawning vote boss.");
//                            return true;
//                        case "nine":
//                            WorldBosses2.handleForcedSpawn();
//                            player.getPacketSender().sendMessage("Spawning vote boss.");
//                            return true;
                    }
                } else {
                    player.getPacketSender().sendMessage("Invalid spawn command.");
                }
                return true;
            }
            case "unlock" -> {
                targets = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (targets == null) {
                    player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is not online. Attempting to unlock...");
                    PlayerSecurity security = new PlayerSecurity(command.substring(commands[0].length() + 1));
                    security.load();
                    PlayerLock lock = security.getPlayerLock();
                    lock.load(command.substring(commands[0].length() + 1));
                    if (lock == null) {
                        player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " lock is null.");
                    } else {
                        lock.setUsername(command.substring(commands[0].length() + 1));
                        lock.unlock();
                        lock.save();
                        security.save();
                        player.sendMessage("Unlocked " + command.substring(commands[0].length() + 1) + "'s account.");
                    }
                } else {
                    player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is online. Can't unlock an online account.");
                }
                return true;
            }
            case "changepassother" -> {
                if (commands.length < 2) {
                    player.getPacketSender().sendMessage("Use as ::changepassother [password] [username]");
                } else {
                    String password = commands[1];
                    targets = World.getPlayerByName(command.substring(commands[0].length() + commands[1].length() + 2));
                    if (targets == null) {
                        player.getPacketSender().sendMessage(command.substring(commands[0].length() + commands[1].length() + 2) + " is not online. Attempting to unlock...");
                        PlayerSecurity security = new PlayerSecurity(command.substring(commands[0].length() + commands[1].length() + 2));
                        security.load();
                        security.changePass(password);
                        player.sendMessage("Password has been successfully set");
                    } else {
                        player.getPacketSender().sendMessage("Player is online. Attempting to change password...");
                        targets.getPSecurity().changePass(password);
                        targets.sendMessage("@red@[STAFF] A staff member has just changed your password!");
                        player.sendMessage("Password has been successfully set");
                    }
                }
                return true;
            }
            case "calendar" -> {
                int calendar = Integer.parseInt(commands[1]);
                targets = World.getPlayerByName(command.substring(commands[0].length() + commands[1].length() + 2));
                if (targets == null) {
                    player.sendMessage("Player is offline");
                } else {
                    if (calendar == 0) {
                        targets.getPSettings().setSetting("donator", true);
                    } else if (calendar == 1) {
                        targets.getPSettings().setSetting("summer-unlock", true);
                    }
                    player.sendMessage("Successfully activated calendar!");
                    targets.sendMessage("@red@ Staff has just activated a calendar for you! Check it out on ::daily");
                }
                return true;
            }
            case "locked" -> {
                targets = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (targets == null) {
                    PlayerSecurity security = new PlayerSecurity(command.substring(commands[0].length() + 1));
                    security.load();
                    PlayerLock lock = security.getPlayerLock();
                    lock.load(command.substring(commands[0].length() + 1));
                    lock.setUsername(command.substring(commands[0].length() + 1));
                    if (lock.isLocked()) {
                        player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is locked = " + lock.getLock());
                    } else {
                        player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is not locked.");
                    }
                } else {
                    player.sendMessage("Player is online");
                }
                return true;
            }
            case "ipunlock" -> {
                targets = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (targets == null) {
                    String username = Misc.formatText(command.substring(commands[0].length() + 1).toLowerCase());
                    player.getPacketSender().sendMessage(username + " is offline. Attempting to unlock...");
                    PlayerSecurity security = new PlayerSecurity(username);
                    security.load();
                    PlayerLock lock = security.getPlayerLock();
                    lock.load(username);
                    Player players = new Player(null);
                    players.setUsername(username);
                    players.setLongUsername(NameUtils.stringToLong(username));
                    new PlayerSecureLoad(players).loadJSON(PLAYER_FILE + username + ".json").run();
                    players.getPSettings().setSetting("security", false);
                    lock.unlock();
                    lock.save();
                    security.resetSecurityListValue("ip");
                    security.save();
                    players.save();
                    player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " has been unlocked fully.");
                } else {
                    player.sendMessage("Player is online, can't unlock");
                }
                return true;
            }
            case "giveitem" -> {
                int id = Integer.parseInt(commands[1]);
                int amount = Integer.parseInt(commands[2]);
                String plrName = command
                        .substring(commands[0].length() + commands[1].length() + commands[2].length() + 3);
                Player target = World.getPlayer(plrName);
                if (target == null) {
                    player.getPacketSender().sendMessage(plrName + " must be online to give them stuff!");
                } else {
                    target.getInventory().add(id, amount);
                    player.getPacketSender().sendMessage(
                            "Gave " + amount + "x " + ItemDefinition.forId(id).getName() + " to " + plrName + ".");
                }
                return true;
            }
            case "mode" -> {
                String mode = commands[1];
                targets = World.getPlayerByName(command.substring(commands[0].length() + +commands[1].length() + 2));
                if (targets == null) {
                    player.sendMessage("Player is offline");
                } else {
                    switch (mode) {
                        case "ironman" -> {
                            player.getMode().changeMode(new Ironman());
                            player.sendMessage("You have set " + targets.getUsername() + "'s game mode to Ironman.");
                        }
                        case "ultimate" -> {
                            player.getMode().changeMode(new UltimateIronman());
                            player.sendMessage("You have set " + targets.getUsername() + "'s game mode to Ultimate.");
                        }
                        case "normal" -> {
                            player.getMode().changeMode(new Normal());
                            player.sendMessage("You have set " + targets.getUsername() + "'s game mode to Normal.");
                        }
                        case "veteran" -> {
                            player.getMode().changeMode(new Veteran());
                            player.sendMessage("You have set " + targets.getUsername() + "'s game mode to Veteran.");
                        }
                        case "group" -> {
                            player.getMode().changeMode(new GroupIronman());
                            player.sendMessage("You have set " + targets.getUsername() + "'s game mode to Group Ironman.");
                        }
                        default -> player.sendMessage("Invalid game mode.");
                    }
                }
                return true;
            }
        }
        return false;
    }
}
