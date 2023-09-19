package com.ruse.world.packages.mode.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.Gamemode;

public class Ironman extends Gamemode {
    @Override
    public void updateTabs() {

    }

    @Override
    public boolean onLogin() {
        return false;
    }

    @Override
    public Item[] starterItems() {
        return new Item[]{new Item(16691, 1), new Item(17239, 1), new Item(16669, 1),
                new Item(19946, 1), new Item(19945, 1), new Item(19914, 1),
                new Item(19944, 1), new Item(21403, 1), new Item(23091, 1),
                new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                new Item(22120, 1), new Item(995, 1_000_000), new Item(14505, 1)
        };
    }

    @Override
    public boolean canOpenShop(int shopId) {
        switch (shopId) {
            case 1, 7, 4, 6, 5, 0, 8, 2, 3:
                return true;
        }
        return false;
    }

    @Override
    public boolean canTrade(Player player) {
        return false;
    }

    @Override
    public boolean canGetRewards() {
        return true;
    }

    @Override
    public boolean changeMode(Gamemode change) {
        return false;
    }

    @Override
    public boolean canInstance() {
        return true;
    }

    @Override
    public boolean canPOS() {
        return false;
    }

    @Override
    public boolean canWear(int id) {
        return switch(id){
            default -> true;
        };
    }

    @Override
    public int xpRate(int xp) {
        return xp * 20;
    }

    @Override
    public double dropRate() {
        return 125;
    }

    @Override
    public int doubleDropRate() {
        return 75;
    }

    @Override
    public boolean canAttack(int npcId) {
        return true;
    }

    @Override
    public boolean openBank(Player player) {
        return true;
    }

    @Override
    public boolean canStake(Player player) {
        return false;
    }

    @Override
    public String toString() {
        return "Ironman";
    }
}
