package com.ruse.world.packages.raids;

import com.ruse.model.GameObject;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.raid.RaidPartyInvite;
import com.ruse.world.packages.mode.impl.GroupIronman;
import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RaidParty {

    protected List<Player> players = new ArrayList<>();
    protected Raid raid;
    protected RaidRoom current;
    protected Player owner;

    protected Map<Player, Integer> points = new HashMap<>();

    public RaidParty(Player player, Raid raid){
        this.raid = raid;
        setOwner(player);
    }

    private void setOwner(Player player){
        players.add(player);
        owner = player;
        player.setRaidParty(this);
    }

    public Player getOwner(){
        return owner;
    }
    public List<Player> getPlayers(){
        return players;
    }

    public abstract String key();
    public abstract boolean canJoin(Player player);
    public abstract void onJoin(Player player);

    public abstract void onLeave(Player player);

    public abstract void startRaid();

    public boolean handleStartObject(Player player, GameObject object){ return false;}

    public String getKeyWithHighestValue() {
        String maxKey = null;
        int maxValue = 0;
        for (Map.Entry<Player, Integer> entry : points.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxKey = entry.getKey().getUsername();
                maxValue = entry.getValue();
            }
        }
        return maxKey;
    }

    public void invite(Player player){
        if(players.size() < 10){
            if(players.contains(player) && players.get(0) != player){
                owner.sendMessage("This player is already in this party.");
                player.sendMessage("You are already in this party.");
                return;
            }
            if(player.getRaidParty() != null){
                owner.sendMessage("This player is already in a party.");
                player.sendMessage("You are already in a party.");
                return;
            }
            if(player.busy()){
                owner.sendMessage("This player is busy.");
                player.sendMessage("You are busy.");
                return;
            }

            if(owner.getMode() instanceof GroupIronman){
                if(!owner.getIronmanGroup().equals(player.getIronmanGroup())){
                    owner.sendMessage("You are not a part of this players ironman group.");
                    player.sendMessage("You are not a part of this players ironman group.");
                    return;
                }
            }

            if(player.getMode() instanceof GroupIronman){
                if(!player.getIronmanGroup().equals(owner.getIronmanGroup())){
                    owner.sendMessage("You are not a part of this players ironman group.");
                    player.sendMessage("You are not a part of this players ironman group.");
                    return;
                }
            }

//            if (owner.getGameMode() == GameMode.GROUP_IRONMAN && !owner.getIronmanGroup().equals(player.getIronmanGroup())){
//                owner.sendMessage("You are not a part of this players ironman group.");
//                player.sendMessage("You are not a part of this players ironman group.");
//                return;
//            }
//            if (player.getGameMode() == GameMode.GROUP_IRONMAN && !player.getIronmanGroup().equals(owner.getIronmanGroup())){
//                owner.sendMessage("You are not a part of this players ironman group.");
//                player.sendMessage("You are not a part of this players ironman group.");
//                return;
//            }

            DialogueManager.sendDialogue(player, new RaidPartyInvite(player, this), -1);
            owner.sendMessage("You have invited "+player.getUsername()+" to join your party.");

        } else {
            player.sendMessage("This party is full.");
        }
    }

    public void addPoints(Player player, int points){
        if(this.points.containsKey(player)){
            this.points.put(player, this.points.get(player) + points);
        } else {
            this.points.put(player, points);
        }
        player.sendMessage("You now have "+this.points.get(player)+" points");
    }

    public void add(Player player){
        if(players.size() < 10){
            if(players.contains(player) && players.get(0) != player){
                player.sendMessage("You are already in this party.");
                return;
            }
            if(players.get(0) != player) {
                players.add(player);
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

            player.getPacketSender().sendMessage("<col=660000>You've left the Raids party.");
            player.setRaidParty(null);
        }
    }

    private void dispose(){
        raid.dispose();
    }

    public void refreshInterface(){
        for (Player member : players) {
            if (member != null) {
                member.getPacketSender().sendString(111709, "Invite");

                int start = 111716;
                for (Player player : players) {
                    member.getPacketSender().sendString(start++, player.getUsername());
                    member.getPacketSender().sendString(start++,
                            String.valueOf(player.getSkillManager().getTotalLevel()));
                    member.getPacketSender().sendString(start++,
                            String.valueOf(player.getSkillManager().getCombatLevel()));
                }

                for (int i = start; i < 111737; i++) {
                    member.getPacketSender().sendString(start++, "---");
                    member.getPacketSender().sendString(start++, "--");
                    member.getPacketSender().sendString(start++, "-");
                }

                member.getPacketSender().sendString(111702, "Raiding Party: @whi@" + players.size());
            }
        }
    }

    public void sendMessage(String message) {
        for (Player member : players) {
            if (member != null) {
                member.getPacketSender().sendMessage(message);
            }
        }
    }

}
