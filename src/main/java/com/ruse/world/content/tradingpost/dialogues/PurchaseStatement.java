//package com.ruse.world.content.tradingpost.dialogues;
//
//import com.ruse.model.definitions.ItemDefinition;
//import com.ruse.util.Misc;
//import com.ruse.world.content.dialogue.Dialogue;
//import com.ruse.world.content.dialogue.DialogueExpression;
//import com.ruse.world.content.dialogue.DialogueType;
//import com.ruse.world.content.tradingpost.TradingPost;
//import com.ruse.world.content.tradingpost.models.Offer;
//import com.ruse.world.entity.impl.player.Player;
//
//public class PurchaseStatement extends Dialogue {
//    private final Player player;
//    private final Offer offer;
//    private final int amount;
//
//    public PurchaseStatement(Player player, Offer offer, int amount) {
//        this.player = player;
//        this.offer = offer;
//        this.amount = amount;
//    }
//
//    @Override
//    public DialogueType type() {
//        return DialogueType.STATEMENT;
//    }
//
//    @Override
//    public DialogueExpression animation() {
//        return null;
//    }
//
//    @Override
//    public String[] dialogue() {
//        return new String[] {"Purchase " + amount + "x " + ItemDefinition.forId(offer.getItemId()).getName() + " for @blu@" + Misc.formatNumber((long) offer.getPrice() *amount) + "@bla@ " + ItemDefinition.forId(TradingPost.CURRENCY_ID).getName() + "s?"};
//    }
//
//    @Override
//    public Dialogue nextDialogue() {
//        return new PurchaseOptions(player, offer, amount);
//    }
//}
