package com.ruse.world;

import com.ruse.model.Timer;
import com.ruse.world.entity.impl.player.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldTimers {

    private static final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public static void sequence() {
        timers.values().forEach(Timer::tick);
        for(Player player : World.getPlayers()) {
            if(player == null)
                continue;
            player.getTimers().tick();
        }
    }
}
