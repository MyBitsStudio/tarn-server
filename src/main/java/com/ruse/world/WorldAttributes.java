package com.ruse.world;

import com.ruse.security.save.impl.server.AttributesLoad;
import com.ruse.security.save.impl.server.AttributesSave;
import com.ruse.security.tools.SecurityUtils;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldAttributes {

    @Getter
    private Map<String, Boolean> settings = new ConcurrentHashMap<>();

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

        settings.put("donator-bonus", false);
        settings.put("slayer-bonus", false);
        settings.put("double-drops", false);
        settings.put("vote-bonus", false);
    }

    public boolean getSetting(String key){
        return settings.get(key);
    }

    public void save(){
        new AttributesSave().create().save();
    }

    public void load(){
        new AttributesLoad().loadJSON(SecurityUtils.ATTRIBUTES).run();
    }

    public void load(Map<String, Boolean> settings){
        this.settings.putAll(settings);
    }

    public void setSetting(String key, boolean value){
        if(settings.containsKey(key))
            settings.replace(key, value);
        save();
    }
}
