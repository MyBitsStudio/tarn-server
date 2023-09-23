package com.ruse.world.packages.raid;

import com.ruse.model.GameObject;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.raid.party.RaidParty;
import com.ruse.world.packages.raid.room.RaidRoom;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
public abstract class Raid {
    protected Map<Integer, RaidRoom> levels;
    protected RaidRoom current;
    protected RaidParty party;
    protected int level = -1;

    protected AtomicBoolean started = new AtomicBoolean(false);
    protected AtomicBoolean finished = new AtomicBoolean(false);

    public Raid(){
    }

    public void setParty(RaidParty party){
        this.party = party;
    }

    public abstract void start();
    public abstract void rewardPlayer(Player player);
    public abstract boolean handleObject(Player player, GameObject object, int option);
    public abstract boolean handleDeath(Player player);
    public void nextRoom(){
        if(current == null || party == null) {
            if(current == null)
                System.out.println("Current room is null");
            if(party == null)
                System.out.println("Party is null");
        } else if (current.getFinished().get()) {
            current.onRoomEndHook();
            if (current.isFinal()) {
                party.getPlayers().forEach(p -> p.sendMessage("You have finished the raid. You can now loot your rewards."));
                finished.set(true);
            }
            RaidRoom next = levels.get(++level);
            if (next != null) {
                current = next;
            }
            current.setRaid(this);
            current.onRoomStartHook();
            current.getStarted().set(true);
            party.getPlayers().forEach(p -> p.moveTo(current.startPosition()));
        }
    }
}
