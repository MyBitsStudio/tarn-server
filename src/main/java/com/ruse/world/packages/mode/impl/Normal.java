package com.ruse.world.packages.mode.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.Gamemode;

public class Normal extends Gamemode {

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
        return switch(shopId){
            default -> true;
        };
    }

    @Override
    public boolean canTrade(Player player) {
//        return switch(player.gamemode){
//            default -> true;
//        };
        return true;
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
    public boolean canWear(int id) {
        return switch(id){
            default -> true;
        };
    }

    @Override
    public double xpRate() {
        return 0;
    }

    @Override
    public double dropRate() {
        return 0;
    }
}
