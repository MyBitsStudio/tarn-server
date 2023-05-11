package com.ruse.world.content.forge.shop;

public class ForgeShopItem {
    private final int itemId;
    private final int amount;
    private final int price;

    public ForgeShopItem(int itemId, int amount, int price) {
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

    public int getPrice() {
        return price;
    }
}
