package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureLoad;
import com.ruse.world.World;
import com.ruse.world.packages.globals.GlobalBossManager;

import java.util.HashMap;
import java.util.Map;

public class AttributesLoad extends SecureLoad {

    public AttributesLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public AttributesLoad run() {
        Map<String, Boolean> banned = builder.fromJson(object.get("attributes"),
                new com.google.gson.reflect.TypeToken<Map<String,Boolean>>() {
                }.getType());
        Map<String, Integer> amounts = builder.fromJson(object.get("amounts"),
                new com.google.gson.reflect.TypeToken<Map<String,Integer>>() {
                }.getType());
        World.attributes.load(banned, amounts);
        return this;
    }
}
