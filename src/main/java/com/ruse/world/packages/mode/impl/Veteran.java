package com.ruse.world.packages.mode.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.Gamemode;

public class Veteran extends Gamemode {
    @Override
    public void updateTabs() {

    }

    @Override
    public boolean onLogin() {
        return false;
    }

    @Override
    public Item[] starterItems() {
        return new Item[]{new Item(703, 1), new Item(704, 1), new Item(705, 1),
                new Item(19946, 1), new Item(19945, 1), new Item(19914, 1),
                new Item(19944, 1), new Item(23089, 1), new Item(23091, 1),
                new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                new Item(995, 1_000_000)
        };
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
    public boolean canWear(int id) {
        return switch(id){
            case 16691, 9704, 17239, 16669, 4977, 4989, 4995, 16339, 6068, 9703
                    -> false;
            default -> true;
        };
    }

    @Override
    public int xpRate(int xp) {
        return xp;
    }

    @Override
    public double dropRate() {
        return 30;
    }

    @Override
    public int doubleDropRate() {
        return 20;
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
        return true;
    }
}
