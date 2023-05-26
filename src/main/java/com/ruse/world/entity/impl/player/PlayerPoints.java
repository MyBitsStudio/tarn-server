package com.ruse.world.entity.impl.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerPoints {

    private final Player player;

    private final Map<String, Integer> points = new ConcurrentHashMap<>();

    public PlayerPoints(Player player) {
        this.player = player;
        start();
    }

    private void start(){
        points.put("single-boss", 0);
    }

    public void add(String name, int amount) {
        points.put(name, get(name) + amount);
    }

    public void remove(String name, int amount) {
        points.put(name, get(name) - amount);
    }

    public int get(String name) {
        return points.getOrDefault(name, 0);
    }
}
