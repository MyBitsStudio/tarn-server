//package com.ruse.world.content.raids.dialogue;
//
//import com.ruse.world.content.dialogue.Dialogue;
//import com.ruse.world.content.dialogue.DialogueExpression;
//import com.ruse.world.content.dialogue.DialogueType;
//import com.ruse.world.content.raids.RaidParty;
//import com.ruse.world.entity.impl.player.Player;
//
//public class RaidPartyInvite extends Dialogue {
//
//    private RaidParty party;
//    private Player player;
//    public RaidPartyInvite(RaidParty party, Player player){
//        this.party = party;
//        this.player = player;
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
//        return new String[]{party.getOwner().getUsername() + " has invited you to join their Raids party."};
//    }
//
//    @Override
//    public Dialogue nextDialogue() {
//        return new Dialogue() {
//
//            @Override
//            public DialogueType type() {
//                return DialogueType.OPTION;
//            }
//
//            @Override
//            public DialogueExpression animation() {
//                return null;
//            }
//
//            @Override
//            public String[] dialogue() {
//                return new String[]{"Join " + party.getOwner().getUsername() + "'s party", "Don't join " + party.getOwner().getUsername() + "'s party."};
//            }
//
//            @Override
//            public void specialAction() {
//                player.setInviteParty(party);
//                player.setDialogueActionId(6666);
//            }
//
//        };
//    }
//}
