package com.ruse.world.packages.dialogue.impl.currency;

import com.ruse.world.content.BankPin;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class CurrencyPouch extends Dialogue {

    public CurrencyPouch(Player player) {
        super(player);

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
        switch(stage){
            case 0 -> sendOption("Withdraw",
                    "Coins x"+getPlayer().getPouch().checkBalance(995),
                    "Tokens x"+getPlayer().getPouch().checkBalance(10835),
                    "Afk Ticket x"+getPlayer().getPouch().checkBalance(5020),
                    "Next");
            case 1 -> sendOption("Withdraw",
                    "Loyalty x"+getPlayer().getPouch().checkBalance(5022),
                    "Pet Frags x"+getPlayer().getPouch().checkBalance(19000),
                    "Elite Pet x"+getPlayer().getPouch().checkBalance(19002),
                    "Next");
            case 2 -> sendOption("Withdraw",
                    "VIP Tickets x"+getPlayer().getPouch().checkBalance(23003),
                    "Slayer Tickets x"+getPlayer().getPouch().checkBalance(5023),
                    "Perk Frag x"+getPlayer().getPouch().checkBalance(19639),
                    "Close");
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
        switch(getStage()){
            case 1 -> {
                switch(option){
                    case FIRST_OPTION_OF_FOUR ->
                    {end();getPlayer().getPouch().withdraw(995, getPlayer().getPouch().checkBalance(995));}
                    case SECOND_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(10835, getPlayer().getPouch().checkBalance(10835));}
                    case THIRD_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(5020, getPlayer().getPouch().checkBalance(5020));}
                    case FOURTH_OPTION_OF_FOUR -> nextStage();
                }
            }
            case 2 -> {
                switch(option){
                    case FIRST_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(5022, getPlayer().getPouch().checkBalance(5022));}
                    case SECOND_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(19000, getPlayer().getPouch().checkBalance(19000));}
                    case THIRD_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(19002, getPlayer().getPouch().checkBalance(19002));}
                    case FOURTH_OPTION_OF_FOUR -> nextStage();
                }
            }
            case 3 -> {
                switch(option){
                    case FIRST_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(23003, getPlayer().getPouch().checkBalance(23003));}
                    case SECOND_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(5023, getPlayer().getPouch().checkBalance(5023));}
                    case THIRD_OPTION_OF_FOUR -> {end();getPlayer().getPouch().withdraw(19639, getPlayer().getPouch().checkBalance(19639));}
                    case FOURTH_OPTION_OF_FOUR -> end();
                }
            }
        }
        return false;
    }
}
