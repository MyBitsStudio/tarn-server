package com.ruse.world.packages.commands;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.event.WorldEventHandler;
import com.ruse.world.event.youtube.YoutubeBoss;

public class YoutubeCommands {

    public static boolean handleCommand(Player player, String[] commands){
        switch(commands[0]){
            case "events":
                if(commands.length >= 2){
                    switch(commands[1]){
                        case "boss":
                            WorldEventHandler.getInstance().startEvent(player, new YoutubeBoss(player, null));
                           return true;

                        case "finish":
                            WorldEventHandler.getInstance().getEvent(player.getUsername()).stop();
                            return true;
                    }
                } else {
                    player.sendMessage("EX: ::event boss/finish");
                }
                return true;
        }
        return false;
    }
}
