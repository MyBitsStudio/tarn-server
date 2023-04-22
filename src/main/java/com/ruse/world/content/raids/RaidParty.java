package com.ruse.world.content.raids;

import com.ruse.model.GameObject;
import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RaidParty {

    protected List<Player> players = new ArrayList<>();
    protected Raid raid;
    protected RaidRoom current;

    protected Map<Player, Integer> points = new HashMap<>();

    public RaidParty(Player player, Raid raid){
        this.raid = raid;
        players.add(player);
    }

    public abstract void start();
    public abstract String key();

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
            if(players.get(0) != player)
                players.add(player);
        }
    }

}
