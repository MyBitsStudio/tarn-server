package com.ruse.world.entity.impl.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerItems {

    public PlayerItems(){
        setDefault();
    }

    private final Map<String, Integer> charges = new ConcurrentHashMap<>();

    private void setDefault(){
        charges.put("play-monic", 0);
        charges.put("gold-monic", 0);
    }

    public Map<String, Integer> getCharges() {
        return charges;
    }

    public void setCharges(Map<String, Integer> charges){
        this.charges.putAll(charges);
    }

    public int getCharges(String name){
        return charges.getOrDefault(name, 0);
    }

    public void useCharge(String name){
        charges.put(name, charges.getOrDefault(name, 0) - 1);
    }

    public void addCharge(String name, int amount){
        charges.put(name, charges.getOrDefault(name, 0) + amount);
    }
}
