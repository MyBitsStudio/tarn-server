package com.ruse.world.packages.skills.crafting;

import com.ruse.model.Item;
import lombok.Getter;

@Getter
public enum CraftingProducts {

    RED_ESSENCE(15684, 1, 0, 0, 9,
            new Item(227, 1), new Item(1761, 1), new Item(1763, 1)),
    BLUE_ESSENCE(15686, 9,0, 1, 19,
            new Item(227, 1), new Item(1761, 1), new Item(1767, 1)),
    YELLOW_ESSENCE(15685, 19,0, 2, 29,
            new Item(227, 1), new Item(1761, 1), new Item(1765, 1)),

    RED_ESSENCE_BOWL(15690, 5,0, 3, 14,
            new Item(15684, 1), new Item(1923, 1)),
    BLUE_ESSENCE_BOWL(15689, 14,0,4 , 29,
            new Item(15686, 1), new Item(1923, 1)),
    YELLOW_ESSENCE_BOWL(15691, 24,0, 5, 44,
            new Item(15685, 1), new Item(1923, 1)),
    GREEN_ESSENCE_BOWL(15693, 34,0, 6, 56,
            new Item(15689, 1), new Item(15685, 1)),
    PURPLE_ESSENCE_BOWL(15694, 44,0, 7, 72,
            new Item(15689, 2), new Item(15691, 2)),
    AQUA_ESSENCE_BOWL(15698, 54,0, 8, 86,
            new Item(15694, 3), new Item(15689, 2)),
    MAGENTA_ESSENCE_BOWL(15700, 69,0, 9, 99,
            new Item(15698, 2), new Item(15694, 2)),
    AMBER_ESSENCE_BOWL(15696, 84,0, 10, 102,
            new Item(15700, 2), new Item(15691, 3)),

    AURA_2(23045, 25,1, 0, 2500,
            new Item(23044, 1), new Item(15690, 3), new Item(15437, 1),
            new Item(6038, 10)),
    AURA_3(23046, 39,1, 1, 5000,
            new Item(23045, 1), new Item(15689, 6), new Item(15437, 2),
            new Item(6038, 25)),
    AURA_4(23047, 59,1, 2, 10000,
            new Item(23046, 1), new Item(15691, 9), new Item(15437, 3),
            new Item(6038, 50)),
    AURA_5(23048, 74,1, 3, 25000,
            new Item(23047, 1), new Item(15694, 9), new Item(15437, 5),
            new Item(6038, 100)),
    AURA_6(23049, 89,1, 4, 50000,
            new Item(23048, 1), new Item(15700, 9), new Item(15437, 9),
            new Item(6038, 250)),

    GOLDEN_STATUE(20661, 49,1, 5, 14900,
            new Item(15585, 1), new Item(15586, 1), new Item(15584, 1),
            new Item(6038, 250), new Item(15694, 25)),
    ANCIENT_STATUE(14876, 79,1, 6, 26900,
            new Item(15587, 1), new Item(15588, 1), new Item(15589, 1),
            new Item(6038, 500), new Item(15700, 25)),
    DIAMOND_STATUE(21570, 99,1, 7, 46500,
            new Item(14876, 1), new Item(20661, 1), new Item(6038, 2500),
            new Item(15696, 50), new Item(19641, 3)),

    MASTER_AURA(22142, 99,1, 8, 69900,
            new Item(23049, 1), new Item(15448, 1), new Item(15449, 1),
            new Item(12630, 1), new Item(6038, 5000), new Item(15696, 100),
            new Item(19641, 3)),

    GEMSTONE_1(6641, 19,1, 9, 1000,
            new Item(6640, 1), new Item(15640, 5), new Item(6038, 10)),
    GEMSTONE_2(6642, 34 ,1, 10, 2500,
            new Item(6641, 1), new Item(15689, 7), new Item(6038, 25)),
    GEMSTONE_3(6644, 54 ,1, 11, 5500,
            new Item(6642, 1), new Item(15691, 9), new Item(6038, 100)),
    GEMSTONE_4(6643, 72 ,1, 12, 12500,
            new Item(6644, 1), new Item(15694, 15), new Item(6038, 250)),
    GEMSTONE_5(6645, 89 ,1, 13, 32500,
            new Item(6643, 1), new Item(15696, 25), new Item(6038, 500)),
    MASTER_GEM(6646, 99 ,1, 14, 32500,
            new Item(6645, 1), new Item(15696, 100), new Item(6038, 2500),
            new Item(19641, 3)),
    ;

    private final int finalProduct, reqLevel, type, base, xp;
    private final Item[] requiredItems;
    CraftingProducts(int finalProduct, int reqLevel, int type, int base, int xp, Item... requiredItems) {
        this.finalProduct = finalProduct;
        this.reqLevel = reqLevel;
        this.type = type;
        this.base = base;
        this.xp = xp;
        this.requiredItems = requiredItems;
    }
}
