package com.ruse.world.packages.globals;

import com.ruse.GameSettings;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.impl.GoldenApeGlobal;
import com.ruse.world.packages.globals.impl.NineTailsGlobal;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalBossManager {

    private static GlobalBossManager instance;

    public static GlobalBossManager getInstance(){
        if(instance == null)
            instance = new GlobalBossManager();
        return instance;
    }
    private final AtomicInteger ticks = new AtomicInteger(-20);

    public GlobalBossManager(){

    }

    public void process(){
        ticks.getAndIncrement();

        if(ticks.get() % 6000 == 0){
            if(World.npcIsRegistered(9904)){
                return;
            }
            NineTailsGlobal nineTailsGlobal = new NineTailsGlobal();
            spawn(nineTailsGlobal);
        }

        if(ticks.get() % 18000 == 0){
            if(World.npcIsRegistered(9908)){
                return;
            }
            GoldenApeGlobal nineTailsGlobal = new GoldenApeGlobal();
            spawn(nineTailsGlobal);
        }
    }

    private void spawn(GlobalBoss boss){
        World.register(boss);
        JavaCord.sendMessage("\uD83E\uDD16│\uD835\uDDEE\uD835\uDDF0\uD835\uDE01\uD835\uDDF6\uD835\uDE03\uD835\uDDF6\uD835\uDE01\uD835\uDE06", "**[World Boss] "+boss.message()+" **");

        for (Player players : World.getPlayers()) {
            if (players == null) {
                continue;
            }
            players.getPacketSender().sendBroadCastMessage("[GLOBAL] "+boss.message(), 100);
        }
        World.sendBroadcastMessage("[GLOBAL] "+boss.message());
        GameSettings.broadcastMessage = "[GLOBAL] "+boss.message();
        GameSettings.broadcastTime = 100;
        World.sendNewsMessage("[GLOBAL] "+boss.message());
    }

    public String timeLeft(String name){
        int tick;
        long ms;
        String m;
        switch(name){
            case "ninetails":
                tick = 6000 - (ticks.get() % 6000);
                tick /= 100;
                tick *= 60;

                ms = tick ;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));

                if (ticks.get() < 0) {
                    m = "Soon";
                }
                return m;

            case "golden":
                tick = 18000 - (ticks.get() % 18000);
                tick /= 100;
                tick *= 60;

                ms = tick ;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));

                if (ticks.get() < 0) {
                    m = "Soon";
                }
                return m;

        }
        return "";
    }
}
