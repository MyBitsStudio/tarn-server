package com.ruse.world.packages.packs.locked.impl;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.packages.packs.locked.LockBox;

public class SummerChest extends LockBox {
    @Override
    public int key() {
        return 28;
    }

    @Override
    public Item[] commons() {
        return new Item[]{
            new Item(10835, 10),
            new Item(21815, Misc.random(2, 7)),
            new Item(8788, Misc.random(2, 6)),
            new Item(4560, Misc.random(2, 16)),
            new Item(4564, Misc.random(2, 14)),
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
            new Item(23149, 1),
            new Item(23148, 1),
            new Item(22121, Misc.random(5)),
            new Item(20501, 1),
            new Item(4563, Misc.random(2, 18)),
            new Item(4561, Misc.random(2, 15)),
        };
    }

    @Override
    public Item[] ultra() {
        return new Item[]{
            new Item(23057, Misc.random(2)),
            new Item(23147, 1),
            new Item(23255, 2),
            new Item(23256, Misc.random(2)),
            new Item(4562, Misc.random(1, 24)),
            new Item(13557, 1),
        };
    }

    @Override
    public int boxId() {
        return 29;
    }
}
