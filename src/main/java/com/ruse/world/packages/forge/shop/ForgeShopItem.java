package com.ruse.world.packages.forge.shop;

public class ForgeShopItem {
    private final int itemId;
    private final int price;

    public ForgeShopItem(int itemId, int price) {
        this.itemId = itemId;
        this.price = price;
    }

    public int getItemId() {
        return itemId;
    }

    public int getPrice() {
        return price;
    }
}
