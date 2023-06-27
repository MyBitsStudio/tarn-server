package com.ruse.world.packages.shops;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopItem {

    private final int defaultStock;

    private int stock, flag, price;

    public ShopItem(int amount, int stock, int price) {
        this.stock = amount;
        this.defaultStock = stock;
        this.price = price;
    }

}
