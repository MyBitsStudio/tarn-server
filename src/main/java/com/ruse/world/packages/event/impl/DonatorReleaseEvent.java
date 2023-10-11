package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class DonatorReleaseEvent extends Event {

    @Override
    public String name() {
        return "DonatorRelease";
    }

    @Override
    public void start() {
        World.attributes.setSetting("donator-bonus", true);
    }

    @Override
    public void stop() {

    }

    @Override
    public void onLogin(@NotNull Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Donator Release event is active with 50%!");
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
}
