package com.ruse.world.content.tradingpost.models;

public class Coffer {
    private final String username;
    private int amount;

    public Coffer(String username, int amount) {
        this.username = username;
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void incrementAmount(int amount) {
        this.amount += amount;
    }
}
