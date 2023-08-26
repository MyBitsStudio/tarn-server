//package com.ruse.world.packages.dialogue.impl.slayer;
//
//import com.ruse.world.content.skill.impl.slayer.SlayerMaster;
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.packages.dialogue.Dialogue;
//import com.ruse.world.packages.dialogue.DialogueExpression;
//import com.ruse.world.packages.dialogue.DialogueType;
//
//public class TotalPoints extends Dialogue {
//
//    private final SlayerMaster master;
//    public TotalPoints(Player player, SlayerMaster master) {
//        super(player);
//        this.master = master;
//    }
//
//    @Override
//    public DialogueType type() {
//        return DialogueType.NPC_STATEMENT;
//    }
//
//    @Override
//    public DialogueExpression animation() {
//        return DialogueExpression.NORMAL;
//    }
//
//    @Override
//    public String[] items() {
//        return new String[0];
//    }
//
//    @Override
//    public void next(int stage) {
//        switch(stage){
//            case 0-> {
//                int pointsReceived = switch (master) {
//                    case MEDIUM_SLAYER -> 5;
//                    case HARD_SLAYER -> 7;
//                    case ELITE_SLAYER -> 11;
//                    default -> 2;
//                };
//                int per5 = pointsReceived * 3;
//                int per10 = pointsReceived * 5;
//                sendNpcChat("You currently receive " + pointsReceived + " points per task,",
//                        per5 + " bonus points per 5 task-streak and",
//                        per10 + " bonus points per 10 task-streak.");
//            }
//            case 1-> end();
//        }
//
//
//    }
//
//    @Override
//    public int id() {
//        return 9;
//    }
//
//    @Override
//    public void onClose() {
//
//    }
//
//    @Override
//    public boolean handleOption(int option) {
//        return false;
//    }
//}
