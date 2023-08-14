package com.ruse.world.packages.event;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

public abstract class Event {

    public abstract String name();

    public abstract void start();

    public abstract void onLogin(Player player);

    public abstract boolean handleItem(Player player, Item item);

    public abstract boolean handleNpc(Player player, int npcId);

    public abstract boolean handleObject(Player player, int objectId);

    public abstract boolean onDeath(Player player, int npcId);
}
