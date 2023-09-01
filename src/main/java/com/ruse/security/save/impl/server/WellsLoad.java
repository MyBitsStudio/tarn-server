package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.packages.shops.ShopHandler;

import java.util.HashMap;

public class WellsLoad extends SecureLoad {

    public WellsLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public WellsLoad run() {
        HashMap<String, Integer> banned = builder.fromJson(object.get("wells"),
                new com.google.gson.reflect.TypeToken<HashMap<String, Integer>>() {
                }.getType());
        GlobalBossManager.getInstance().load(banned);
        return this;
    }
}
