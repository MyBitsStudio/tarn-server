package com.ruse.security.save.impl;

import com.ruse.security.ServerSecurity;
import com.ruse.security.save.SecureLoad;
import com.ruse.security.tools.SecurityUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSecurityLoad extends SecureLoad {

    private final ServerSecurity sec;
    public ServerSecurityLoad(ServerSecurity sec){
        this.sec = sec;
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[1];
    }

    @Override
    public ServerSecurityLoad run() {

        Map<String, Object> associations = new ConcurrentHashMap<>();
        Map<String, Object> label = builder.fromJson(object.get("keys"),
                new com.google.gson.reflect.TypeToken<ConcurrentHashMap<String, Object>>() {
                }.getType());

        for(Map.Entry<String, Object> labelz : label.entrySet()){
            String key = labelz.getKey();
            Object value = labelz.getValue();
            if(value instanceof String){
                associations.put(key.trim(), ((String) value).trim());
            }
        }

        sec.setKeys(associations);

        Map<String, Map<String, String>> mapping = new ConcurrentHashMap<>();
        Map<String, List<String>> security = builder.fromJson(object.get("securityMap"),
                new com.google.gson.reflect.TypeToken<ConcurrentHashMap<String, List<String>>>() {
                }.getType());
        for(Map.Entry<String, List<String>> maps : security.entrySet()){
            String key = maps.getKey();
            List<String> value = maps.getValue();
            Map<String, String> secure = new ConcurrentHashMap<>();
            for(String ss : value){
                String[] split = ss.split("\\+::\\+");
                secure.put(get(split[0]).trim(), get(split[1]).trim());
            }
            mapping.put(key, secure);
        }

        sec.setSecurityMap(mapping);

        String[] blacklist = builder.fromJson(object.get("blacklist"),
                new com.google.gson.reflect.TypeToken<String[]>() {
                }.getType());
        sec.setBlackList(blacklist);
        return this;
    }
}
