package com.ruse.world.packages.commands;

import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHandler {

    public static void handleCommand(@NotNull Player player, String command, String[] parts){
        PlayerLogs.logCommands(player.getUsername(), player.getUsername() + " used command ::" + command
                + " | Player rights = " + player.getRights());
        AdminCord.sendMessage(1109338481941020743L, player.getUsername() + " used command ::" + command
                + " | Player rights = " + player.getRights());


        switch(player.getRights()){
            case MODERATOR:
                if(!ModCommands.handleCommand(player, command, parts) && !PlayerCommands.handleCommand(player,command, parts)
                        && !DonatorCommands.handleCommand(player, parts) && !HelperCommands.handleCommand(player, command, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            case ADMINISTRATOR:
                if(!AdminCommands.handleCommand(player, command, parts) && !ModCommands.handleCommand(player, command, parts)
                        && !PlayerCommands.handleCommand(player, command, parts) && !DonatorCommands.handleCommand(player, parts)
                        && !HelperCommands.handleCommand(player, command, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            case DEVELOPER:
                if(!OwnerCommands.handleCommand(player, command, parts) && !AdminCommands.handleCommand(player, command, parts)
                        && !ModCommands.handleCommand(player, command, parts) && !PlayerCommands.handleCommand(player, command, parts)
                        && !DonatorCommands.handleCommand(player, parts) && !YoutubeCommands.handleCommand(player, parts)
                        && !HelperCommands.handleCommand(player, command, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            case SUPPORT:
            case HELPER:
                if(!PlayerCommands.handleCommand(player, command, parts) && !DonatorCommands.handleCommand(player, parts)
                 && !HelperCommands.handleCommand(player, command, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            case YOUTUBER:
                if(!PlayerCommands.handleCommand(player, command, parts) && !DonatorCommands.handleCommand(player, parts)
                 && !YoutubeCommands.handleCommand(player, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            default :
                if(!PlayerCommands.handleCommand(player, command, parts) && !DonatorCommands.handleCommand(player, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
        }
    }
}
