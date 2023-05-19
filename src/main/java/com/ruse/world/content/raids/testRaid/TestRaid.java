package com.ruse.world.content.raids.testRaid;

import com.ruse.model.GameObject;
import com.ruse.model.Position;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidParty;
import com.ruse.world.content.raids.RaidRoom;
import com.ruse.world.content.raids.RaidsManager;
import com.ruse.world.content.raids.testRaid.rooms.one.TestRaidRoomOne;
import com.ruse.world.entity.impl.player.Player;

public class TestRaid extends Raid {

    public String RAID = "Test Raid";

    @Override
    public void start(RaidParty party) {
        this.current = new TestRaidRoomOne(this);
        current.onRoomStartHook();
        getParty().getPlayers().forEach(p -> {
            p.setRaid(this);
            p.moveTo(current.playerSpawn());
            p.sendMessage(("Welcome to the " + RAID + " raid!"));
            p.getPA().sendInterfaceRemoval();
        });
        this.started.set(true);
    }

    @Override
    public void rewardPlayer(Player player) {

    }

    @Override
    public void dispose() {
        if (getParty() != null) {
            getParty().getPlayers().forEach(p -> {
                p.getPA().sendInterfaceRemoval();
                p.moveTo(new Position(1234, 3562, 0));
                p.setRaid(null);
                p.setRaidParty(null);
            });
        }
        RaidsManager.getRaids().removeParty(getParty().getPlayers().get(0));
    }

    @Override
    public boolean handleObject(Player player, GameObject object, int option) {
        switch(object.getId()){
            case 13192:
                if(current.isFinished()){
                    startNextRoom();
                } else {
                    player.sendMessage("You must finish the current room before moving on!");
                }
                return true;
        }
        return false;
    }

    @Override
    public boolean handleDeath(Player player) {
        RaidRoom room = getCurrentRoom();

        if(room != null){
            player.moveTo(room.deathPosition());
            player.sendMessage("Oh dear, you have died!");
            player.getPlayerAttributes().setAttribute("test_dead", true);
        }

        int dead = 0;
        for(Player p : getParty().getPlayers()){
            if(p.getPlayerAttributes().getBooleanValue("test_dead")){
                dead++;
            }
        }
        if(dead >= getParty().getPlayers().size()){
            getParty().getPlayers().forEach(p -> {
                p.moveTo(new Position(1234, 3562, 0));
                p.sendMessage("Your team has been defeated.");
                dispose();
            });
        }
        return true;
    }
}
