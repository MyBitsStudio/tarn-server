package com.ruse.world.content.johnachievementsystem;

import com.google.common.collect.ImmutableList;

public enum PerkType {
    PERK_ONE(0),
    PERK_TWO(1),
    PERK_THREE(2),
    PERK_FOUR(3),
    PERK_FIVE(4),
    PERK_SIX(5),
    PERK_SEVEN(6),
    PERK_EIGHT(7),
    PERK_NINE(8);

    private final int key;

    PerkType(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public static final ImmutableList<PerkType> VALUES = ImmutableList.copyOf(values());
}
