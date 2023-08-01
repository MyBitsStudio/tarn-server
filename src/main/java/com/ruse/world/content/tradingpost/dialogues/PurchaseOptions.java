//package com.ruse.world.content.tradingpost.dialogues;
//
//import com.ruse.world.content.dialogue.Dialogue;
//import com.ruse.world.content.dialogue.DialogueExpression;
//import com.ruse.world.content.dialogue.DialogueType;
//import com.ruse.world.content.tradingpost.models.Offer;
//import com.ruse.world.entity.impl.player.Player;
//
//public class PurchaseOptions extends Dialogue {
//    private final Player player;
//    private final Offer offer;
//    private final int amount;
//
//    public PurchaseOptions(Player player, Offer offer, int amount) {
//        this.player = player;
//        this.offer = offer;
//        this.amount = amount;
//    }
//
//    @Override
//    public DialogueType type() {
//        return DialogueType.OPTION;
//    }
//
//    @Override
//    public DialogueExpression animation() {
//        return null;
//    }
//
//    @Override
//    public String[] dialogue() {
//        return new String[] {"Yes","No"};
//    }
//    @Override
//    public void specialAction() {
//        player.setDialogueActionId(754);
//    }
//
//    public Offer getOffer() {
//        return offer;
//    }
//
//    public int getAmount() {
//        return amount;
//    }
//}
