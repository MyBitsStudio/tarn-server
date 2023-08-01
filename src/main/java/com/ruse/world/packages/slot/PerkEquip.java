package com.ruse.world.packages.slot;

import com.ruse.model.Item;
import com.ruse.model.container.impl.Equipment;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.slot.EquipOnSlot;

import java.util.Objects;

public class PerkEquip {

    public static void equipPerk(Player player, Item item){
        boolean weaponPerk = SlotEffect.values()[item.getEffect()].isWeapon();
        player.getVariables().setSetting("inv-slot", player.getInventory().slotOf(item));

        if(weaponPerk){
            player.getVariables().setSetting("slot-chosen", ""+Equipment.WEAPON_SLOT);
            DialogueManager.sendDialogue(player, new EquipOnSlot(player), -1);
        } else {
            player.getPacketSender().sendInterface(162750);
        }

    }

    public static void handleButton(Player player, int button){
        int slot = player.getEquipment().convertSlotFromClient(button);
        if(slot == -1){
            player.sendMessage("Something went wrong here...");
            return;
        }
        player.getVariables().setSetting("slot-chosen", slot);
        DialogueManager.sendDialogue(player, new EquipOnSlot(player), -1);
    }

    public static void finishEquip(Player player){
        Item item = player.getInventory().get(player.getVariables().getIntValue("inv-slot"));
        if(Objects.equals(item, null)){
            player.sendMessage("Something went wrong here. Item is Null");
            return;
        }
        SlotEffect effect = SlotEffect.values()[item.getEffect()];
        if(Objects.equals(effect, null) || Objects.equals(effect, SlotEffect.NOTHING)){
            player.sendMessage("Something went wrong here. Effect is Null");
            return;
        }

        player.getEquipment().getSlotBonuses()[player.getVariables().getIntValue("slot-chosen")] = new SlotBonus(effect, item.getBonus());
        player.getInventory().delete(item);
        player.getEquipment().refreshItems();
        player.sendMessage("You have added the perk to your"+player.getEquipment().slotToName(player.getVariables().getIntValue("slot-chosen"))+" slot.");
        reset(player);
    }

    public static void reset(Player player){
        player.getVariables().setSetting("inv-slot", -1);
        player.getVariables().setSetting("slot-chosen", -1);
    }

}
