package com.ruse.security.save.impl.server;

import com.ruse.engine.GameEngine;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.WorldIPChecker;
import com.ruse.world.packages.globals.GlobalBossManager;

import java.io.FileWriter;
import java.io.IOException;

public class WorldIPSave extends SecureSave {
    public WorldIPSave(){
        ;
    }

    @Override
    public WorldIPSave create() {
        object.add("ipLogs", builder.toJsonTree(WorldIPChecker.getInstance().getWorldIPLogs()));
        return this;
    }

    @Override
    public void save() {
        GameEngine.submit(() -> {
            try (FileWriter file = new FileWriter(SecurityUtils.IP_LOGS)) {
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
