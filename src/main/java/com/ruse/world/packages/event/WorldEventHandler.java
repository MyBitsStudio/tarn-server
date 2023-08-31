package com.ruse.world.packages.event;

import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WorldEventHandler {

    private final Calendar calendar = Calendar.getInstance();
    private final List<Event> events = new ArrayList<>();

    public WorldEventHandler load(){
        for(Events event : Events.values()){
            int[] dates = event.getDates();

            calendar.set(Calendar.MONTH, dates[0]);
            calendar.set(Calendar.DAY_OF_MONTH, dates[1]);
            calendar.set(Calendar.YEAR, 2023);
            long start = calendar.getTimeInMillis();

            calendar.set(Calendar.MONTH, dates[2]);
            calendar.set(Calendar.DAY_OF_MONTH, dates[3]);
            calendar.set(Calendar.YEAR, 2023);
            long end = calendar.getTimeInMillis();

            if(System.currentTimeMillis() >= start && System.currentTimeMillis() <= end){
                events.add(event.getEvent());
            }
        }
        return this;
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
    }

    public void onLogin(Player player){
        for(Event event : events){
            event.onLogin(player);
        }
    }
}
