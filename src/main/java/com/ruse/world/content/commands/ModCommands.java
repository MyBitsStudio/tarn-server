package com.ruse.world.content.commands;

import com.ruse.security.ServerSecurity;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;

public class ModCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){
        Player player2;
        switch(commands[0]){
            case "invisible":
                player.setVisible(!player.isVisible());
                player.sendMessage("You are now " + (player.isVisible() ? "visible" : "invisible"));
                return true;

            case "ban":
                if(commands.length >= 4){
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

        }
        return false;
    }
}
