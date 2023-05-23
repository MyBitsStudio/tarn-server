package com.ruse.world.content.clans;

import com.ruse.util.Stopwatch;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Clan {

    private String owner;
    private final String name;

    private final List<Player> players = new CopyOnWriteArrayList<>();
    private final List<String> banned = new CopyOnWriteArrayList<>();

    public Clan(String name){
        this.name = name;
    }

    public Clan(String owner, String name){
        this.owner = owner;
        this.name = name;
    }

    public Clan(String name, List<String> banned){
        this.name = name;
        this.banned.addAll(banned);
    }

    public List<Player> getMembers(){
        return players;
    }

    public boolean addMember(Player player){
        if(!players.contains(player))
            players.add(player);
        return players.contains(player);
    }

    public void removeMember(Player player){
        players.remove(player);
    }

    public void banPlayer(String name){
        if(!banned.contains(name))
            banned.add(name);
    }

    public void unbanPlayer(String name){
        banned.remove(name);
    }

    public boolean isBanned(String name){
        return banned.contains(name);
    }


}
