package com.ruse.world.packages.commands;

import com.ruse.GameSettings;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.security.PlayerLock;
import com.ruse.security.PlayerSecurity;
import com.ruse.security.save.impl.player.PlayerSecureLoad;
import com.ruse.util.Misc;
import com.ruse.util.NameUtils;
import com.ruse.world.World;
import com.ruse.world.content.WorldBosses;
import com.ruse.world.content.WorldBosses2;
import com.ruse.world.content.WorldBosses3;
import com.ruse.world.content.WorldBosses4;
import com.ruse.world.packages.donation.DonationManager;
import com.ruse.world.packages.voting.VoteBossDrop;
import com.ruse.world.entity.impl.player.Player;

import static com.ruse.security.tools.SecurityUtils.PLAYER_FILE;

public class AdminCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){

        Player player2, targets;

        switch(commands[0]){
            case "broadcast":
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

            case "unban":

                return true;

            case "spawn":
                if(commands.length >= 2) {
                    switch (commands[1]) {
                        case "donation": case "donate":
                            DonationManager.getInstance().forceSpawn();
                            player.getPacketSender().sendMessage("Spawning donation boss.");
                            return true;
                        case "vote":
                            VoteBossDrop.handleForcedSpawn();
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

            case "unlock":
                targets = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if(targets == null) {
                    player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is not online. Attempting to unlock...");
                    PlayerSecurity security = new PlayerSecurity(command.substring(commands[0].length() + 1));
                    security.load();
                    PlayerLock lock = security.getPlayerLock();
                    lock.load(command.substring(commands[0].length() + 1));
                    if(lock == null) {
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

            case "changepassother":
                if(commands.length < 2){
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

            case "calendar":
                int calendar = Integer.parseInt(commands[1]);
                targets = World.getPlayerByName(command.substring(commands[0].length() + commands[1].length() + 2));
                if(targets == null){
                    player.sendMessage("Player is offline");
                } else {
                    if(calendar == 0){
                        targets.getPSettings().setSetting("donator", true);
                    } else if(calendar == 1){
                        targets.getPSettings().setSetting("summer-unlock", true);
                    }
                    player.sendMessage("Successfully activated calendar!");
                    targets.sendMessage("@red@ Staff has just activated a calendar for you! Check it out on ::daily");
                }
                return true;

            case "locked":
                targets = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if(targets == null) {
                    PlayerSecurity security = new PlayerSecurity(command.substring(commands[0].length() + 1));
                    security.load();
                    PlayerLock lock = security.getPlayerLock();
                    lock.load(command.substring(commands[0].length() + 1));
                    lock.setUsername(command.substring(commands[0].length() + 1));
                    if(lock.isLocked()){
                        player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is locked = "+lock.getLock());
                    } else {
                        player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is not locked.");
                    }
                } else {
                    player.sendMessage("Player is online");
                }
                return true;

            case "ipunlock":
                targets = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if(targets == null) {
                    String username = Misc.formatText(command.substring(commands[0].length() + 1).toLowerCase());
                    player.getPacketSender().sendMessage(username + " is offline. Attempting to unlock...");
                    PlayerSecurity security = new PlayerSecurity(username);
                    security.load();
                    PlayerLock lock = security.getPlayerLock();
                    lock.load(username);
                    Player players = new Player(null);
                    players.setUsername(username);
                    players.setLongUsername(NameUtils.stringToLong(username));
                    new PlayerSecureLoad(players).loadJSON(PLAYER_FILE +username + ".json").run();
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

            case "giveitem":
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
        return false;
    }
}
