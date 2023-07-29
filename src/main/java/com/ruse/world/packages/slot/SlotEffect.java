package com.ruse.world.packages.slot;

import lombok.Getter;

@Getter
public enum SlotEffect {

    NOTHING(SlotRarity.COMMON),

    DROP_RATE_LOW(SlotRarity.UNCOMMON, 5, 20),
    DOUBLE_XP(SlotRarity.UNCOMMON),

    DOUBLE_DROP(SlotRarity.RARE, 5, 20),
    DROP_RATE_MED(SlotRarity.RARE, 25, 50),

    MULTI_KILLS(SlotRarity.LEGENDARY, 1, 3),
    DOUBLE_CASH(SlotRarity.LEGENDARY),
    ALL_DAMAGE(SlotRarity.LEGENDARY, 1, 3),

    AOE_EFFECT(SlotRarity.MYTHICAL, 1, 4),
    DROP_RATE_HIGH(SlotRarity.MYTHICAL, 50, 99),
    TRIPLE_CASH(SlotRarity.MYTHICAL),
    MULTI_SHOT(SlotRarity.MYTHICAL, 2, 3)

    ;

    private final SlotRarity rarity;
    private final int[] ranges;
    SlotEffect(SlotRarity rarity, int... ranges){
        this.rarity = rarity;
        this.ranges = ranges;
    }

}

