package com.ruse.security.save.impl.server;

import com.ruse.engine.GameEngine;
import com.ruse.security.ServerSecurity;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMapsSave extends SecureSave {

    private final ServerSecurity sec;
    public ServerMapsSave(ServerSecurity sec){
        this.sec = sec;
    }

    @Override
    public ServerMapsSave create() {
        object.add("ipMap", builder.toJsonTree(sec.getIpMap()));
        object.add("macMap", builder.toJsonTree(sec.getMacMap()));
        object.add("hwidMap", builder.toJsonTree(sec.getHwidMap()));
        object.add("flagged", builder.toJsonTree(sec.getFlagged()));
        return this;
    }

    @Override
    public void save() {
        GameEngine.submit(() -> {
            try (FileWriter file = new FileWriter(SecurityUtils.SERVER_MAPS)) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[1];
    }
}
