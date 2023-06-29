package com.ruse.world.packages.mode.impl;

import com.ruse.model.GameMode;
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
        return false;
    }
}
