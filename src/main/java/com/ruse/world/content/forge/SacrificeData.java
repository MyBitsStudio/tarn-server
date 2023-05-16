package com.ruse.world.content.forge;

import java.util.HashMap;

public final class SacrificeData {

    private static final HashMap<Integer, Integer> ITEM_VALUES = new HashMap<>();

    static {
        ITEM_VALUES.put(4151, 350);
        ITEM_VALUES.put(6585, 165);
        ITEM_VALUES.put(11732, 135);
        ITEM_VALUES.put(4712, 235);
        ITEM_VALUES.put(4714, 335);
        ITEM_VALUES.put(4716, 135);
    }

    public static int getValue(int itemId) {
        return ITEM_VALUES.getOrDefault(itemId, 0);
    }

    private SacrificeData() {

    }
}
