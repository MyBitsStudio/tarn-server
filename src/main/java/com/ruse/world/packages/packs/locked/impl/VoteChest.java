package com.ruse.world.packages.packs.locked.impl;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.packages.packs.locked.LockBox;

public class VoteChest extends LockBox {
    @Override
    public int key() {
        return 23103;
    }

    @Override
    public Item[] commons() {
        return new Item[]{
            new Item(10835, 10),
            new Item(23103, 1),
            new Item(22121, 2),
            new Item(8788, 3),
            new Item(4000, Misc.random(2, 6)),
            new Item(4001, Misc.random(1, 3)),
            new Item(23149, 1),
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(10835, 25),
                new Item(23103, 3),
                new Item(22121, 3),
                new Item(22122, 1),
                new Item(8788, 6),
                new Item(4000, Misc.random(4, 12)),
                new Item(4001, Misc.random(2, 7)),
                new Item(23149, 2),
                new Item(23148, 1),
        };
    }

    @Override
    public Item[] ultra() {
        return new Item[]{
                new Item(10835, 50),
                new Item(23103, 5),
                new Item(22121, 5),
                new Item(22122, 3),
                new Item(22123, 1),
                new Item(8788, 10),
                new Item(4000, Misc.random(8, 24)),
                new Item(4001, Misc.random(6, 14)),
                new Item(23148, 3),
                new Item(23149, 1),
        };
    }

    @Override
    public int boxId() {
        return 25101;
    }
}
