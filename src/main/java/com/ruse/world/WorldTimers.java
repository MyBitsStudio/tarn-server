package com.ruse.world;

import com.ruse.model.Timer;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WorldTimers {

    private static final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public static void sequence() {
        timers.values().forEach(timer -> {
            if(!timer.tick()){
                unregister(timer.getName());
            }
        });
    }

    public static void unregister(String name){
        stop(name);
        timers.remove(name);
    }

    public static void stopAll() {
        timers.values().forEach(Timer::stop);
    }

    public static void stop(String name) {
        Timer timer = get(name);
        if(timer != null) {
            timer.stop();
        }
    }

    public static void register(@NotNull Timer timer){
        if(timers.containsKey(timer.getName())){
            return;
        }
        timers.put(timer.getName(), timer);
        if(timer.getRunning().get()){
            timer.start();
        }
    }

    public static Timer get(String name){
        return timers.get(name);
    }
}
