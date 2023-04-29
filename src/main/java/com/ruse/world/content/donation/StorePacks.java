package com.ruse.world.content.donation;

import lombok.Getter;

public enum StorePacks {

    $1_BOND(10946, 1.00),
    TRANSFER_CRYSTAL(8788, 3.00),
    $10_BOND(23057, 10.00),
    COLLECTOR_NECKLACE(19886, 15.00),
    COLLECTOR_RING(4446, 15.00),
    $25_BOND(23058, 25.00),
    COLLECTORS_ATTACHMENT(9084, 25.00),
    CURSED_BOX(15003, 35.00),
    GRACIOUS_BOX(15002, 50.00),

    OWNER_CAPE(7995, 60.00),
    MAJESTIC_BOX(15004, 75.00),
    DR_ENHANCE(99999, 75.00), // need id
    OWNER_ATTACHMENT(22110, 85.00),
    INFAMOUS_BOX(20489, 100.00),
    $100_BOND(23059, 100.00),
    OWNER_GOODY(13019, 100.00),
    DAMNATION_BOX(20491, 150.00),
    DR_BOOSTER(99999, 150.00), // need id
    SACRED_BOX(20490, 200.00),
    $250_BOND(23060, 250.00),
    COLLECTORS_AURA(15450, 250.00),
    OWNER_CHEST(99999, 300.00), // need id

    ;

    @Getter private int itemId;
    @Getter private double price;
    StorePacks(int itemId, double price) {
        this.itemId = itemId;
        this.price = price;
    }

    public static double getPriceForItem(int itemId){
        for(StorePacks pack : StorePacks.values()){
            if(pack.getItemId() == itemId){
                return pack.getPrice();
            }
        }
        return 0.00;
    }
}
