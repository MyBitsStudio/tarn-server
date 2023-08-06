package com.ruse.world.packages.dialogue.impl.tp;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class CancelTPOptions extends Dialogue {

    private final String itemName;
    private final int slot;
    public CancelTPOptions(Player player, String itemname, int slot) {
        super(player);
        this.itemName = itemname;
        this.slot = slot;
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
           case 0 -> sendOption("Cancel @red@" + itemName + "@bla@?", "Cancel", "Nevermind...");
       }
    }

    @Override
    public int id() {
        return 3;
    }

    @Override
    public void onClose() {
        getPlayer().getTradingPost().openMainInterface();
    }

    @Override
    public boolean handleOption(int option) {
        switch(option){
            case FIRST_OPTION_OF_TWO -> {
                end();
                getPlayer().getTradingPost().cancelSlot(slot);
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
