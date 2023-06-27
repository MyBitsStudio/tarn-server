package com.ruse.world.packages.raids.testRaid;

import com.ruse.model.GameObject;
import com.ruse.world.packages.raids.Raid;
import com.ruse.world.packages.raids.RaidParty;
import com.ruse.world.packages.raids.RaidsManager;
import com.ruse.world.entity.impl.player.Player;

public class TestRaidParty extends RaidParty {
    public TestRaidParty(Player player, Raid raid) {
        super(player, raid);
        raid.setParty(this);
        player.setRaidParty(this);
        RaidsManager.getRaids().addParty(player, this);
    }

    @Override
    public String key() {
        return "Test Raid";
    }

    @Override
    public boolean canJoin(Player player) {
        return true;
    }

    @Override
    public void onJoin(Player player) {
        player.sendMessage("You have joined the " + key() + " raid!");
    }

    @Override
    public void onLeave(Player player) {
        getPlayers().remove(player);
    }

    @Override
    public void startRaid() {
        raid.setParty(this);
        RaidsManager.getRaids().addRaid(getOwner(), raid);
        raid.start(this);
    }

    @Override
    public boolean handleStartObject(Player player, GameObject object){
        switch(object.getId()){
            case 12260:
                if(getOwner() == player){
                    startRaid();
                }
                return true;
        }
        return false;
    }


}
