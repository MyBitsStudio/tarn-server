package com.ruse.world.content.raids;

import com.ruse.model.GameObject;
import com.ruse.model.Position;
import com.ruse.world.entity.impl.player.Player;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class RaidRoom {

    protected Raid raid;
    protected RaidNPC boss;

    protected AtomicBoolean finished = new AtomicBoolean(false), started = new AtomicBoolean(false);

    public RaidRoom(Raid raid){this.raid = raid;}

    public abstract Position playerSpawn();
    public abstract Position deathPosition();

    public abstract void onRoomStartHook();
    public abstract void onRoomFinishHook();

    public abstract RaidRoom nextRoom();

    public boolean isFinalRoom(){ return false;}

    public boolean handleObjectClick(Player player, GameObject object, int option){ return false;}

    public boolean handRoomExitObject(Player player, GameObject object, int option){ return false;}


    public void setBoss(RaidNPC boss){ this.boss = boss;}
    public RaidNPC getBoss(){ return boss;}
    public boolean isFinished(){ return finished.get();}
    public boolean isStarted(){ return started.get();}
    public void setStarted(boolean started){ this.started.set(started);}
    public void setFinished(boolean finsihed){ this.finished.set(finsihed);}
}
