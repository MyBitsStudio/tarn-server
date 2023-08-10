package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.clans.Clan;
import com.ruse.world.packages.clans.ClanManager;
import com.ruse.world.packages.shops.ShopHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JunkPriceLoad extends SecureLoad {

    public JunkPriceLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public JunkPriceLoad run() {
        HashMap<Integer, Integer> banned = builder.fromJson(object.get("prices"),
                new com.google.gson.reflect.TypeToken<HashMap<Integer, Integer>>() {
                }.getType());
        ShopHandler.junkPrices.putAll(banned);
        return this;
    }
}
