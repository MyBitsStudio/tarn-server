package com.ruse.world.packages.donation;

import lombok.Getter;

public enum StorePacks {

    $10_BOND(23057, 10.00),
    $25_BOND(23058, 25.00),
    $100_BOND(23059, 100.00),
    $250_BOND(23060, 250.00),

    LEGENDARY_UPGRADE(23217, 5.00),
    MAJESTIC_UPGRADE(23218, 15.00),
    PREMIUM_PASS(3686, 40.00),

    ARMOR_RANDOM(23255, 50.00),
    ENHANCE_CHEST(20506, 50.00),
    OWNER_ULT_CHEST(20507, 1200.00),
    TICKET_PACK(20498, 50.00),
    COUNTER_PACK(20500, 35.00),
    EVENT_PACK(20501, 100.00),
    DONATOR_PACK(20502, 75.00),
    OWNER_CHEST(23002, 300.00),

    DROP_RATE_TOKEN(23207, 5.00),
    DAMAGE_HIGH_TOKEN(23208, 5.00),
    AOE_TOKEN_6(23206, 25.00),
    AOE_TOKEN_3(23219, 10.00),

    SOLAR_3(23243, 10.00),
    SHINI_2(23244, 30.00),
    FAZULA_2(23245, 45.00),
    YASUDA_2(23246, 60.00),

    GOKU_1(23247, 100.00),
    LIGHT_1(23248, 150.00),
    DARK_1(23249, 200.00),
    WARDEN_1(23250, 250.00),
    ASTA_1(23251, 300.00),


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
