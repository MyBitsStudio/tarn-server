package com.ruse.world.packages.mode.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;
import com.ruse.world.packages.mode.Gamemode;

public class UltimateIronman extends Gamemode {

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
                new Item(19944, 1), new Item(21403, 1), new Item(23091, 1),
                new Item(22084, 1), new Item(22092, 1), new Item(22083, 1),
                new Item(22119, 1), new Item(995, 1_000_000)
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
        return switch(id){
            default -> true;
        };
    }

    @Override
    public int xpRate(int xp) {
        return xp * 10;
    }

    @Override
    public double dropRate() {
        return 15;
    }

    @Override
    public int doubleDropRate() {
        return 10;
    }

    @Override
    public boolean canAttack(int npcId) {
        return true;
    }

    @Override
    public boolean openBank(Player player) {
        if(CombatConstants.wearingUltimateBankItem(player)){
            player.getUimBank().open();
        }
        return false;
    }

    @Override
    public boolean canStake(Player player) {
        return false;
    }
}
