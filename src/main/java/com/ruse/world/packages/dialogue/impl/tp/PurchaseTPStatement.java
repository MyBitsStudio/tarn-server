package com.ruse.world.packages.dialogue.impl.tp;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.util.Misc;
import com.ruse.world.packages.tradingpost.TradingPost;
import com.ruse.world.packages.tradingpost.models.Offer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class PurchaseTPStatement extends Dialogue {

    private final Offer offer;
    private final int amount;
    public PurchaseTPStatement(Player player, Offer offer, int amount) {
        super(player);
        this.offer = offer;
        this.amount = amount;
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
            case 0 -> sendRegularStatement("Purchase " + amount + "x " + ItemDefinition.forId(offer.getItemId()).getName() + " for @blu@" + Misc.formatNumber((long) offer.getPrice() *amount) + "@bla@ " + ItemDefinition.forId(TradingPost.CURRENCY_ID).getName() + "s?");
            case 1 -> sendOption("Purchase Now", "Yes", "No");
        }
    }

    @Override
    public int id() {
        return 3;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        if(option == 1){
            end();
            getPlayer().getTradingPost().purchase(offer, amount, 1);
        } else {
            end();
        }
        return true;
    }
}
