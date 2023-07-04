package com.ruse.world.packages.gearpack;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.Objects;

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
}
