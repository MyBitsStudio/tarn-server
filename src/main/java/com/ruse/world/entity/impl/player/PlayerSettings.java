package com.ruse.world.entity.impl.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerSettings {

    private final Map<String, Object> settings = new ConcurrentHashMap<>();

    private final Player player;

    public PlayerSettings(Player player){
        this.player = player;
        defaultSettings();
    }

    public Map<String, Object> getSettings() {
    	return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings.putAll(settings);
    }

    private void defaultSettings(){
        settings.put("drop-messages", true);
        settings.put("hidden-players", false);
        settings.put("pass-change", false);
        settings.put("is-locked", false);
        settings.put("drop-message-personal", true);
        settings.put("security-lock", true);
    }

    public void setSetting(String key, Object value){
        if(settings.containsKey(key))
            settings.replace(key, value);
        else
            settings.put(key, value);
    }

    public String getStringValue(String key){
        return (String) settings.get(key);
    }

    public int getIntValue(String key){
        return (int) settings.getOrDefault(key, -1);
    }

    public boolean getBooleanValue(String key){
        return (boolean) settings.getOrDefault(key, false);
    }

    public boolean getBooleanValueDef(String key){
        return (boolean) settings.getOrDefault(key, true);
    }

    public Object getValue(String key){
        return settings.get(key);
    }
}
