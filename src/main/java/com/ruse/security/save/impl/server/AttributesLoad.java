package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureLoad;
import com.ruse.world.World;
import com.ruse.world.packages.globals.GlobalBossManager;

import java.util.HashMap;

public class AttributesLoad extends SecureLoad {

    public AttributesLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public AttributesLoad run() {
        HashMap<String, Boolean> banned = builder.fromJson(object.get("attributes"),
                new com.google.gson.reflect.TypeToken<HashMap<String,Boolean>>() {
                }.getType());
        World.attributes.load(banned);
        return this;
    }
}
