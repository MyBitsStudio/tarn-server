package com.ruse.world.packages.slot;

import com.ruse.model.Item;
import com.ruse.model.container.impl.Equipment;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.slot.EquipOnSlot;
import com.ruse.world.packages.misc.ItemIdentifiers;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PerkEquip {

    public static void equipPerk(Player player, @NotNull Item item){
        if(Objects.equals(item.getPerk(), null) || Objects.equals(item.getPerk(), "none")){
            player.sendMessage("Something went wrong here. Perk is Null");
            return;
        }
        SlotEffect effect = SlotEffect.values()[Integer.parseInt(item.getPerk())];
        if(Objects.equals(effect, null)){
            player.sendMessage("Something went wrong here. Effect is Null");
            return;
        }

        boolean weaponPerk = effect.isWeapon();
        player.getVariables().setSetting("inv-slot", player.getInventory().slotOf(item));

        if(weaponPerk){
            player.getVariables().setSetting("slot-chosen", Equipment.WEAPON_SLOT);
            DialogueManager.sendDialogue(player, new EquipOnSlot(player), -1);
        } else {
            player.getPacketSender().sendInterface(162750);
        }

    }

    public static void handleButton(@NotNull Player player, int button){
        int slot = player.getEquipment().convertSlotFromClient(button);
        if(slot == -1){
            player.sendMessage("Something went wrong here...");
            return;
        }
        player.getVariables().setSetting("slot-chosen", slot);
        DialogueManager.sendDialogue(player, new EquipOnSlot(player), -1);
    }

    public static void finishEquip(@NotNull Player player){
        Item item = player.getInventory().get(player.getVariables().getIntValue("inv-slot"));
        if(Objects.equals(item, null)){
            player.sendMessage("Something went wrong here. Item is Null");
            return;
        }
        SlotEffect effect = SlotEffect.values()[Integer.parseInt(item.getPerk())];
        if(Objects.equals(effect, null) || Objects.equals(effect, SlotEffect.NOTHING)){
            player.sendMessage("Something went wrong here. Effect is Null");
            return;
        }
        if(player.getVariables().getIntValue("slot-chosen") == Equipment.WEAPON_SLOT && !effect.isWeapon()){
            player.sendMessage("You can't equip this perk in the weapon slot.");
            return;
        }
        player.getEquipment().getSlotBonuses()[player.getVariables().getIntValue("slot-chosen")] = new SlotBonus(effect, Integer.parseInt(item.getBonus()));
        player.getInventory().delete(item);
        player.getEquipment().refreshItems();
        player.sendMessage("You have added the perk to your"+player.getEquipment().slotToName(player.getVariables().getIntValue("slot-chosen"))+" slot.");
        reset(player);
    }

    public static void reset(@NotNull Player player){
        player.getVariables().setSetting("inv-slot", -1);
        player.getVariables().setSetting("slot-chosen", -1);
    }

    public static void fixPerks(@NotNull Player player){
        for(Item item : player.getInventory().getItems()){
            if(item == null)
                continue;

            if(SlotEffect.isPerkItem(item.getId())){
                SlotEffect effect = SlotEffect.forItemId(item.getId());
                if(effect == null)
                    continue;
                if(item.getPerk() == null)
                    continue;
                if (item.getPerk().equals("none"))
                    continue;

                if(effect.ordinal() != Integer.parseInt(item.getPerk())){
                    Item newItem = new Item(item.getId(), item.getAmount(),
                           "stale", String.valueOf(effect.ordinal()),  String.valueOf(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1));
                    player.getInventory().delete(item);
                    player.getInventory().add(newItem);
                    player.getInventory().refreshItems();
                }
            }
        }

        for(int i = 0; i < 6; i++)
            for(Item item : player.getBank(i).getItems()){
                if(item == null)
                    continue;
                if(item.getPerk() == null)
                    continue;
                if (item.getPerk().equals("none"))
                    continue;

                if(SlotEffect.isPerkItem(item.getId())){
                    SlotEffect effect = SlotEffect.forItemId(item.getId());
                    if(effect == null)
                        continue;
                    if(effect.ordinal() != Integer.parseInt(item.getPerk())){
                        Item newItem = new Item(item.getId(), item.getAmount(),
                                "stale", String.valueOf(effect.ordinal()),  String.valueOf(effect.getRanges().length != 0 ? Misc.random(effect.getRanges()[0], effect.getRanges()[1]) : -1));
                        player.getBank(i).delete(item);
                        player.getBank(i).add(newItem);
                        player.getInventory().refreshItems();
                    }
                }
            }

    }

}
