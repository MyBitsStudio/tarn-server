//package com.ruse.world.content.tradingpost.dialogues;
//
//import com.ruse.world.content.dialogue.Dialogue;
//import com.ruse.world.content.dialogue.DialogueExpression;
//import com.ruse.world.content.dialogue.DialogueType;
//import com.ruse.world.entity.impl.player.Player;
//
//public class CancelOptions extends Dialogue {
//
//    private final Player player;
//    private final String itemName;
//    private final int slot;
//
//    public CancelOptions(Player player, String itemName, int slot) {
//        this.player = player;
//        this.itemName = itemName;
//        this.slot = slot;
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
//        return new String[] {
//                "Cancel @red@" + itemName + "@bla@?",
//                "Nevermind..."
//        };
//    }
//
//    @Override
//    public void specialAction() {
//        player.setDialogueActionId(753);
//    }
//
//    public int getSlot() {
//        return slot;
//    }
//}
