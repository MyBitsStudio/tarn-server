package com.ruse.security.save.impl.server;

import com.ruse.engine.GameEngine;
import com.ruse.io.ThreadProgressor;
import com.ruse.security.ServerSecurity;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSecuritySave extends SecureSave {

    private final ServerSecurity sec;
    public ServerSecuritySave(ServerSecurity sec){
        this.sec = sec;
    }

    @Override
    public ServerSecuritySave create() {
        Map<String, List<String>> security = new ConcurrentHashMap<>();
        Map<String, Map<String, String>> mapping = sec.getSecurityMap();
        for(Map.Entry<String, Map<String, String>> maps : mapping.entrySet()){
            String key = maps.getKey();
            Map<String, String> value = maps.getValue();
            List<String> secure = new ArrayList<>();
            for(Map.Entry<String, String> map : value.entrySet()){
                String k = map.getKey();
                String v = map.getValue();
                secure.add(set(k)+"+::+"+set(v));
            }
            security.put(key, secure);
        }

        object.add("securityMap", builder.toJsonTree(security));

        Map<String, Object> keyMaps = new ConcurrentHashMap<>();
        Map<String, Object> keyMapping = sec.getKeys();
        for(Map.Entry<String, Object> maps : keyMapping.entrySet()){
            String key = maps.getKey();
            Object value = maps.getValue();
            if(value instanceof String){
                keyMaps.put(key.trim(), ((String) value).trim());
            }
        }

        object.add("keys", builder.toJsonTree(keyMaps));

        object.add("blacklist", builder.toJsonTree(sec.getBlackList()));
        return this;
    }

    @Override
    public void save() {
        ThreadProgressor.submit(false, () -> {
            try (FileWriter file = new FileWriter(SecurityUtils.SERVER_SECURITY_FILE)) {
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
        return SecurityUtils.seeds[1];
    }
}
