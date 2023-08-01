package com.ruse.world.packages.slot;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;

public class SlotItems {

    public static boolean handlePerk(Player player, Item item){
        switch(item.getId()){
            case 23149 -> {
                if(player.getInventory().getFreeSlots() <= 0){
                    player.getPacketSender().sendMessage("You need at least 1 free inventory space to do this.");
                    return true;
                }
                player.getInventory().delete(item);
                SlotEffect effect = SlotEffect.lowBonus();
                Item reward = new Item(effect.getItemId(), 1);
                reward.setEffect(effect.ordinal());
                reward.setBonus(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1);
                player.getInventory().add(reward);
                player.getPacketSender().sendMessage("You have received a " + reward.getDefinition().getName() + " with a bonus of " + reward.getBonus() + ".");
                return true;
            }
            case 23148 -> {
                if(player.getInventory().getFreeSlots() <= 0){
                    player.getPacketSender().sendMessage("You need at least 1 free inventory space to do this.");
                    return true;
                }
                player.getInventory().delete(item);
                SlotEffect effect = SlotEffect.medBonus();
                Item reward = new Item(effect.getItemId(), 1);
                reward.setEffect(effect.ordinal());
                reward.setBonus(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1);
                player.getInventory().add(reward);
                player.getPacketSender().sendMessage("You have received a " + reward.getDefinition().getName() + " with a bonus of " + reward.getBonus() + ".");
                return true;
            }
            case 23147 -> {
                if(player.getInventory().getFreeSlots() <= 0){
                    player.getPacketSender().sendMessage("You need at least 1 free inventory space to do this.");
                    return true;
                }
                player.getInventory().delete(item);
                SlotEffect effect = SlotEffect.highBonus();
                Item reward = new Item(effect.getItemId(), 1);
                reward.setEffect(effect.ordinal());
                reward.setBonus(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1);
                player.getInventory().add(reward);
                player.getPacketSender().sendMessage("You have received a " + reward.getDefinition().getName() + " with a bonus of " + reward.getBonus() + ".");
                return true;
            }


            case 23150, 23151, 23152, 23153, 23154, 23155, 23156,
                    23157, 23158, 23159, 23160 -> {
                PerkEquip.equipPerk(player,item);
                return true;
            }
        }
        return false;
    }
}
