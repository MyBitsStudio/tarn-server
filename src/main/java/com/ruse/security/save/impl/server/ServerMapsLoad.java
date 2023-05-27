package com.ruse.security.save.impl.server;

import com.ruse.security.ServerSecurity;
import com.ruse.security.save.SecureLoad;
import com.ruse.security.tools.SecurityUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerMapsLoad extends SecureLoad {

    private final ServerSecurity sec;
    public ServerMapsLoad(ServerSecurity sec){
        this.sec = sec;
    }

    @Override
    public String key() {
        return SecurityUtils.seeds[1];
    }

    @Override
    public ServerMapsLoad run() {
        Map<String, List<String>> security = builder.fromJson(object.get("ipMap"),
                new com.google.gson.reflect.TypeToken<ConcurrentHashMap<String, List<String>>>() {
                }.getType());
        sec.setIpMap(security);
        Map<String, List<String>> mac = builder.fromJson(object.get("macMap"),
                new com.google.gson.reflect.TypeToken<ConcurrentHashMap<String, List<String>>>() {
                }.getType());
        sec.setMacMap(mac);
        Map<String, List<String>> hwid = builder.fromJson(object.get("hwidMap"),
                new com.google.gson.reflect.TypeToken<ConcurrentHashMap<String, List<String>>>() {
                }.getType());
        sec.setHwidMap(hwid);
        List<String> flagged = builder.fromJson(object.get("flagged"),
                new com.google.gson.reflect.TypeToken<List<String>>() {
                }.getType());
        sec.setFlagged(flagged);
        return this;
    }
}
