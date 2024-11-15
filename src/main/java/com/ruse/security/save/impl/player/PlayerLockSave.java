package com.ruse.security.save.impl.player;

import com.ruse.engine.GameEngine;
import com.ruse.io.ThreadProgressor;
import com.ruse.security.PlayerLock;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerLockSave extends SecureSave {

    private PlayerLock lock;
    private Player player;
    private String username;
    public PlayerLockSave(String username, PlayerLock lock) {
        this.lock = lock;
        this.username = username;
    }
    @Override
    public SecureSave create() {
        object.add("assoc", builder.toJsonTree(lock.getAssociations()));

        object.add("logs", builder.toJsonTree(lock.getLockLogs()));

        object.add("locks", builder.toJsonTree(lock.getPlayerLocks()));

        object.addProperty("lockTime", lock.getLockTime());
        object.addProperty("lock", lock.getLock());

        return this;
    }

    @Override
    public void save() {
        ThreadProgressor.submit(true, () -> {
            try (FileWriter file = new FileWriter(SecurityUtils.PLAYER_LOCK_FILE+username+".json")) {
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
        return null;
    }
}
