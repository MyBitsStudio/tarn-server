package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;

public class Solstice extends Event {
    @Override
    public String name() {
        return "Solstice";
    }

    @Override
    public void start() {
        World.register(new NPC(3321, new Position(2221, 3746, 0)));
    }

    @Override
    public void onLogin(Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Summer Solstice event is active!");
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
