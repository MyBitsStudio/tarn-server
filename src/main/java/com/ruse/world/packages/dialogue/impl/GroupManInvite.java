package com.ruse.world.packages.dialogue.impl;

import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.Dialogue;
import com.ruse.world.packages.dialogue.DialogueExpression;
import com.ruse.world.packages.dialogue.DialogueType;

public class GroupManInvite extends Dialogue {

    private final Player invite;
    public GroupManInvite(Player player, Player p) {
        super(player);
        this.invite = p;
    }

    @Override
    public DialogueType type() {
        return null;
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
            case 0 -> sendRegularStatement(invite.getUsername() + " has invited you to join their Group Ironman group.");
            case 1 -> sendOption("Would you like to join?","Join " + invite.getUsername() + "'s group", "Don't join " + invite.getUsername() + "'s group.");
        }
    }

    @Override
    public int id() {
        return 4;
    }

    @Override
    public void onClose() {

    }

    @Override
    public boolean handleOption(int option) {
        if(option == FIRST_OPTION_OF_TWO){
            getPlayer().getPacketSender().sendInterfaceRemoval();
            if (getPlayer().getGroupInvitationId() != -1 && GroupManager.getGroup(getPlayer().getGroupInvitationId()) != null) {
                assert GroupManager.getGroup(getPlayer().getGroupInvitationId()) != null;
                GroupManager.getGroup(getPlayer().getGroupInvitationId()).addPlayer(getPlayer());
            }
            if (GroupManager.isInGroup(getPlayer())) {
                GroupManager.openInterface(getPlayer());
            }
            if (getPlayer().getIronmanGroup().getOwner() != null) {
                getPlayer().getIronmanGroup().getOwner().sendMessage("@blu@" + getPlayer().getUsername() + " has joined your Ironman group!");
                GroupManager.openInterface(getPlayer().getIronmanGroup().getOwner());
            }
            getPlayer().setGroupInvitationId(-1);
            return true;
        } else if(option == SECOND_OPTION_OF_TWO){
            end();
            return true;
        }
        return false;
    }
}
