package com.ruse.world.packages.plus;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class PlusUpgrade {

    private static PlusUpgrade plusUpgrade;

    public static PlusUpgrade getPlusUpgrade() {
        if (plusUpgrade == null) {
            plusUpgrade = new PlusUpgrade();
        }
        return plusUpgrade;
    }

    public boolean isUpgradeable(int id){
        return PlusUpgrades.getPlus(id) != null;
    }

    public boolean upgradeGear(Player player, Item item){
        PlusUpgrades plus = PlusUpgrades.getPlus(item.getId());
        if(plus == null) {
            player.sendMessage("This item is not upgradeable.");
            return false;
        } else {
            Item[] cost = plus.getCost();
            AtomicBoolean hasItems = new AtomicBoolean(true);
            Arrays.stream(cost).forEach(i -> {
                if(!player.getInventory().contains(i.getId(), i.getAmount())){
                    hasItems.set(false);
                }
            });
            if(hasItems.get()){
                Arrays.stream(cost).forEach(i -> player.getInventory().delete(i));
                player.getInventory().delete(item);
                player.getInventory().add(new Item(plus.getResult().getId(), plus.getResult().getAmount()));
                return true;
            } else {
                player.getPacketSender().sendMessage("You do not have the required items to upgrade this item.");
                return false;
            }
        }
    }

    public void sendDialogue(Player player, Item item){

    }


}
