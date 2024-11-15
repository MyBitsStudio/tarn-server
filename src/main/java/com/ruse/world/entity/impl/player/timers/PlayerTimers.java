package com.ruse.world.entity.impl.player.timers;

import com.ruse.model.Timer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTimers {

    private final Map<String, Timer> timers = new ConcurrentHashMap<>();

    public void load(@NotNull Map<String, Long> map, Player player){
        map.forEach((name, time) -> {
            Timer timer = Objects.requireNonNull(PlayerTimer.getTimer(name)).getTimerClass();
            if(timer instanceof PotionTimer times){
                times.setPlayer(player);
            }
            if(timer != null){
                //System.out.println("Loading timer: " + name);
                timer.setLength(time);
                timer.set();
                timers.put(name, timer);
            }
        });
    }

    public void startAll(){
        timers.values().forEach(timer -> {
            //System.out.println("Starting timer: " + timer.getName());
            timer.start();
        });
    }

    public Map<String, Long> save(){
        Map<String, Long> map = new ConcurrentHashMap<>();
        timers.forEach((name, timer) -> {
            if(timer.getRunning().get()){
                map.put(name, timer.returnLeft());
            }
        });
        stopAll();
        return map;
    }

    public void register(@NotNull Timer timer){
        if(timers.containsKey(timer.getName())){
            return;
        }
        timers.put(timer.getName(), timer);
        if(timer.getRunning().get()){
            timer.start();
        }
    }

    public void unregister(String name){
        stop(name);
        timers.remove(name);
    }

    public Timer get(String name){
        return timers.get(name);
    }

    public void tick(){
        timers.values().forEach(timer -> {
            if(!timer.tick()){
                unregister(timer.getName());
            }
        });
    }

    public void stopAll() {
    	timers.values().forEach(Timer::stop);
    }

    public void stop(String name) {
    	Timer timer = get(name);
    	if(timer != null) {
    		timer.stop();
    	}
    }
}
