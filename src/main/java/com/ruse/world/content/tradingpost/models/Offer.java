package com.ruse.world.content.tradingpost.models;

public class Offer {

    private final int itemId;
    private int itemBonus;
    private final int initialAmount;
    private final int price;
    private final String seller;
    private final int slot;
    private int amountSold;

    public Offer(int itemId,  int initialAmount, int price, String seller, int slot) {
        this.itemId = itemId;
        this.initialAmount = initialAmount;
        this.price = price;
        this.seller = seller;
        this.slot = slot;
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

    public int getSlot() {
        return slot;
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
