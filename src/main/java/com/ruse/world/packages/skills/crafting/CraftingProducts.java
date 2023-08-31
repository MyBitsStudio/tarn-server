package com.ruse.world.packages.skills.crafting;

import com.ruse.model.Item;
import lombok.Getter;

@Getter
public enum CraftingProducts {

    RED_ESSENCE(15684, 5,
            new Item(227, 1), new Item(1761, 1), new Item(1763, 1)),
    BLUE_ESSENCE(15686, 9,
            new Item(227, 1), new Item(1761, 1), new Item(1767, 1)),
    YELLOW_ESSENCE(15685, 14,
            new Item(227, 1), new Item(1761, 1), new Item(1765, 1)),

    AURA_2(23045, 25,
            new Item(23044, 1), new Item(15684, 3), new Item(15437, 1)),

    ;

    private final int finalProduct, reqLevel;
    private final Item[] requiredItems;
    CraftingProducts(int finalProduct, int reqLevel, Item... requiredItems) {
        this.finalProduct = finalProduct;
        this.reqLevel = reqLevel;
        this.requiredItems = requiredItems;
    }
}
