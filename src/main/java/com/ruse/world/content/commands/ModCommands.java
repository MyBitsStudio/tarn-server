package com.ruse.world.content.commands;

import com.ruse.world.entity.impl.player.Player;

public class ModCommands {

    public static boolean handleCommand(Player player, String command, String[] commands){
        switch(commands[0]){
            case "invisible":
                player.setVisible(!player.isVisible());
                player.sendMessage("You are now " + (player.isVisible() ? "visible" : "invisible"));
                return true;


        }
        return false;
    }
}
