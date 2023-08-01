package com.ruse.world.packages.dialogue.impl.slot;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

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
            case 2-> end();
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
                getPlayer().sendMessage("HERE!");
                end();
                return true;
            }
            case SECOND_OPTION_OF_THREE -> {

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
