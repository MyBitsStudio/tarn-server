//package com.ruse.world.packages.dialogue.impl.slayer;
//
//import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.packages.dialogue.Dialogue;
//import com.ruse.world.packages.dialogue.DialogueExpression;
//import com.ruse.world.packages.dialogue.DialogueType;
//
//public class ReceivedTask extends Dialogue {
//
//    final SlayerTasks task;
//    public ReceivedTask(Player player, SlayerTasks task) {
//        super(player);
//        this.task = task;
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
//            case 0 -> {
//                boolean duoSlayer = getPlayer().getSlayer().getDuoPartner() != null;
//                int amountToKill = getPlayer().getSlayer().getAmountToSlay();
//                String you = duoSlayer ? "You and your partner" : "You";
//                String line1 = "You have been assigned to kill " + amountToKill + " "
//                        + task.getName() + "s.";
//                String line2 = "";
//                if (duoSlayer) {
//                    line1 = you + " have been assigned to kill";
//                    line2 = amountToKill + " "
//                            + task.getName() + "s.";
//                }
//                if (getPlayer().getSlayer().getLastTask() != SlayerTasks.NO_TASK) {
//                    line1 = you + " are doing great! Your new";
//                    line2 = "assignment is to kill " + amountToKill + " "
//                            + task.getName() + "s.";
//                }
//                sendNpcChat(line1, line2);
//            }
//            case 1-> end();
//        }
//
//    }
//
//    @Override
//    public int id() {
//        return 6;
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
