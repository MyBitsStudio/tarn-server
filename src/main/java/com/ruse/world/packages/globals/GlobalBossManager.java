package com.ruse.world.packages.globals;

import com.ruse.GameSettings;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.impl.*;

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
        int tick = ticks.getAndIncrement();

        if(tick % 3000 == 0){
            if(World.npcIsRegistered(9906)){
                return;
            }
            VeigarGlobal nineTailsGlobal = new VeigarGlobal();
            spawn(nineTailsGlobal);
        }

        if(tick % 6000 == 0){
            if(World.npcIsRegistered(9904)){
                return;
            }
            NineTailsGlobal nineTailsGlobal = new NineTailsGlobal();
            spawn(nineTailsGlobal);
        }

        if(tick % 12000 == 0){
            if(World.npcIsRegistered(9907)){
                return;
            }
            MeruemGlobal nineTailsGlobal = new MeruemGlobal();
            spawn(nineTailsGlobal);
        }

        if(tick % 18000 == 0){
            if(World.npcIsRegistered(9908)){
                return;
            }
            GoldenApeGlobal nGlobal = new GoldenApeGlobal();
            spawn(nGlobal);
        }

        if(tick % 24000 == 0){
            if(World.npcIsRegistered(3308)){
                return;
            }
            LugiaGlobal ntGlobal = new LugiaGlobal();
            spawn(ntGlobal);
        }
    }

    private void spawn(GlobalBoss boss){
        World.register(boss);
        JavaCord.sendMessage(1117224370587304057L, "**[World Boss] "+boss.message()+" **");

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
        int ticka = ticks.get();
        switch(name){
            case "ninetails":
                tick = 6000 - (ticka % 6000);
                tick /= 100;
                tick *= 60;

                ms = tick ;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));

                if (tick < 0) {
                    m = "Soon";
                }
                return m;

            case "golden":
                tick = 18000 - (ticka % 18000);
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

            case "meruem":
                tick = 18000 - 12000 - (ticka % 12000);
                tick /= 100;
                tick *= 60;

                ms = tick ;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));

                if (tick < 0) {
                    m = "Soon";
                }
                return m;

            case "veigar":
                tick = 3000 - (ticka % 3000);
                tick /= 100;
                tick *= 60;

                ms = tick ;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));

                if (tick < 0) {
                    m = "Soon";
                }
                return m;

            case "lugia":
                tick = 24000 - (ticka % 24000);
                tick /= 100;
                tick *= 60;

                ms = tick ;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));

                if (tick < 0) {
                    m = "Soon";
                }
                return m;

        }
        return "";
    }
}
