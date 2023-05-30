package com.ruse.world.content.commands;

import com.ruse.GameSettings;
import com.ruse.security.PlayerLock;
import com.ruse.security.PlayerSecurity;
import com.ruse.security.ServerSecurity;
import com.ruse.world.World;
import com.ruse.world.content.WorldBosses;
import com.ruse.world.content.WorldBosses2;
import com.ruse.world.content.WorldBosses3;
import com.ruse.world.content.WorldBosses4;
import com.ruse.world.content.donation.DonationManager;
import com.ruse.world.content.voting.VoteBossDrop;
import com.ruse.world.entity.impl.player.Player;

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
                        case "meruem":
                            WorldBosses3.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                        case "veigar":
                            WorldBosses4.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                        case "golden":
                            WorldBosses.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                        case "nine":
                            WorldBosses2.handleForcedSpawn();
                            player.getPacketSender().sendMessage("Spawning vote boss.");
                            return true;
                    }
                } else {
                    player.getPacketSender().sendMessage("Invalid spawn command.");
                }
                return true;

            case "unlock":
                targets = World.getPlayer(command.substring(commands[0].length() + 1));
                if(targets == null) {
                    player.getPacketSender().sendMessage(command.substring(commands[0].length() + 1) + " is not online. Attempting to unlock...");
                    PlayerSecurity security = new PlayerSecurity(command.substring(commands[0].length() + 1));
                    security.load();
                    PlayerLock lock = security.getPlayerLock();
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
                    targets = World.getPlayer(command.substring(commands[0].length() + commands[1].length() + 2));
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
        return false;
    }
}
