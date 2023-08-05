package com.ruse.world.packages.dialogue.impl.tower;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;
import com.ruse.world.packages.tower.TarnTower;

public class NextLevel extends Dialogue {

    public NextLevel(Player player) {
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
            case 0-> sendOption("Progress To Next Level Or Leave?", "Next Level", "Leave");
        }
    }

    @Override
    public int id() {
        return 0;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        switch((option)){
            case FIRST_OPTION_OF_TWO -> {
                end();
                TarnTower.leave(getPlayer());
                TarnTower.startTower(getPlayer());
                return true;
            }
            case SECOND_OPTION_OF_TWO -> {
                end();
                TarnTower.leave(getPlayer());
                return true;
            }
        }
        return false;
    }
}
