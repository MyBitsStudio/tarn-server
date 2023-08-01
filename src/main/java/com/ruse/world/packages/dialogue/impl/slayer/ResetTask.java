package com.ruse.world.packages.dialogue.impl.slayer;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class ResetTask extends Dialogue {
    public ResetTask(Player player) {
        super(player);
    }

    @Override
    public DialogueType type() {
        return DialogueType.NPC_STATEMENT;
    }

    @Override
    public DialogueExpression animation() {
        return DialogueExpression.NORMAL;
    }

    @Override
    public String[] items() {
        return new String[0];
    }

    @Override
    public void next(int stage) {
        switch(stage){
            case 0 -> sendNpcChat("It currently costs a skip scroll to cancel a task",
                    "You will also lose your current Task Streak.", "Are you sure you wish to continue?");
            case 1-> sendOption("Cancel Task?", "Yes, please", "Cancel");
        }
    }

    @Override
    public int id() {
        return 8;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        switch(option){
            case 1 -> {
                getPlayer().getSlayer().resetSlayerTask();
                end();return true;
            }
            case 2 -> {end(); return true;}
        }
        return false;
    }
}
