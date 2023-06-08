package com.ruse.security.log;

import com.ruse.engine.GameEngine;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;

public class SecureLogging {

    /**
     * Core links
     */
    public final static String PLAYER = "player/";
    /**
     * Sub Links
     */
    public final static String UPGRADE = "upgrade/";

    public static void logAction(Player player, String type, String action){

    }

    public static void logCore(String type, String producer, String action){

    }

    public static void logException(String type, String producer, Exception e){

    }

    public static void saveLog(String location, String data){

    }

}
