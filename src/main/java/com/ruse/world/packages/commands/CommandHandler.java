package com.ruse.world.packages.commands;

import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHandler {

    public static void handleCommand(@NotNull Player player, String command, String[] parts){
        PlayerLogs.logCommands(player.getUsername(), player.getUsername() + " used command ::" + command
                + " | Player rights = " + player.getRank());
        AdminCord.sendMessage(1116230874170667028L, player.getUsername() + " used command ::" + command
                + " | Player rights = " + player.getRank());


        switch(player.getRank()){
            case MODERATOR:
                if(!ModCommands.handleCommand(player, command, parts) && !PlayerCommands.handleCommand(player,command, parts)
                        && !DonatorCommands.handleCommand(player, parts) && !HelperCommands.handleCommand(player, command, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            case MANAGER:
            case ADMINISTRATOR:
                if(!AdminCommands.handleCommand(player, command, parts) && !ModCommands.handleCommand(player, command, parts)
                        && !PlayerCommands.handleCommand(player, command, parts) && !DonatorCommands.handleCommand(player, parts)
                        && !HelperCommands.handleCommand(player, command, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            case OWNER:
            case DEVELOPER:
                if(!OwnerCommands.handleCommand(player, command, parts) && !AdminCommands.handleCommand(player, command, parts)
                        && !ModCommands.handleCommand(player, command, parts) && !PlayerCommands.handleCommand(player, command, parts)
                        && !DonatorCommands.handleCommand(player, parts) && !YoutubeCommands.handleCommand(player, parts)
                        && !HelperCommands.handleCommand(player, command, parts))
                    player.getPacketSender().sendMessage("That command does not exist.");
                break;
            case TRAIL_STAFF:
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
