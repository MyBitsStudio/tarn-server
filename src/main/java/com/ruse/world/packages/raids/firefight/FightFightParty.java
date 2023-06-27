package com.ruse.world.packages.raids.firefight;

import com.ruse.model.GameObject;
import com.ruse.world.packages.raids.Raid;
import com.ruse.world.packages.raids.RaidParty;
import com.ruse.world.packages.raids.RaidsManager;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class FightFightParty extends RaidParty {
    public FightFightParty(Player player, Raid raid) {
        super(player, raid);
        raid.setParty(this);
        player.setRaidParty(this);
        RaidsManager.getRaids().addParty(player, this);
    }

    @Override
    public String key() {
        return "FireFight";
    }

    @Override
    public boolean canJoin(Player player) {

        return false;
    }

    @Override
    public void onJoin(@NotNull Player player) {
        player.sendMessage("You have joined the " + key() + " raid!");
        player.setRaidParty(this);
    }

    @Override
    public void onLeave(Player player) {
        getPlayers().remove(player);
        player.setRaidParty(null);
        player.setRaid(null);
    }

    @Override
    public void startRaid() {
        raid.setParty(this);
        RaidsManager.getRaids().addRaid(getOwner(), raid);
        raid.start(this);
    }

    @Override
    public boolean handleStartObject(Player player, @NotNull GameObject object){
        switch(object.getId()){
            case 621:
                if(getOwner() == player){
                    startRaid();
                }
                return true;
        }
        return false;
    }
}
