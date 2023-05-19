package com.ruse.security.save.impl;

import com.ruse.engine.GameEngine;
import com.ruse.security.PlayerSecurity;
import com.ruse.security.ServerSecurity;
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
        Map<String, List<String>> con = new ConcurrentHashMap<>();
        for(Map.Entry<String, List<String>> coo : sec.getAssociations().entrySet()){
            String key = coo.getKey();
            List<String> value = coo.getValue();
            List<String> secure = new CopyOnWriteArrayList<>();
            for(String string : value){
                secure.add(set(string));
            }
            con.put(key, secure);
        }

        object.add("associations", builder.toJsonTree(con));

        Map<String, Object> map = new ConcurrentHashMap<>();
        for(Map.Entry<String, Object> maps : sec.getSecurityMap().entrySet()){
            String key = maps.getKey();
            Object value = maps.getValue();
            if(value instanceof List){
                List<String> secure = new CopyOnWriteArrayList<>();
                for(String string : (List<String>) value){
                    secure.add(set(string));
                }
                map.put(key, secure);
            } else if(value instanceof String){
                map.put(key, set((String) value));
            }
        }

        object.add("securityMap", builder.toJsonTree(map));

        object.add("seed", builder.toJsonTree(sec.getSeed()));
        object.add("auth", builder.toJsonTree(sec.getAuth()));
        object.addProperty("fa", sec.getFA());

        object.add("logs", builder.toJsonTree(sec.getLogins()));

        return this;
    }

    @Override
    public void save() {
        GameEngine.submit(() -> {
            try (FileWriter file = new FileWriter(SecurityUtils.PLAYER_SECURITY_FILE+sec.getUsername()+".json")) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[0];
    }
}
