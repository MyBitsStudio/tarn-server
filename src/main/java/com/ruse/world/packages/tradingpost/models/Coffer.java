package com.ruse.world.packages.tradingpost.models;

import lombok.Getter;

@Getter
public class Coffer {
    private final String username;
    private int amount;

    public Coffer(String username, int amount) {
        this.username = username;
        this.amount = amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void incrementAmount(int amount) {
        this.amount += amount;
    }
}
