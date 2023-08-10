package com.ruse.world.packages.shops;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopItem {

    private final int defaultStock;

    private int stock, flag, price, id;

    public ShopItem(int id, int stock, int price) {
        this.stock = stock;
        this.defaultStock = stock;
        this.price = price;
        this.id = id;
    }

}
