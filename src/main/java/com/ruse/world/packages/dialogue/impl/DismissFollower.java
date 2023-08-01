package com.ruse.world.packages.dialogue.impl;

import com.ruse.world.content.skill.impl.summoning.SummoningTab;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class DismissFollower extends Dialogue {
    public DismissFollower(Player player) {
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
            case 0 -> sendOption("Dismiss your follower?", "Yes", "No");
        }
    }

    @Override
    public int id() {
        return 15;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        switch(option){
            case FIRST_OPTION_OF_TWO-> {
                SummoningTab.handleDismiss(getPlayer(), true);
                return true;
            }
            case SECOND_OPTION_OF_TWO->{
                end();
                return true;
            }
        }
        return false;
    }
}
