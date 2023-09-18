package com.ruse.world.packages.dialogue.impl.raid;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;
import com.ruse.world.packages.raid.party.RaidParty;

public class RaidPartyInvite extends Dialogue {

    private final RaidParty party;
    public RaidPartyInvite(Player player, RaidParty party) {
        super(player);
        this.party = party;
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
            case 0 -> sendRegularStatement(party.getOwner().getUsername() + " has invited you to join their Raids party.");
            case 1-> sendOption("Join the party?", "Join " + party.getOwner().getUsername() + "'s party", "Don't join " + party.getOwner().getUsername() + "'s party.");
        }
    }

    @Override
    public int id() {
        return 11;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        switch(option){
            case 1->{
                getPlayer().getPacketSender().sendInterfaceRemoval();
                party.add(getPlayer());
                return true;
            }
            case 2->{
                //getPlayer().setInviteParty(null);
                end();
                return true;
            }
        }
        return false;
    }
}
