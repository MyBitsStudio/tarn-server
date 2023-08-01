package com.ruse.world.packages.dialogue.impl;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class EmptyInv extends Dialogue {
    public EmptyInv(Player player) {
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
            case 0-> sendOption("Empty your inventory?", "Yes", "No");
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
            case FIRST_OPTION_OF_TWO-> {
                getPlayer().getPacketSender().sendInterfaceRemoval().sendMessage("You clear your inventory.");
                getPlayer().getSkillManager().stopSkilling();
                    for (int i = 0; i < getPlayer().getInventory().capacity(); i++) {
                        if (getPlayer().getInventory().get(i) != null && getPlayer().getInventory().get(i).getId() > 0) {
                            if (ItemDefinition.forId(getPlayer().getInventory().get(i).getId()) == null || ItemDefinition.forId(getPlayer().getInventory().get(i).getId()).getName() == null) {
                                PlayerLogs.log(getPlayer().getUsername(), "Emptied item (id:" + getPlayer().getInventory().get(i).getId()
                                        + ", amount:" + getPlayer().getInventory().get(i).getAmount() + ")");
                            } else {
                                PlayerLogs.log(getPlayer().getUsername(),
                                        "Emptied item (id:" + getPlayer().getInventory().get(i).getId() + ", amount:"
                                                + getPlayer().getInventory().get(i).getAmount() + ") -- "
                                                + ItemDefinition.forId(getPlayer().getInventory().get(i).getId()).getName());
                            }
                        }

                        getPlayer().getInventory().resetItems().refreshItems();
                    }
                    end();
                    return true;
            }
            case SECOND_OPTION_OF_TWO -> {
                end();
                return true;
            }
        }
        return false;
    }
}
