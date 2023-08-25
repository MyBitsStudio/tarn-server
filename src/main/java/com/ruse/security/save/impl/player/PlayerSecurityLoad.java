package com.ruse.security.save.impl.player;

import com.google.gson.reflect.TypeToken;
import com.ruse.security.PlayerSecurity;
import com.ruse.security.save.SecureLoad;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.packages.misc.ItemIdentifiers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerSecurityLoad extends SecureLoad {

    private final PlayerSecurity sec;
    public PlayerSecurityLoad(PlayerSecurity sec){
        this.sec = sec;
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[0];
    }

    @Override
    public PlayerSecurityLoad run() {
        Map<String, List<String>> label = builder.fromJson(object.get("associations"),
                new TypeToken<ConcurrentMap<String, List<String>>>() {
                }.getType());

        sec.setAssociations(label);

        Map<String, Object> mapping = new ConcurrentHashMap<>();
        Map<String, Object> labels = builder.fromJson(object.get("securityMap"),
                new TypeToken<ConcurrentMap<String, Object>>() {
                }.getType());
        for(Map.Entry<String, Object> labelz : labels.entrySet()){
            String key = labelz.getKey();
            Object object = labelz.getValue();
            if(object instanceof List){
                mapping.put(key, object);
            } else if(object instanceof String) {
                mapping.put(key, get((String) object));
            }
        }

        sec.setSecurityMap(mapping);

        byte[] salt = builder.fromJson(object.get("seed"), byte[].class);
        byte[] hash = builder.fromJson(object.get("auth"), byte[].class);
        sec.setSeed(salt);
        sec.setAuth(hash);

        sec.setFA(object.get("fa").getAsString());

//        List<String> logs = builder.fromJson(object.get("logs"), new TypeToken<List<String>>(){}.getType());
//        sec.setLogins(logs);

        return this;
    }
}
