package com.ruse.world.packages.dialogue.impl;

import com.ruse.model.Position;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.content.transportation.TeleportType;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.DialogueType;

public class TaskTeleport extends Dialogue {
    public TaskTeleport(Player player) {
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
            case 0-> sendOption("Teleport to Task? Will cost 250k coins", "Teleport to task", "Cancel");
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
        switch(option){
            case FIRST_OPTION_OF_TWO -> {
                if (getPlayer().getSlayer().getAmountToSlay() > 0) {
                    if (getPlayer().getInventory().contains(ItemDefinition.COIN_ID, 250000)) {
                        Position pos = getPlayer().getSlayer().getSlayerTask().getTaskPosition();
                        TeleportHandler.teleportPlayer(getPlayer(), pos, TeleportType.TELE_TAB);
                        getPlayer().getInventory().delete(ItemDefinition.COIN_ID, 250000);
                    } else {
                        DialogueManager.sendStatement(getPlayer(), "You need 250,000 coins to do this.");
                    }
                } else {
                    DialogueManager.sendStatement(getPlayer(), "You do not currently have a task.");
                }
                return true;
            }
            case SECOND_OPTION_OF_TWO-> {
                end();
                return true;
            }
        }
        return false;
    }
}
