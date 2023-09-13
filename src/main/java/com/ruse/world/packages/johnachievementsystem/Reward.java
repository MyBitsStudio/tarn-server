package com.ruse.world.packages.johnachievementsystem;

import lombok.Getter;

@Getter
public class Reward {

    private int itemId;
    private int amount;

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
