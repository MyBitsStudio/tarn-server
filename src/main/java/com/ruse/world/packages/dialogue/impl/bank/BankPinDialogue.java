package com.ruse.world.packages.dialogue.impl.bank;

import com.ruse.world.content.BankPin;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class BankPinDialogue extends Dialogue {

    private final boolean has;
    public BankPinDialogue(Player player, boolean has) {
        super(player);
        this.has = has;
    }

    @Override
    public DialogueType type() {
        return DialogueType.STATEMENT;
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
        if(has){
            switch(stage){
                case 0-> sendOption("Would you like to delete your pin?", "Delete", "Cancel");
            }
        } else {
            switch(stage){
                case 0-> sendRegularStatement("Would you like to set a pin?");
                case 1-> sendOption("Set Pin", "Yes", "No");
            }
        }
    }

    @Override
    public int id() {
        return 12;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        if(has){
            switch(getStage()){
                case 0-> {
                    switch(option){
                        case 1-> {
                            BankPin.deletePin(getPlayer());
                            end();
                            return true;
                        }
                        case 2-> {
                            end();
                            return true;
                        }
                    }
                }
            }
        } else {
            switch(getStage()){
                case 1-> {
                    switch(option){
                        case 1-> {
                            BankPin.init(getPlayer(), false);
                            end();
                            return true;
                        }
                        case 2-> {
                            end();
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
