package com.ruse.world.packages.commands;

import com.ruse.GameSettings;
import com.ruse.security.PlayerLock;
import com.ruse.security.PlayerSecurity;
import com.ruse.security.ServerSecurity;
import com.ruse.world.World;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;

import static com.ruse.world.entity.impl.player.PlayerFlags.FORCE_KICK;

public class ModCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){
        Player player2;
        switch(commands[0]){
            case "checkbank":
                player2 = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (player2 == null) {
                    player.getPacketSender().sendMessage("Player is offline!");
                } else {
                    player.getPacketSender().sendMessage("Loading bank..");
                    player2.getBank(0).openOther(player, true, false);
                }
                return true;

            case "checkinv":
                player2 = World.getPlayerByName(command.substring(commands[0].length() + 1));
                if (player2 == null) {
                    player.getPacketSender().sendMessage("Cannot find that player online..");
                    return true;
                }
                player.getPacketSender().sendItemContainer(player2.getInventory(), 3214);
                return true;

            case "endcheck":
                player.getInventory().refreshItems();
                return true;

            case "invisible":
                player.setVisible(!player.isVisible());
                player.sendMessage("You are now " + (player.isVisible() ? "visible" : "invisible"));
                return true;

            case "ban":
                if(commands.length >= 3){
                    player2 = World.getPlayerByName(command.substring(commands[0].length() + commands[1].length() + commands[2].length() + 3));
                    if (player2 == null) {
                        player.getPacketSender().sendMessage("Player is offline!");
                        return true;
                    } else {
                        switch(commands[1]){
                            case "norm" : ServerSecurity.getInstance().banPlayer(player2, 0, ((1000L * 60 * 60) * Integer.parseInt(commands[2]))); return true;
                            case "tri" : ServerSecurity.getInstance().banPlayer(player2, 1, ((1000L * 60 * 60) * Integer.parseInt(commands[2]))); return true;
                            case "full" : ServerSecurity.getInstance().banPlayer(player2, 2, ((1000L * 60 * 60) * Integer.parseInt(commands[2]))); return true;
                            case "max" : ServerSecurity.getInstance().banPlayer(player2, 3, ((1000L * 60 * 60) * Integer.parseInt(commands[2]))); return true;
                            case "perm" : ServerSecurity.getInstance().banPlayer(player2, 4, -1); return true;
                        }
                        player.sendMessage(player2.getUsername()+" was successfully banned");
                    }
                }
                return true;

            case "lock":
                String playerToTele = command.substring(commands[0].length() + 1);
                player2 = World.getPlayerByName(playerToTele);
                if (player2 == null) {
                    player.getPacketSender()
                            .sendMessage("Player " + playerToTele + " couldn't be found on " + GameSettings.RSPS_NAME + ".");
                    return true;
                }

                player2.save();
                World.removePlayer(player2);

                PlayerSecurity security = new PlayerSecurity(command.substring(commands[0].length() + 1));
                security.load();
                PlayerLock lock = security.getPlayerLock();
                lock.setUsername(command.substring(commands[0].length() + 1));
                lock.lock("secLock");
                lock.save();
                security.save();
                player.sendMessage("Locked " + command.substring(commands[0].length() + 1) + "'s account.");

                PlayerLogs.log(player.getUsername(),
                        player.getUsername() + " just locked " + player2.getUsername() + "!");
                World.sendStaffMessage("<col=FF0066><img=2> [PUNISHMENTS]<col=6600FF> " + player.getUsername()
                        + " just locked " + player2.getUsername() + ".");
                AdminCord.sendMessage(1109203238907027527L, player.getUsername() + " used command ::" + command
                        + " | Player rights = " + player.getRights());
                return true;

        }
        return false;
    }
}
