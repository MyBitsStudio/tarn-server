package com.ruse.world.packages.mode;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

public abstract class Gamemode {

    public abstract void updateTabs();

    public abstract boolean onLogin();

    public abstract Item[] starterItems();

    public abstract boolean canOpenShop(int shopId);

    public abstract boolean canTrade(Player player);

    public abstract boolean canGetRewards();

    public abstract boolean changeMode(Gamemode change);

    public abstract boolean canInstance();
    public abstract boolean canPOS();

    public abstract boolean canWear(int id);

    public abstract int xpRate(int xp);

    public abstract double dropRate();

    public abstract int doubleDropRate();

    public abstract boolean canAttack(int npcId);

    public abstract boolean openBank(Player player);

    //duel arena
    public abstract boolean canStake(Player player);

    public abstract String toString();



}
