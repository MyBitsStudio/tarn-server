package com.ruse.world.packages.event.impl;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.event.Event;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class SlayerBonusEvent extends Event {

    @Getter
    private int bonus;
    @Override
    public String name() {
        return "SlayerBonus";
    }

    @Override
    public void start() {
        World.attributes.setSetting("slayer-bonus", true);
        bonus = Misc.random(5, 50);
    }

    @Override
    public void onLogin(@NotNull Player player) {
        player.sendMessage("@red@[EVENT]@whi@ Slayer Bonus event is active with "+bonus+"%!");
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
