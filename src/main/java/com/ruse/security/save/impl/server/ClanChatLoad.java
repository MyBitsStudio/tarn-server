package com.ruse.security.save.impl.server;

import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.clans.Clan;
import com.ruse.world.packages.clans.ClanManager;

import java.util.List;

public class ClanChatLoad extends SecureLoad {

    private final ClanManager manager;
    public ClanChatLoad(ClanManager manager){
        this.manager = manager;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public ClanChatLoad run() {
        String name = object.get("name").getAsString();
        List<String> banned = builder.fromJson(object.get("banned"),
                new com.google.gson.reflect.TypeToken<List<String>>() {
                }.getType());
        manager.addChat(new Clan(name, banned));
        return this;
    }
}
