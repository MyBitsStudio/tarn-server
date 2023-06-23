package com.ruse.world.content.tradingpost.models;

public class Offer {

    private final int itemId;
    private int itemBonus;
    private final String itemEffect;
    private final String itemRarity;
    private final int initialAmount;
    private final int price;
    private final String seller;
    private int amountSold;

    public Offer(int itemId, int itemBonus, String itemEffect, String itemRarity, int initialAmount, int price, String seller) {
        this.itemId = itemId;
        this.itemBonus = itemBonus;
        this.itemEffect = itemEffect;
        this.itemRarity = itemRarity;
        this.initialAmount = initialAmount;
        this.price = price;
        this.seller = seller;
    }

    public int getItemId() {
        return itemId;
    }

    public int getInitialAmount() {
        return initialAmount;
    }

    public int getPrice() {
        return price;
    }

    public String getSeller() {
        return seller;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public int getItemBonus() {
        return itemBonus;
    }

    public void setItemBonus(int itemBonus) {
        this.itemBonus = itemBonus;
    }

    public String getItemEffect() {
        return itemEffect;
    }

    public String getItemRarity() {
        return itemRarity;
    }

    public int getAmountLeft() {
        return initialAmount - amountSold;
    }

    public long getTotal() {
        return 0;
    }
}
