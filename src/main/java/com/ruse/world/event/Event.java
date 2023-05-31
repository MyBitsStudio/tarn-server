package com.ruse.world.event;

import com.ruse.model.GameObject;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import javax.xml.stream.Location;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Event {

    @Getter
    protected final AtomicBoolean running = new AtomicBoolean(false);

    @Getter
    protected Player host;
    @Getter
    protected Location location;

    @Getter
    protected final List<GameObject> objects = new CopyOnWriteArrayList<>();
    @Getter
    protected final List<NPC> npcs = new CopyOnWriteArrayList<>();

    public Event(Player player, Location loc){
        this.host = player;
        this.location = loc;
    }

    public abstract void start();

    public abstract void stop();
    public abstract String key();
    public abstract void moveTo(Player player);
}
