package com.ruse.world.content.progressionzone;

import com.ruse.model.GameObject;
import com.ruse.model.Item;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.player.Player;

public class ProgressionZone {

    public static ZoneData.Monsters getCurrentZone(Player player) {
        if (player.getZonesComplete()[4] == true) {
            return ZoneData.Monsters.values()[4];
        }
        for (int i = player.getZonesComplete().length - 1; i >= 0; i--) {
            if (player.getZonesComplete()[i] == true) {
                return ZoneData.Monsters.values()[i + 1];
            }
        }
        return ZoneData.Monsters.GOBLIN;
    }

    public static void teleport(Player player) {

        player.sendMessage("You have been teleported to the " + getCurrentZone(player).getName() + " Zone.");

        player.sendMessage("@blu@Once you completed the zone, you can use the portal to advance to the next zone.");
    }


    public static void handleGates(Player player, GameObject gameObject, boolean back) {
        ZoneData.Monsters monster = getCurrentZone(player);

        int ordinal = gameObject.getPosition().getZ() / 4;

        if (gameObject.getPosition().getZ() >= 24){
            player.sendMessage("You can only do this in the default zone. To progress type ::train2");
            return;
        }

        if (player.getPosition().getZ() == gameObject.getPosition().getZ() && back) {
            if (ordinal == 0) {
                player.sendMessage("@mag@You are already at the lowest zone!");
            } else {
                player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ() - 4));
                player.sendMessage("@mag@You move to the previous zone!");
            }
            return;
        }
        if (ordinal +1 == ZoneData.Monsters.values().length) {
            player.sendMessage("@mag@You are at the highest zone.");
            return;
        }
        if (player.getZonesComplete()[ordinal]) {
            if (player.getPosition().getZ() == gameObject.getPosition().getZ()) {
                player.moveTo(new Position(player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ() + 4));
                player.sendMessage("@mag@You move to the next zone!");
            }
        } else {
            player.sendMessage("@mag@You need to kill x" + monster.getAmountToKill() + " " + monster.getName() + "'s to pass.");
        }
    }
}
