package com.ruse.world.packages.slot;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.misc.ItemIdentifiers;

public class SlotItems {

    public static boolean handlePerk(Player player, int slot){
        if(slot <= -1 || slot > 28) {
            player.getPacketSender().sendMessage("This slot does not exist.");
            return false;
        }
        Item item = player.getInventory().get(slot);
        if(item == null) {
            player.getPacketSender().sendMessage("This item does not exist.");
            return false;
        }
        switch(item.getId()){
            case 23149 -> {
                if(player.getInventory().getFreeSlots() <= 0){
                    player.getPacketSender().sendMessage("You need at least 1 free inventory space to do this.");
                    return true;
                }
                player.getInventory().delete(item.getId(), 1);
                SlotEffect effect = SlotEffect.lowBonus();
                Item reward = new Item(effect.getItemId(), 1);
                ItemIdentifiers.addItemIdentifier(reward.getUid(), "PERK", String.valueOf(effect.ordinal()));
                ItemIdentifiers.addItemIdentifier(reward.getUid(), "BONUS", String.valueOf(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1));
                player.getInventory().add(reward);
                player.getPacketSender().sendMessage("You have received a " + reward.getDefinition().getName() + " with a bonus of " + ItemIdentifiers.getItemIdentifier(reward.getUid(), "BONUS") + ".");
                return true;
            }
            case 23148 -> {
                if(player.getInventory().getFreeSlots() <= 0){
                    player.getPacketSender().sendMessage("You need at least 1 free inventory space to do this.");
                    return true;
                }
                player.getInventory().delete(item.getId(), 1);
                SlotEffect effect = SlotEffect.medBonus();
                Item reward = new Item(effect.getItemId(), 1);
                ItemIdentifiers.addItemIdentifier(reward.getUid(), "PERK", String.valueOf(effect.ordinal()));
                ItemIdentifiers.addItemIdentifier(reward.getUid(), "BONUS", String.valueOf(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1));
                player.getInventory().add(reward);
                player.getPacketSender().sendMessage("You have received a " + reward.getDefinition().getName() + " with a bonus of " + ItemIdentifiers.getItemIdentifier(reward.getUid(), "BONUS") + ".");
                return true;
            }
            case 23147 -> {
                if(player.getInventory().getFreeSlots() <= 0){
                    player.getPacketSender().sendMessage("You need at least 1 free inventory space to do this.");
                    return true;
                }
                player.getInventory().delete(item.getId(), 1);
                SlotEffect effect = SlotEffect.highBonus();
                Item reward = new Item(effect.getItemId(), 1);
                ItemIdentifiers.addItemIdentifier(reward.getUid(), "PERK", String.valueOf(effect.ordinal()));
                ItemIdentifiers.addItemIdentifier(reward.getUid(), "BONUS", String.valueOf(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1));
                player.getInventory().add(reward);
                player.getPacketSender().sendMessage("You have received a " + reward.getDefinition().getName() + " with a bonus of " + ItemIdentifiers.getItemIdentifier(reward.getUid(), "BONUS") + ".");
                return true;
            }


            case 23150, 23151, 23152, 23153, 23154, 23155, 23156,
                    23157, 23158, 23159, 23160, 25000, 25001 -> {
                PerkEquip.equipPerk(player,item);
                return true;
            }
        }
        return false;
    }
}
