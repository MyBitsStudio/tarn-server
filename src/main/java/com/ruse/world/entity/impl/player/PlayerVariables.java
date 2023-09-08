package com.ruse.world.entity.impl.player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerVariables {

    private final Map<String, Object> settings = new ConcurrentHashMap<>();

    private final Player player;

    public PlayerVariables(Player player){
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
        settings.put("interface-settings", new String[6]);
        settings.put("active-shop", Integer.parseInt("-1"));
        settings.put("active-tab", Integer.parseInt("-1"));

        settings.put("slot-chosen", Integer.parseInt("-1"));
        settings.put("inv-slot", Integer.parseInt("-1"));

        settings.put("perk-chosen", Integer.parseInt("-1"));
        settings.put("monic-chosen", Integer.parseInt("-1"));

        settings.put("double-dr", false);
        settings.put("double-ddr", false);
        settings.put("double-damage", false);
        settings.put("vote-xp", false);
        settings.put("vote-dr", false);

        settings.put("monic-xp", false);
        settings.put("monic-dr", false);
        settings.put("monic-ddr", false);
        settings.put("monic-damage", false);
        settings.put("monic-prayer", false);

        settings.put("item-chosen", Integer.parseInt("-1"));

        settings.put("last-button", System.currentTimeMillis());
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

    public Long getLongValue(String key){
        return (Long) settings.getOrDefault(key, Long.valueOf("-1"));
    }

    public void setLongValue(String key, Long value) {
    	settings.put(key, value);
    }
    public boolean getBooleanValueDef(String key){
        return (boolean) settings.getOrDefault(key, true);
    }

    public Object getValue(String key){
        return settings.get(key);
    }

    public String[] getInterfaceSettings() {
    	return (String[]) settings.getOrDefault("interface-settings", new String[6]);
    }

    public void setInterfaceSettings(int index, String put) {
    	String[] settings = getInterfaceSettings();
    	settings[index] = put;
    	setSetting("interface-settings", settings);
    }
}
