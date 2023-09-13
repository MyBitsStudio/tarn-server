package com.ruse.world.packages.dialogue.impl.slot;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;
import com.ruse.world.packages.misc.ItemIdentifiers;
import com.ruse.world.packages.slot.SlotBonus;

public class UnequipSlotPerk extends Dialogue {

    public UnequipSlotPerk(Player player) {
        super(player);
    }

    @Override
    public DialogueType type() {
        return DialogueType.OPTION;
    }

    @Override
    public DialogueExpression animation() {
        return null;
    }

    @Override
    public String[] items() {
        return new String[0];
    }

    @Override
    public void next(int stage) {
        switch(stage){
            case 0 -> sendRegularStatement("You have selected to unequip a slot perk.");
            case 1 -> sendOption("Would you like to remove the perk?", "Keep (Transfer Crystal)", "Remove", "Never Mind");
            case 2 -> end();
        }
    }

    @Override
    public int id() {
        return 1;
    }

    @Override
    public void onClose() {
        getPlayer().getEquipment().refreshItems();
    }

    @Override
    public boolean handleOption(int option) {
        switch(option){
            case FIRST_OPTION_OF_THREE -> {
                if(getPlayer().getInventory().contains(8788)){
                    if(getPlayer().getInventory().getFreeSlots() < 0){
                        getPlayer().sendMessage("You do not have enough inventory space to transfer the perk.");
                    } else {
                        getPlayer().getInventory().delete(8788, 1);
                        Item items = new Item(getPlayer().getEquipment().getSlotBonuses()[getPlayer().getVariables().getIntValue("slot-chosen")].getEffect().getItemId(), 1, Misc.createRandomString(12),
                                String.valueOf(getPlayer().getEquipment().getSlotBonuses()[getPlayer().getVariables().getIntValue("slot-chosen")].getEffect().ordinal()),
                                String.valueOf(getPlayer().getEquipment().getSlotBonuses()[getPlayer().getVariables().getIntValue("slot-chosen")].getBonus()));
                        getPlayer().getInventory().add(items);
                        getPlayer().getEquipment().getSlotBonuses()[getPlayer().getVariables().getIntValue("slot-chosen")] = new SlotBonus();
                        getPlayer().getEquipment().refreshItems();
                        getPlayer().sendMessage("You have transferred the slot perk to a token.");
                    }
                } else {
                    getPlayer().sendMessage("You do not have a crystal to transfer the perk to.");
                }
                end();
                return true;
            }
            case SECOND_OPTION_OF_THREE -> {
                getPlayer().getEquipment().getSlotBonuses()[getPlayer().getVariables().getIntValue("slot-chosen")] = new SlotBonus();
                getPlayer().getEquipment().refreshItems();
                getPlayer().sendMessage("You have removed the slot perk.");
                end();
                return true;
            }
            case THIRD_OPTION_OF_THREE -> {
                end();
                return true;
            }
        }
        return false;
    }
}
