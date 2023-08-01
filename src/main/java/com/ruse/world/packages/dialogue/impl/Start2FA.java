package com.ruse.world.packages.dialogue.impl;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class Start2FA extends Dialogue {
    public Start2FA(Player player) {
        super(player);
    }

    @Override
    public DialogueType type() {
        return DialogueType.NPC_STATEMENT;
    }

    @Override
    public DialogueExpression animation() {
        return DialogueExpression.PLAIN_TALKING;
    }

    @Override
    public String[] items() {
        return new String[0];
    }

    @Override
    public void next(int stage) {
        switch(stage){
            case 0-> sendNpcChat("You can be able to add 2FA to your account for", "better account security",
                    "We highly recommend enabling this on your account for ", "account security and ease of access.");
            case 1 -> sendNpcChat("We will walk through how to set up your account for 2FA ", "and how it will be used in the future.", "Please make sure you have access to Google Authenticator before ", "continuing onto the next stage.");
            case 2 -> sendOption("ADo you want to continue?", "I am Ready!", "No thanks");
            case 3 -> sendNpcChat("Make sure your app is up and you ", "are ready to enter the code provided.", "You will receive your code, put it into the app, ", "and than verify it is working.");
            case 4 -> sendOption("Start 2FA?", "Start!", "No thanks");

        }
    }

    @Override
    public int id() {
        return 2;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        switch(getStage()){
            case 2 -> {
                if(option == FIRST_OPTION_OF_TWO){
                    nextStage();
                    return true;
                } else if(option == SECOND_OPTION_OF_TWO){
                    end();
                    return true;
                }
            }
            case 4 -> {
                if(option == FIRST_OPTION_OF_TWO){
                    getPlayer().getPacketSender().sendInterfaceRemoval();
                    getPlayer().getPSecurity().begin2FA();
                    return true;
                } else if(option == SECOND_OPTION_OF_TWO){
                    end();
                    return true;
                }
            }
        }
        return false;
    }
}
