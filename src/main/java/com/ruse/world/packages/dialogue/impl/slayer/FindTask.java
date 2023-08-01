package com.ruse.world.packages.dialogue.impl.slayer;

import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class FindTask extends Dialogue {
    public FindTask(Player player) {
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
            case 0 -> {
                SlayerTasks task = getPlayer().getSlayer().getSlayerTask();
                assert task != null;
                String l = task.getNpcLocation();
                sendNpcChat("Your current task is to kill " + (getPlayer().getSlayer().getAmountToSlay()) + " "
                        + task.getName() + "s.", "You can find them at " + l + ".");
            }
            case 1 -> end();
        }

    }

    @Override
    public int id() {
        return 7;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        return false;
    }
}
