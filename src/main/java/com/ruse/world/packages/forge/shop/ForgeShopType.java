package com.ruse.world.packages.forge.shop;

import com.google.common.collect.ImmutableList;

public enum ForgeShopType {
    ARMOURY(0),
    JEWELRY(1),
    OTHERS(2);

    private final int key;

    ForgeShopType(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    public static final ImmutableList<ForgeShopType> VALUES = ImmutableList.copyOf(values());
}
