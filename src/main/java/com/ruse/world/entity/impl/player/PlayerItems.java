package com.ruse.world.entity.impl.player;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerItems {

    public PlayerItems(){
        setDefault();
    }

    private final Map<String, Integer> charges = new ConcurrentHashMap<>();

    private void setDefault(){
        charges.put("ancient-monic", 0);
        charges.put("crystal-monic", 0);
        charges.put("starter-pot", 50);
    }

    public void setCharges(Map<String, Integer> charges){
        this.charges.putAll(charges);
    }

    public int getCharges(String name){
        return charges.get(name);
    }

    public void useCharge(String name){
        charges.put(name, charges.get(name) - 1);
    }

    public void useCharge(String name, int amount){
        charges.put(name, charges.get(name) - amount);
    }

    public void addCharge(String name, int amount){
        charges.put(name, charges.get(name) + amount);
    }
}
