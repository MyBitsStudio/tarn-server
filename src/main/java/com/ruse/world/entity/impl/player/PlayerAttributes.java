package com.ruse.world.entity.impl.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Corrupt
 * @Since 04/27/2023
 *
 * Player Attributes are a class to hold temporary attributes and be able to access them
 * These attributes are mainly used for raids, temporary blocks, and other features
 */
public class PlayerAttributes {

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private final Player player;

    public PlayerAttributes(Player player){
        this.player = player;
    }

    public void setAttribute(String key, Object value){
        if(attributes.containsKey(key))
            attributes.replace(key, value);
         else
            attributes.put(key, value);
    }

    public String getStringValue(String key){
        return (String) attributes.get(key);
    }

    public int getIntValue(String key){
        return (int) attributes.get(key);
    }

    public boolean getBooleanValue(String key){
        return (boolean) attributes.get(key);
    }

    public Object getValue(String key){
        return attributes.get(key);
    }

}
