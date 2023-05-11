package com.ruse.world.content.raids.firefight;

import com.ruse.model.GameObject;
import com.ruse.world.content.raids.Raid;
import com.ruse.world.content.raids.RaidParty;
import com.ruse.world.content.raids.RaidsManager;
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
    public boolean handleStartObject(Player player, @NotNull GameObject object){
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
