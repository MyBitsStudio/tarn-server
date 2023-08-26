//package com.ruse.world.packages.dialogue.impl.slayer;
//
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.packages.dialogue.Dialogue;
//import com.ruse.world.packages.dialogue.DialogueExpression;
//import com.ruse.world.packages.dialogue.DialogueType;
//import org.jetbrains.annotations.NotNull;
//
//public class InviteDuo extends Dialogue {
//
//    private final Player invited;
//    public InviteDuo(Player player, @NotNull Player invited) {
//        super(player);
//        this.invited = invited;
//        player.getSlayer().setDuoInvitation(invited.getUsername());
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
//    public String[] items() {
//        return new String[0];
//    }
//
//    @Override
//    public void next(int stage) {
//        switch(stage){
//            case 0 -> sendRegularStatement(invited.getUsername() + " has invited you to form a duo Slayer team.");
//            case 1 -> sendOption("Accept?", "Accept " + invited.getUsername() + "'s invitation",
//                    "Decline " + invited.getUsername() + "'s invitation");
//        }
//    }
//
//    @Override
//    public int id() {
//        return 10;
//    }
//
//    @Override
//    public void onClose() {
//
//    }
//
//    @Override
//    public boolean handleOption(int option) {
//        switch(option){
//            case 1->{
//                getPlayer().getSlayer().handleInvitation(true);
//                end();
//                return true;
//            }
//            case 2->{
//                getPlayer().getSlayer().handleInvitation(false);
//                end();
//                return true;
//            }
//        }
//        return false;
//    }
//}
