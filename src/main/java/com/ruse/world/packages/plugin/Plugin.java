package com.ruse.world.packages.plugin;

import com.ruse.world.entity.impl.player.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Plugin {

    protected Player player;
    protected Map<String, Map<String, Long>> vars;
    protected Map<String, Map<String, String>> settings;


    public Plugin(Player player){
        this.player = player;
        defaultVars();
    }

    public Plugin(Player player, Map<String, Map<String, Long>> variables, Map<String, Map<String, String>> settings){
        this.player = player;
        this.vars = variables;
        this.settings = settings;
    }

    private void defaultVars(){
        this.vars = new LinkedHashMap<>();
        this.settings = new LinkedHashMap<>();
        vars.put("damage", new LinkedHashMap<>());
        vars.put("count", new LinkedHashMap<>());
        vars.put("misc", new LinkedHashMap<>());
    }


    public long getDamage(String name){
        return vars.get("damage").getOrDefault(name, 0L);
    }

    public long getCount(String name){
        return vars.get("count").getOrDefault(name, 0L);
    }

    public void addDamage(String name, long damage){
        if(vars.get("damage").containsKey(name))
            vars.get("damage").replace(name, vars.get("damage").get(name) + damage);
        else
            vars.get("damage").put(name, damage);
    }

    public void addCount(String name, long count){
        if(vars.get("count").containsKey(name))
            vars.get("count").replace(name, vars.get("count").get(name) + count);
        else
            vars.get("count").put(name, count);
    }

    public void setCount(String name, long count){
        if(vars.get("count").containsKey(name))
            vars.get("count").replace(name, count);
        else
            vars.get("count").put(name, count);
    }

    public void takeCount(String name, long count){
        if(vars.get("count").containsKey(name))
            vars.get("count").replace(name, vars.get("count").get(name) - count);
        else
            vars.get("count").put(name, count);
    }

    public void setDamage(String name, long damage){
        if(vars.get("damage").containsKey(name))
            vars.get("damage").replace(name, damage);
        else
            vars.get("damage").put(name, damage);
    }

    public void clearDamageMap(){
        vars.get("damage").clear();
    }

    public void clearDamage(String name){
        vars.get("damage").remove(name);
    }

    public long getMisc(String name){
        return vars.get("misc").getOrDefault(name, 0L);
    }

    public void setMisc(String name, long value){
        if(vars.get("misc").containsKey(name))
            vars.get("misc").replace(name, value);
        else
            vars.get("misc").put(name, value);
    }

}
