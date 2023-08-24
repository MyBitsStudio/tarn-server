package com.ruse.security.save.impl.player;

import com.ruse.engine.GameEngine;
import com.ruse.io.ThreadProgressor;
import com.ruse.security.PlayerSecurity;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerSecuritySave extends SecureSave {

    private final PlayerSecurity sec;

    public PlayerSecuritySave(PlayerSecurity sec){
        this.sec = sec;
    }
    @Override
    public PlayerSecuritySave create() {
        object.add("associations", builder.toJsonTree(sec.getAssociations()));
        object.add("securityMap", builder.toJsonTree(sec.getSecurityMap()));

        object.add("seed", builder.toJsonTree(sec.getSeed()));
        object.add("auth", builder.toJsonTree(sec.getAuth()));

        object.add("logs", builder.toJsonTree(sec.getLogins()));

        object.addProperty("fa", sec.getFA());

        return this;
    }

    @Override
    public void save() {
        ThreadProgressor.submit(true, () -> {
            try (FileWriter file = new FileWriter(SecurityUtils.PLAYER_SECURITY_FILE+sec.getUsername()+".json")) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[0];
    }
}
