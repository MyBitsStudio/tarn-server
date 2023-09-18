package com.ruse.world.packages.raid;

import com.ruse.model.GameObject;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.raid.party.RaidParty;
import com.ruse.world.packages.raid.room.RaidRoom;

import java.util.Map;

public abstract class Raid {
    protected Map<Integer, RaidRoom> levels;
    protected RaidRoom current;
    protected RaidParty party;

    public Raid(){
    }

    public void setParty(RaidParty party){
        this.party = party;
    }

    public abstract void start();
    public abstract boolean handleObject(Player player, GameObject object, int option);
    public abstract boolean handleDeath(Player player);
    public void nextRoom(){

    }
}
