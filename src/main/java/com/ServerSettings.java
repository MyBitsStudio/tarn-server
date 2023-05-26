package com;

import org.javacord.api.entity.server.Server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerSettings {

    private static ServerSettings server;

    public static ServerSettings getServer() {
        if(server == null)
            server = new ServerSettings();
        return server;
    }

    private final Map<String, Boolean> contentLocks = new ConcurrentHashMap<>();

    public ServerSettings(){

    }

    public void load(){

    }



}
