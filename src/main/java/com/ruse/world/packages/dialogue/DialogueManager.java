package com.ruse.world.packages.dialogue;

import com.ruse.world.entity.impl.player.Player;

public class DialogueManager {

    public static void sendDialogue(Player player, Dialogue dia, int id){
        if(player.getChat() != null){
            player.getChat().end();
            player.setChat(null);
        }
        player.setChat(dia);
        player.getChat().start(id);
    }

    public static void sendForceDialogue(Player player, Dialogue dia, int id){
        if(player.getChat() != null){
            player.getChat().end();
            player.setChat(null);
        }
        player.getPacketSender().sendInterfaceRemoval();
        player.setChat(dia);
        player.getChat().start(id);
    }

    public static void sendStatement(Player player, String message){
        if(player.getChat() != null){
            player.getChat().end();
            player.setChat(null);
        }
        player.getPacketSender().sendString(357, message);
        player.getPacketSender().sendString(358, "Click here to continue");
        player.getPacketSender().sendChatboxInterface(356);
    }


}
