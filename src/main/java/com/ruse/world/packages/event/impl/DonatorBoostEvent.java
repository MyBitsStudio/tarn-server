package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;
import lombok.Getter;

@Getter
public class DonatorBoostEvent extends Event {

    private int boost;

    @Override
    public String name() {
        return "DonatorBonus";
    }

    @Override
    public void start() {
        World.attributes.setSetting("donator-bonus", true);
        boost = Misc.random(5, 45);
    }

    @Override
    public void stop() {

    }

    @Override
    public void onLogin(Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Donator Bonus event is active with "+boost+"%!");
    }

    @Override
    public boolean handleItem(Player player, Item item) {
        return false;
    }

    @Override
    public boolean handleNpc(Player player, int npcId, int option) {
        return false;
    }

    @Override
    public boolean handleObject(Player player, int objectId, int option) {
        return false;
    }

    @Override
    public boolean onDeath(Player player, int npcId) {
        return false;
    }

    @Override
    public void handleEventDrop(Player player, Item drop, Position pos) {

    }

}
