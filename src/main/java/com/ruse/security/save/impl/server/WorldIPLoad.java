package com.ruse.security.save.impl.server;

import com.ruse.model.WorldIPLog;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.packages.globals.GlobalBossManager;

import java.util.HashMap;
import java.util.List;

public class WorldIPLoad extends SecureLoad {

    public WorldIPLoad(){
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public WorldIPLoad run() {
        List<WorldIPLog> banned = builder.fromJson(object.get("ipLogs"),
                new com.google.gson.reflect.TypeToken<List<WorldIPLog>>() {
                }.getType());
        WorldIPChecker.getInstance().load(banned);
        return this;
    }
}
