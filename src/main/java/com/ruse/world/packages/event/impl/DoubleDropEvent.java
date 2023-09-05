package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;

public class DoubleDropEvent extends Event {
    @Override
    public String name() {
        return "DoubleDrop";
    }

    @Override
    public void start() {

    }

    @Override
    public void onLogin(Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Double Drop event is active!");
    }

    @Override
    public boolean handleItem(Player player, Item item) {
        return false;
    }

    @Override
    public boolean handleNpc(Player player, int npcId) {
        return false;
    }

    @Override
    public boolean handleObject(Player player, int objectId) {
        return false;
    }

    @Override
    public boolean onDeath(Player player, int npcId) {
        return false;
    }
}
