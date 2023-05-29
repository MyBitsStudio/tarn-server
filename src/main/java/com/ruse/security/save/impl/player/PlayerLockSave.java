package com.ruse.security.save.impl.player;

import com.ruse.engine.GameEngine;
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

        Map<String, List<String>> con = new ConcurrentHashMap<>();
        for(Map.Entry<String, List<String>> coo : lock.getLockLogs().entrySet()){
            String key = coo.getKey();
            List<String> value = coo.getValue();
            List<String> secure = new CopyOnWriteArrayList<>();
            for(String string : value){
                secure.add(set(string));
            }
            con.put(key, secure);
        }

        object.add("logs", builder.toJsonTree(con));

        Map<String, Boolean> boos = new ConcurrentHashMap<>();
        for(Map.Entry<String, Boolean> coo : lock.getPlayerLocks().entrySet()){
            String key = coo.getKey();
            boos.put(key, coo.getValue());
        }

        object.add("locks", builder.toJsonTree(boos));

        object.addProperty("lockTime", lock.getLockTime());
        object.addProperty("lock", lock.getLock());

        return this;
    }

    @Override
    public void save() {
        GameEngine.submit(() -> {
            try (FileWriter file = new FileWriter(SecurityUtils.PLAYER_LOCK_FILE+username+".json")) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String key() {
        return null;
    }
}
