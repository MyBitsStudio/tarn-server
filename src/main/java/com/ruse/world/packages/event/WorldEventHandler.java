package com.ruse.world.packages.event;

import com.ruse.model.Item;
import com.ruse.model.Timer;
import com.ruse.util.Misc;
import com.ruse.world.WorldCalendar;
import com.ruse.world.WorldTimers;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.impl.*;
import com.ruse.world.packages.globals.GlobalBossManager;
import com.ruse.world.timers.DoubleDropTimer;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WorldEventHandler {
    @Getter
    private final List<Event> events = new ArrayList<>();

    private int ticks;

    public WorldEventHandler load(){
        for(Events event : Events.values()){
            int[] dates = event.getDates();

            if(WorldCalendar.getInstance().isWithinDate(dates[0], dates[1], dates[2], dates[3])){
                events.add(event.getEvent());
            }

        }
        if(WorldCalendar.getInstance().isWeekend()){
            events.add(new DonatorBoostEvent());
            events.add(new SlayerBonusEvent());
            events.add(new VoteBonusEvent());
        }
        return this;
    }

    public void runRandomEvent(){
        ++ticks;
        if(ticks % 50 == 0) {

            int random = Misc.random(1000);

            if (random == 633) {
                WorldTimers.register(new DoubleDropTimer(Timer.HOURS * 2));
            } else if (random == 385) {
                WorldTimers.register(new DoubleDropTimer(Timer.HOURS * 3));
            }

            if(random >= 100 && random <= 110){
                GlobalBossManager.getInstance().spawnVeigar();
            }

            if(random == 137){
                startEvent(new HalloweenSpawn());
            }
        }
    }

    public void startEvent(Event event){
        if(event == null){
            System.out.println("Event is null.");
            return;
        }
        if(events.contains(event)){
            System.out.println("Event is already active.");
            return;
        }
        events.add(event);
        event.start();
    }


    public void startEvents(){
        if(events.isEmpty()){
            System.out.println("No events to start.");
            return;
        }
        for(Event event : events){
            event.start();
        }
    }

    public boolean eventActive(String name){
        for(Event event : events){
            if(event.name().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public Event getEvent(String name){
        for(Event event : events){
            if(event.name().equalsIgnoreCase(name)){
                return event;
            }
        }
        return null;
    }

    public void reload(){
        for(Event event : events){
            event.stop();
        }
        events.clear();
        load();
        startEvents();
    }

    public void onLogin(Player player){
        for(Event event : events){
            event.onLogin(player);
        }
    }

    public boolean handleItemClick(Player player, Item item){
        for(Event event : events){
            if(event.handleItem(player, item)){
                return true;
            }
        }
        return false;
    }

    public boolean handleNpcClick(Player player, int npcId, int option){
        for(Event event : events){
            if(event.handleNpc(player, npcId, option)){
                return true;
            }
        }
        return false;
    }

    public boolean handleObjectClick(Player player, int objectId, int option){
        for(Event event : events){
            if(event.handleObject(player, objectId, option)){
                return true;
            }
        }
        return false;
    }

    public void stop(String name){
        for(Event event : events){
            if(event.name().equalsIgnoreCase(name)){
                event.stop();
                events.remove(event);
                return;
            }
        }
    }


}
