package com.ruse.world;

import com.ruse.security.save.impl.server.AttributesLoad;
import com.ruse.security.save.impl.server.AttributesSave;
import com.ruse.security.tools.SecurityUtils;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WorldAttributes {

    private final Map<String, Boolean> settings = new ConcurrentHashMap<>();
    private final Map<String, Integer> amounts = new ConcurrentHashMap<>();

    public WorldAttributes(){
        defaultSettings();
    }

    private void defaultSettings(){
        settings.put("instances", true);
        settings.put("raids", true);
        settings.put("calendar", true);
        settings.put("forge", false);
        settings.put("loyalty", true);
        settings.put("afk", true);
        settings.put("dissolve", true);
        settings.put("globals", true);
        settings.put("pos", true);
        settings.put("battlepass", true);
        settings.put("shop", true);
        settings.put("tower", true);
        settings.put("collection", false);
        settings.put("trading", false);
        settings.put("achievements", true);
        settings.put("refer", true);

        settings.put("donator-bonus", false);
        settings.put("slayer-bonus", false);
        settings.put("double-drops", false);
        settings.put("vote-bonus", false);


        amounts.put("donation-boss", 0);
        amounts.put("vote-boss", 0);
    }

    public boolean getSetting(String key){
        return settings.get(key);
    }

    public int getAmount(String key) {
    	return amounts.get(key);
    }

    public void save(){
        new AttributesSave().create().save();
    }

    public void load(){
        new AttributesLoad().loadJSON(SecurityUtils.ATTRIBUTES).run();
    }

    public void load(Map<String, Boolean> settings, Map<String, Integer> amounts){
        this.settings.putAll(settings);
        this.amounts.putAll(amounts);
    }

    public void setSetting(String key, boolean value){
        if(settings.containsKey(key))
            settings.replace(key, value);
        save();
    }

    public void setAmount(String key, int value) {
    	if(amounts.containsKey(key))
    		amounts.replace(key, value);
    	save();
    }
}
