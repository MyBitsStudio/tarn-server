package com.ruse.world.packages.gearpack;

import com.ruse.model.Item;
import com.ruse.model.ItemBonus;
import com.ruse.model.projectile.ItemEffect;
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
            if(pack.isBoosted()){
                sendBoosted(player, pack);
            } else {
                player.getInventory().addItems(pack.getItems(), true);
            }
        }
    }

    public static void sendBoosted(Player player, GearPacks pack){
        Arrays.stream(pack.getItems())
                .filter(Objects::nonNull)
                .forEach(i -> {
                    GearPackBoost boost = GearPackBoost.getBoost(pack.getBoostTier(), i.getDefinition().getEquipmentSlot());
                    if(boost == null) {
                        player.getInventory().add(i, true);
                    } else {
                        player.getInventory().add(new Item(i.getId(), i.getAmount(), new ItemBonus(ItemEffect.valueOf(boost.getPerk()), boost.getBonus())), true);
                    }
                });
    }
}
