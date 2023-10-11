package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;

public class HalloweenEvent extends Event {
    @Override
    public String name() {
        return "Halloween";
    }

    @Override
    public void start() {
        World.register(new NPC(3306, new Position(2221, 3749, 0)));
    }

    @Override
    public void stop() {

    }

    @Override
    public void onLogin(Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Halloween event is active!");
    }

    @Override
    public boolean handleItem(Player player, Item item) {
        return false;
    }

    @Override
    public boolean handleNpc(Player player, int npcId, int option) {
        switch(npcId){
            case 3306 -> {
                switch(option){
                    case 1->{

                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean handleObject(Player player, int objectId, int option) {
        switch(objectId){
            case 31424 -> {
                switch(option){
                    case 1->{

                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onDeath(Player player, int npcId) {
        return false;
    }
}