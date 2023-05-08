package com.ruse.world.content.forge;

import java.util.HashMap;

public final class ForgeItemConstants {

    private static final HashMap<Integer, Integer> ITEM_VALUES = new HashMap<>();

    static {
        ITEM_VALUES.put(4151, 50);
        ITEM_VALUES.put(6585, 65);
        ITEM_VALUES.put(11732, 35);
        ITEM_VALUES.put(4712, 35);
        ITEM_VALUES.put(4714, 35);
        ITEM_VALUES.put(4716, 35);
    }

    public static int getValue(int itemId) {
        return ITEM_VALUES.getOrDefault(itemId, 0);
    }

    private ForgeItemConstants() {

    }
}
