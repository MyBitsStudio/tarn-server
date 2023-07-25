package com.ruse.world.packages.packs.gearpack;

import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;

public class GearPack {

    public static void openGearPack(Player player, int itemId){
        GearPacks pack = GearPacks.getPack(itemId);
        if(pack != null) {
            int items = pack.getItems().length;
            if(player.getInventory().getFreeSlots() < items){
                player.getPacketSender().sendMessage("You need at least "+items+" free inventory slots to open this pack.");
                return;
            }
            player.getInventory().delete(itemId, 1);

            player.getInventory().addItems(pack.getItems(), true);

        }
    }

    public static void openRandomBox(Player player, int itemId){
        GearPacks pack = GearPacks.getPack(itemId);
        if(pack != null) {
            if(player.getInventory().getFreeSlots() < 1){
                player.getPacketSender().sendMessage("You need at least 1 free inventory slots to open this pack.");
                return;
            }
            player.getInventory().delete(itemId, 1);

            int random = Misc.random(pack.getItems().length - 1);
            player.getInventory().add(pack.getItems()[random].getId(), 1);

        }
    }
}
