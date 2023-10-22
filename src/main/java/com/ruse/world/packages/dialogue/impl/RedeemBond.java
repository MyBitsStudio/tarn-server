package com.ruse.world.packages.dialogue.impl;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.packages.panels.PlayerPanel;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class RedeemBond extends Dialogue {

    private final int item;
    private final boolean all;
    public RedeemBond(Player player, int item, boolean all) {
        super(player);
        this.item = item;
        this.all = all;
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
        return new String[]{String.valueOf(this.item), "1", "BOND"};
    }

    @Override
    public void next(int stage) {
        switch(stage){
            case 0 -> sendItemChat("Do you want to redeem this bond?");
            case 1 -> sendOption("Redeem Now?", "Yes", "No");
        }


    }

    @Override
    public int id() {
        return 13;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        int amount = 0;
        switch(option){
            case FIRST_OPTION_OF_TWO-> {
                end();
                switch(item){
                    case 23057 -> amount = 100;
                    case 23058 -> amount = 250;
                    case 23059 -> amount = 1000;
                    case 23060 -> amount = 2500;
                    case 10946 -> amount = 10;
                }
                if (getPlayer().getInventory().contains(item)) {
                    PlayerLogs.log(getPlayer().getUsername(),
                            "Has just redeemed a " + ItemDefinition.forId(item).getName() + " successfully!");


                    int amounts = all ? getPlayer().getInventory().getAmount(item) : 1;
                    if (all) {
                        amount *= amounts;
                    }

                    getPlayer().getInventory().delete(item, amounts);
                    getPlayer().incrementAmountDonated(amount);
                    getPlayer().getPlayerVIP().addPoints(amount);
                    getPlayer().getPacketSender().sendMessage("Your account has gained funds worth $" + (amount / 10)
                            + ". Your total is now at " + getPlayer().getPlayerVIP().getPoints() + ".");
                    PlayerPanel.refreshPanel(getPlayer());
                }
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
