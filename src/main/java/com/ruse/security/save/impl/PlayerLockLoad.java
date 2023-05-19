package com.ruse.security.save.impl;

import com.google.gson.reflect.TypeToken;
import com.ruse.security.PlayerLock;
import com.ruse.security.save.SecureLoad;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerLockLoad extends SecureLoad {

    private PlayerLock lock;
    public PlayerLockLoad(PlayerLock lock){
        this.lock = lock;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public PlayerLockLoad run() {
        Map<String, Integer> labels = builder.fromJson(object.get("assoc"),
                new TypeToken<Map<String, Integer>>() {
                }.getType());

        lock.setAssociations(labels);

        Map<String, List<String>> label = builder.fromJson(object.get("logs"),
                new TypeToken<Map<String, List<String>>>() {
                }.getType());

        lock.setLockLogs(label);

        Map<String, Boolean> labela = builder.fromJson(object.get("locks"),
                new TypeToken<Map<String, Boolean>>() {
                }.getType());

        lock.setPlayerLocks(labela);

        lock.setLockTime(object.get("lockTime").getAsLong());
        lock.setLock(object.get("lock").getAsString());

        return this;
    }
}
