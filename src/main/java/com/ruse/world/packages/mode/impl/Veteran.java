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
    public boolean canWear(int id) {
        return switch(id){
            case 16691, 9704, 17239, 16669, 4977, 4989, 4995, 16339, 6068, 9703
                    -> false;
            default -> true;
        };
    }

    @Override
    public int xpRate(int xp) {
        return xp / 300;
    }

    @Override
    public double dropRate() {
        return 50;
    }

    @Override
    public int doubleDropRate() {
        return 30;
    }

    @Override
    public boolean canAttack(int npcId) {
        return false;
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
