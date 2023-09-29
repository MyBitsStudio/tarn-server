package com.ruse.world.packages.event;

import com.ruse.model.Timer;
import com.ruse.util.Misc;
import com.ruse.world.WorldCalendar;
import com.ruse.world.WorldTimers;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.impl.DonatorBoostEvent;
import com.ruse.world.packages.event.impl.DoubleDropEvent;
import com.ruse.world.packages.event.impl.SlayerBonusEvent;
import com.ruse.world.packages.event.impl.VoteBonusEvent;
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

        if(ticks++ % 100 == 0) {

            int random = Misc.random(100000);

            if (random == 63315) {
                WorldTimers.register(new DoubleDropTimer(Timer.HOURS * 2));
            } else if (random == 38512) {
                WorldTimers.register(new DoubleDropTimer(Timer.HOURS * 3));
            }
        }
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
        events.clear();
        load();
        startEvents();
    }

    public void onLogin(Player player){
        for(Event event : events){
            event.onLogin(player);
        }
    }
}
