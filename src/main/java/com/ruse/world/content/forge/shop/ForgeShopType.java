package com.ruse.world.content.forge.shop;

import com.google.common.collect.ImmutableList;

public enum ForgeShopType {
    ARMOURY(0),
    WEAPONS(1),
    JEWELRY(2);

    private final int key;

    ForgeShopType(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public static final ImmutableList<ForgeShopType> VALUES = ImmutableList.copyOf(values());
}
