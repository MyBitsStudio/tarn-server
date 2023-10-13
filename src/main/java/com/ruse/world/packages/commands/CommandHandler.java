package com.ruse.world.packages.commands;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.packages.discord.BotManager;
import com.ruse.world.packages.discord.impl.admin.AdminBot;
import com.ruse.world.packages.discord.modal.MessageCreate;
import com.ruse.world.packages.discordbot.AdminCord;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class CommandHandler {

    public static void handleCommand(@NotNull Player player, String command, String[] parts){
        PlayerLogs.logCommands(player.getUsername(), player.getUsername() + " used command ::" + command
                + " | Player rights = " + player.getRank());
        BotManager.getInstance().sendMessage("ADMIN", AdminBot.COMMANDS, new MessageCreate("Player "+player.getUsername()+" used command ::"+command
                + " | Player rights = " + player.getRank()));
//        AdminCord.sendMessage(1116230874170667028L, player.getUsername() + " used command ::" + command
//                + " | Player rights = " + player.getRank());


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
