package com.ruse.world.packages.globals;

import com.ruse.GameSettings;
import com.ruse.model.input.impl.ChangePinPacketListener;
import com.ruse.security.save.impl.server.WellsSave;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.impl.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
    @Getter
    private final Map<String, Integer> wells = new ConcurrentHashMap<>();



    public GlobalBossManager(){
        setUp();
    }

    public void setUp(){
        wells.put("VIP", 0);
    }

    public int getProgress(String name){
        return wells.get(name);
    }

    private void save(){
        new WellsSave().create().save();
    }

    public void load(Map<String, Integer> maps){
        wells.putAll(maps);
    }

    public void addToWell(Player player, int item, int amount){
        if(!World.attributes.getSetting("globals")){
            return;
        }
        if (item == 23003) {//vip tickets
            boolean active = World.npcIsRegistered(9005);
            if (active) {
                player.getPacketSender().sendMessage("The VIP boss is already active.");
            } else {
                int well = wells.get("VIP"), max = 50;
                if (well + amount > max) {
                    amount = max - well;
                }
                if (amount > 0) {
                    wells.put("VIP", well + amount);
                    player.getInventory().delete(item, amount);
                    player.getPacketSender().sendMessage("You have added " + amount + " VIP tickets to the well.");

                } else {
                    player.getPacketSender().sendMessage("Something went wrong here. Report to an admin.");
                }
            }
            check();
        }
    }

    private void check(){
        if(wells.get("VIP") >= 50){
            wells.put("VIP", 0);
            MewtwoGlobal mewtwoGlobal = new MewtwoGlobal();
            spawn(mewtwoGlobal);
        }
        save();
    }

    public void process(){
        int tick = ticks.getAndIncrement();

        if(!World.attributes.getSetting("globals")){
            return;
        }

        if(tick % 6000 == 0){
            if(World.npcIsRegistered(9904)){
                return;
            }
            NineTailsGlobal nineTailsGlobal = new NineTailsGlobal();
            spawn(nineTailsGlobal);
        }

        if(tick % 12000 == 0){
            if(World.npcIsRegistered(8010)){
                return;
            }
            GroundonGlobal ntGlobal = new GroundonGlobal();
            spawn(ntGlobal);
        }

        if(tick % 24000 == 0){
            if(World.npcIsRegistered(3308)){
                return;
            }
            LugiaGlobal ntGlobal = new LugiaGlobal();
            spawn(ntGlobal);
        }
        check();
    }

    public void spawnDonationBoss(){
        if(World.npcIsRegistered(587)){
            return;
        }
        DonationGlobal donationGlobal = new DonationGlobal();
        spawn(donationGlobal);
    }

    public void spawnVoteBoss(){
        if(World.npcIsRegistered(8013)){
            return;
        }
        VoteGlobal donationGlobal = new VoteGlobal();
        spawn(donationGlobal);
    }

    public void spawnVeigar(){
        if(World.npcIsRegistered(9906)){
            return;
        }
        VeigarGlobal veigarGlobal = new VeigarGlobal();
        spawn(veigarGlobal);
    }

    private void spawn(GlobalBoss boss){
        World.register(boss);

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

    public String timeLeft(@NotNull String name){
        int tick;
        long ms;
        String m;
        int ticka = ticks.get();
        switch (name) {
            case "lugia" -> {
                if(World.npcIsRegistered(3308)){
                    return "::lugia";
                }
                tick = 24000 - (ticka % 24000);
                tick /= 100;
                tick *= 60;
                ms = tick;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));
                return m;
            }
            case "ninetails" -> {
                if(World.npcIsRegistered(9904)){
                    return "::ninetails";
                }
                tick = 6000 - (ticka % 6000);
                tick /= 100;
                tick *= 60;
                ms = tick;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));
                return m;
            }
            case "groudon" -> {
                if(World.npcIsRegistered(8010)){
                    return "::groudon";
                }
                tick = 12000 - (ticka % 12000);
                tick /= 100;
                tick *= 60;
                ms = tick;
                m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                        TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                        TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));
                return m;
            }
        }
        return "";
    }
}
