package com.ruse.world.packages.tradingpost.models;

import lombok.Getter;

@Getter
public class Offer {

    private final int itemId;
    private int itemBonus;
    private final int initialAmount;
    private final int price;
    private final String seller;
    private final int slot;
    private int amountSold;
    private final long timestamp;
    private final String uid, perk, bonus;

    public Offer(int itemId, String uid, String perk, String bonus, int initialAmount, int price, String seller, int slot, long timestamp) {
        this.itemId = itemId;
        this.uid = uid;
        this.perk = perk;
        this.bonus = bonus;
        this.initialAmount = initialAmount;
        this.price = price;
        this.seller = seller;
        this.slot = slot;
        this.timestamp = timestamp;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public void setItemBonus(int itemBonus) {
        this.itemBonus = itemBonus;
    }

    public int getAmountLeft() {
        return initialAmount - amountSold;
    }

    public long getTotal() {
        return (long) getAmountLeft() *price;
    }

    public void incrementAmountSold(int amountSold) {
        this.amountSold += amountSold;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "itemId=" + itemId +
                ", itemBonus=" + itemBonus +
                ", initialAmount=" + initialAmount +
                ", price=" + price +
                ", seller='" + seller + '\'' +
                ", slot=" + slot +
                ", amountSold=" + amountSold +
                '}';
    }
}
