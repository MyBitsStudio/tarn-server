package com.ruse.world.packages.forge;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

@Getter
public enum ForgeTierType {
    TIER_ONE(1, 500),
    TIER_TWO(2, 10_000),
    TIER_THREE(3, 25_000)
    ;

    private final int tier;
    private final int requiredValue;

    ForgeTierType(int tier, int requiredValue) {
        this.tier = tier;
        this.requiredValue = requiredValue;
    }

    public static final ImmutableList<ForgeTierType> VALUES = ImmutableList.copyOf(values());
}
