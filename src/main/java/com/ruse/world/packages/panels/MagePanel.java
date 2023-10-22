package com.ruse.world.packages.panels;

import com.ruse.GameSettings;
import com.ruse.model.Position;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.instances.InstanceManager;

public class MagePanel {

    public static void refreshPanel(Player player) {

    }

    public static boolean handleButtons(Player player, int button) {
        switch(button){
            case 123001 -> {
                TeleportHandler.teleportPlayer(player, GameSettings.DEFAULT_POSITION.copy(),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            }
            case 123002 -> {
                player.getPacketSender().sendInterfaceRemoval();
                InstanceManager.getManager().sendInterface(player);
                return true;
            }
            case 123003 -> {
                player.getPacketSender().sendInterfaceRemoval();
                player.getTeleInterface().open();
                return true;
            }
            case 123004 -> {
                TeleportHandler.teleportPlayer(player, new Position(2856, 2708, 0),
                        player.getSpellbook().getTeleportType());
                player.getPacketSender().sendInterfaceRemoval();
                return true;
            }
        }
        return false;
    }
}
