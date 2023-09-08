package com.ruse.world.entity.impl.player;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerPoints {

    private final Map<String, Integer> points = new ConcurrentHashMap<>();

    public PlayerPoints() {
        start();
    }

    private void start(){
        points.put("voted", 0);
        points.put("donated", 0);

    }

    public void load(Map<String, Integer> points){
        this.points.putAll(points);
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
