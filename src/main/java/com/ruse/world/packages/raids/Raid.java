package com.ruse.world.packages.raids;

import com.ruse.model.GameObject;
import com.ruse.world.entity.impl.player.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author @Corrupt
 * @Project Azura
 * @Date 04/21/2023
 *
 * Raids abstract class
 * Overlaps each raid and holds viable information and functions to conducting each raid
 */
//2867, 2763
//2923, 2763
public abstract class Raid {

    protected RaidParty party;
    protected RaidRoom current;
    protected RaidRewards rewards;

    protected List<GameObject> objects = new CopyOnWriteArrayList<>();

    protected AtomicBoolean started = new AtomicBoolean(false);
    protected AtomicBoolean finished = new AtomicBoolean(false);


    public Raid() {
    }

    public abstract void start(RaidParty party);
    public abstract void rewardPlayer(Player player);
    public abstract void dispose();

    public abstract boolean handleObject(Player player, GameObject object, int option);

    public abstract boolean handleDeath(Player player);

    public void setFinished(boolean finished){
        this.finished.set(finished);
    }

    public boolean isFinished(){
        return finished.get();
    }

    public void setStarted(boolean started){
        this.started.set(started);
    }

    public boolean isStarted(){
        return started.get();
    }

    public void setParty(RaidParty party){
        this.party = party;
    }

    public RaidParty getParty(){
        return party;
    }

    public void setRewards(RaidRewards rewards){
        this.rewards = rewards;
    }

    public RaidRewards getRewards(){
        return rewards;
    }

    public RaidRoom getCurrentRoom(){
        return current;
    }

    public boolean canAOE(){ return false;}

    public boolean handleObjectClicks(Player player, GameObject object, int option){
        if(current != null && current.handRoomExitObject(player, object, option)){
            return true;
        }
        if(current != null && current.handleObjectClick(player, object, option)){
            return true;
        }
        return handleObject(player, object, option);
    }

    public void startNextRoom(){
        if(current == null || party == null) {
            if(current == null)
                System.out.println("Current room is null");
            if(party == null)
                System.out.println("Party is null");
        } else if (current.isFinished()) {
            current.onRoomFinishHook();
            if (current.isFinalRoom()) {
                party.players.forEach(p -> p.sendMessage("You have finished the raid. You can now loot your rewards."));
                setFinished(true);
            }
            RaidRoom next = current.nextRoom();
            if (next != null) {
                current = next;
            }
            current.onRoomStartHook();
            party.players.forEach(p -> p.moveTo(current.playerSpawn()));
        }
    }
}
