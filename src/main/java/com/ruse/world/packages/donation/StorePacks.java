package com.ruse.world.packages.donation;

import lombok.Getter;

public enum StorePacks {

    $10_BOND(23057, 10.00),
    $25_BOND(23058, 25.00),
    $100_BOND(23059, 100.00),
    $250_BOND(23060, 250.00),

    CURSED_BOX(15003, 20.00),
    GRACIOUS_BOX(15002, 35.00),
    MAJESTIC_BOX(15004, 50.00),
    INFAMOUS_BOX(20489, 75.00),
    DAMNATION_BOX(20491, 100.00),
    SACRED_BOX(20490, 150.00),
    OWNER_CHEST(23002, 300.00),


    TRANSFER_CRYSTAL(8788, 3.00),
    DR_ENHANCE(23178, 50.00),
    OWNER_GOODY(13019, 40.00),
    DR_BOOSTER(4440, 125.00),
    DAMAGE_SCROLL(4442, 125.00),
    PREMIUM_PASS(3686, 40.00),
    COLLECTORS_AURA(15450, 175.00),
    COLLECTORS_ATTACHMENT(9084, 25.00),
    OWNER_ATTACHMENT(22110, 75.00),

    ENHANCE_CHEST(20506, 100.00),
    OWNER_ULT_CHEST(20507, 1200.00),
    TICKET_PACK(20498, 50.00),
    COUNTER_PACK(20500, 35.00),
    EVENT_PACK(20501, 100.00),
    DONATOR_PACK(20502, 75.00),

    DROP_RATE_TOKEN(23207, 350.00),
    DAMAGE_HIGH_TOKEN(23208, 350.00),
    AOE_TOKEN(23206, 425.00),


    ;

    @Getter private final int itemId;
    @Getter private final double price;
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
