package com.ruse.world.packages.johnachievementsystem;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

@Getter
public enum PerkType {
    ACCURACY(0),
    DROP(1),
    MELEE(2),
    MAGE(3),
    RANGE(4),
    COINS(5),
    DEFENCE(6),
    SKILLING(7),
    ABSORB(8);

    private final int key;

    PerkType(int key) {
        this.key = key;
    }

    public static final ImmutableList<PerkType> VALUES = ImmutableList.copyOf(values());
}
