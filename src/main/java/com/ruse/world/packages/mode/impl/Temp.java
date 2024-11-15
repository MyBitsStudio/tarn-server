package com.ruse.world.packages.mode.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.Gamemode;

public class Temp extends Gamemode {
    @Override
    public void updateTabs() {

    }

    @Override
    public boolean onLogin() {
        return false;
    }

    @Override
    public Item[] starterItems() {
        return new Item[0];
    }

    @Override
    public boolean canOpenShop(int shopId) {
        return false;
    }

    @Override
    public boolean canTrade(Player player) {
        return false;
    }

    @Override
    public boolean canGetRewards() {
        return false;
    }

    @Override
    public boolean changeMode(Gamemode change) {
        return false;
    }

    @Override
    public boolean canInstance() {
        return false;
    }

    @Override
    public boolean canPOS() {
        return false;
    }

    @Override
    public boolean canWear(int id) {
        return false;
    }

    @Override
    public int xpRate(int xp) {
        return 0;
    }

    @Override
    public double dropRate() {
        return 0;
    }

    @Override
    public int doubleDropRate() {
        return 0;
    }

    @Override
    public boolean canAttack(int npcId) {
        return false;
    }

    @Override
    public boolean openBank(Player player) {
        return false;
    }

    @Override
    public boolean canStake(Player player) {
        return false;
    }

    @Override
    public String toString() {
        return "Hacks";
    }
}
