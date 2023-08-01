package com.ruse.world.packages.dialogue.impl.slot;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;
import com.ruse.world.packages.slot.PerkEquip;

public class EquipOnSlot extends Dialogue {
    public EquipOnSlot(Player player) {
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
            case 0-> sendRegularStatement("If you have a slot effect, it will be replaced.");
            case 1-> sendRegularStatement("To keep the effect, you must remove it first.");
            case 2-> sendOption("Equip to "+getPlayer().getEquipment().slotToName(getPlayer().getVariables().getIntValue("slot-chosen")), "Equip", "Cancel");
        }
    }

    @Override
    public int id() {
        return 14;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        switch(option){
            case FIRST_OPTION_OF_TWO -> {
                end();
                PerkEquip.finishEquip(getPlayer());
                return true;
            }
            case SECOND_OPTION_OF_TWO -> {
                end();
                PerkEquip.reset(getPlayer());
                return true;
            }
        }
        return false;
    }
}
