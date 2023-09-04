package com.ruse.world.packages.globals;

import com.ruse.GameSettings;
import com.ruse.model.input.impl.ChangePinPacketListener;
import com.ruse.security.save.impl.server.WellsSave;
import com.ruse.world.World;
import com.ruse.world.content.discordbot.JavaCord;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.globals.impl.*;
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
    private final Map<String, Integer> wells = new ConcurrentHashMap<>();



    public GlobalBossManager(){
        setUp();
    }

    public void setUp(){
        wells.put("VIP", 0);
    }

    public boolean interact(Player player, int item) {
        switch (item) {
            case 23003 -> {
                player.setInputHandling(new ChangePinPacketListener());
                player.getPacketSender().sendEnterInputPrompt("Enter a new pin");
            }
        }
        return false;
    }

    public int getProgress(String name){
        return wells.get(name);
    }

    public Map<String, Integer> getWells(){
        return wells;
    }

    private void save(){
        new WellsSave().create().save();
    }

    public void load(Map<String, Integer> maps){
        wells.putAll(maps);
    }

    public void addToWell(Player player, int item, int amount){
        if(World.attributes.getSetting("globals")){
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
                    return "SPAWNED";
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
        }
        return "";
    }
}
