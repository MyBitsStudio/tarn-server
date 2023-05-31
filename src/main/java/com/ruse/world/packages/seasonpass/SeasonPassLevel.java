package com.ruse.world.packages.seasonpass;

public class SeasonPassLevel {
    private int freeItemId;
    private int freeAmount;
    private int premiumItemId;
    private int premiumAmount;
    private int expNeeded;

    public SeasonPassLevel(int freeItemId, int freeAmount, int premiumItemId, int premiumAmount, int expNeeded) {
        this.freeItemId = freeItemId;
        this.freeAmount = freeAmount;
        this.premiumItemId = premiumItemId;
        this.premiumAmount = premiumAmount;
        this.expNeeded = expNeeded;
    }

    public SeasonPassLevel() {
    }

    public int getFreeItemId() {
        return freeItemId;
    }

    public void setFreeItemId(int freeItemId) {
        this.freeItemId = freeItemId;
    }

    public int getFreeAmount() {
        return freeAmount;
    }

    public void setFreeAmount(int freeAmount) {
        this.freeAmount = freeAmount;
    }

    public int getPremiumItemId() {
        return premiumItemId;
    }

    public void setPremiumItemId(int premiumItemId) {
        this.premiumItemId = premiumItemId;
    }

    public int getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(int premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public int getExpNeeded() {
        return expNeeded;
    }

    public void setExpNeeded(int expNeeded) {
        this.expNeeded = expNeeded;
    }

}
