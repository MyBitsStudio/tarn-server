package com.ruse.world.packages.prices;

public class PriceManager {

    private static PriceManager priceManager;

    public static PriceManager getPriceManager() {
        if (priceManager == null) {
            priceManager = new PriceManager();
        }
        return priceManager;
    }


}
