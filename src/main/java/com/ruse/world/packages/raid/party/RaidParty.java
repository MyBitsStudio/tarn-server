package com.ruse.world.packages.raid.party;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.raid.RaidPartyInvite;
import com.ruse.world.packages.raid.Raid;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class RaidParty {

    private final List<Player> players = new CopyOnWriteArrayList<>();

    private final PartyAttributes attributes;
    private Raid raid;
    private Player owner;

    public RaidParty(Player player, PartyAttributes attributes){
        owner = player;
        this.attributes = attributes;
        players.add(player);
        owner.getRaidPlugin().reset();
    }

    public void invite(Player player){
        if(players.size() < attributes.getMaxPlayers()){
            if(players.contains(player) && players.get(0) != player){
                owner.sendMessage("This player is already in this party.");
                player.sendMessage("You are already in this party.");
                return;
            }

            if(player.busy()){
                owner.sendMessage("This player is busy.");
                player.sendMessage("You are busy.");
                return;
            }

            DialogueManager.sendDialogue(player, new RaidPartyInvite(player, this), -1);
            owner.sendMessage("You have invited "+player.getUsername()+" to join your party.");
        }
    }

    public void add(Player player){
        if(players.size() < attributes.getMaxPlayers()){
            if(players.contains(player) && players.get(0) != player){
                player.sendMessage("You are already in this party.");
                return;
            }
            if(players.get(0) != player) {
                players.add(player);
                player.getRaidPlugin().reset();
                player.getPacketSender().sendMessage("<col=660000>You've created a Raids party.");

                player.setRaidParty(this);
            }
        } else {
            player.sendMessage("This party is full.");
        }
    }

    public void remove(Player player){
        if(players.contains(player)){
            player.getPacketSender().sendCameraNeutrality();
            player.getPacketSender().sendInterfaceRemoval();
            player.getPacketSender().sendString(111709, "Create");
            int id = 111716;
            for (int i = 111716; i < 111737; i++) {
                player.getPacketSender().sendString(id++, "---");
                player.getPacketSender().sendString(id++, "--");
                player.getPacketSender().sendString(id++, "-");
            }
            player.getPacketSender().sendString(111702, "Raiding Party: @whi@0");

            if(player == players.get(0)){
                if (players.size() >= 2) {
                    players.get(1).getPacketSender().sendMessage("<col=660000>You've been promoted to party leader.");
                    players.get(1).getPacketSender().sendString(111709, "Invite");
                    owner = players.get(1);
                } else {
                    dispose();
                    return;
                }
            }

            players.remove(player);
            player.getRaidPlugin().reset();

            player.getPacketSender().sendMessage("<col=660000>You've left the Raids party.");
            player.setRaidParty(null);
        }
    }

    public void dispose(){

    }

    public void clearInterface(Player p) {
        for (int i = 0; i < 5; i++) {
            p.getPacketSender().sendString(26235 + i, "");
            p.getPacketSender().sendString(27235 + i, "");
        }
    }

    public void refreshInterface(){
        for (Player member : players) {
            if (member != null) {

                clearInterface(member);

                for (int i = 0; i < getPlayers().size(); i++) {
                    Player p = getPlayers().get(i);

                    if (p != null) {
                        member.getPacketSender().sendString(26235 + i, p.getUsername());
                        member.getPacketSender().sendString(27235 + i, p.getUsername());
                    }
                }
//                member.getPacketSender().sendString(111709, "Invite");
//
//                int start = 111716;
//                for (Player player : players) {
//                    member.getPacketSender().sendString(start++, player.getUsername());
//                    member.getPacketSender().sendString(start++,
//                            String.valueOf(player.getSkillManager().getTotalLevel()));
//                    member.getPacketSender().sendString(start++,
//                            String.valueOf(player.getSkillManager().getCombatLevel()));
//                }
//
//                for (int i = start; i < 111737; i++) {
//                    member.getPacketSender().sendString(start++, "---");
//                    member.getPacketSender().sendString(start++, "--");
//                    member.getPacketSender().sendString(start++, "-");
//                }
//
//                member.getPacketSender().sendString(111702, "Raiding Party: @whi@" + players.size());
            }
        }
    }

    public void sendMessage(String message){
        for (Player member : players) {
            if (member != null) {
                member.getPacketSender().sendMessage(message);
            }
        }
    }

    public void startRaid(){
        raid = attributes.getRaid();
        if(raid == null){
            dispose();
            return;
        }

        raid.setParty(this);

        for(Player player : players){
            player.setRaid(raid);

            player.getPacketSender().sendInterfaceRemoval();
        }

        raid.start();
    }
}
