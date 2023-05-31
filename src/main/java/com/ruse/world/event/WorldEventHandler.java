package com.ruse.world.event;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldEventHandler {

    private static WorldEventHandler instance;

    public static WorldEventHandler getInstance(){
        if(instance == null)
            instance = new WorldEventHandler();
        return instance;
    }

    private final Map<String, Event> activeEvents = new ConcurrentHashMap<>();

    public void addEvent(String key, Event event){
        activeEvents.put(key, event);
    }

    public void removeEvent(String key){
        activeEvents.remove(key);
    }

    public boolean eventActive(String key){
        if(activeEvents.containsKey(key))
            return activeEvents.get(key).getRunning().get();
        return false;
    }

    public Event getEvent(String key){
        return activeEvents.get(key);
    }

    public void startEvent(Player player, @NotNull Event event){
        if(!event.getRunning().get()) {
            event.start();
            event.getRunning().set(true);
            activeEvents.put(player.getUsername(), event);
        }
    }
}
