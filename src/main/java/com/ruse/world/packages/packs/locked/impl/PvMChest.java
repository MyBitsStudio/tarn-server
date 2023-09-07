package com.ruse.world.packages.packs.locked.impl;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.packages.packs.locked.LockBox;

public class PvMChest extends LockBox {
    @Override
    public int key() {
        return 23105;
    }

    @Override
    public Item[] commons() {
        return new Item[]{
            new Item(10835, 5),
            new Item(13727, Misc.random(5, 25)),
            new Item(21815, Misc.random(1, 3)),
            new Item(21814, Misc.random(1, 3)),
            new Item(4558, Misc.random(1, 3)),
            new Item(22121, Misc.random(1, 2)),
            new Item(8788, Misc.random(1, 2)),
            new Item(23149, 1),
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(10835, 10),
                new Item(13727, Misc.random(9, 44)),
                new Item(21815, Misc.random(2, 5)),
                new Item(21814, Misc.random(2, 5)),
                new Item(4559, Misc.random(1, 3)),
                new Item(22121, Misc.random(1, 4)),
                new Item(22122, Misc.random(1, 2)),
                new Item(8788, Misc.random(1, 4)),
                new Item(23148, 1),
        };
    }

    @Override
    public Item[] ultra() {
        return new Item[]{
                new Item(10835, 15),
                new Item(13727, Misc.random(12, 59)),
                new Item(21815, Misc.random(3, 8)),
                new Item(21814, Misc.random(3, 8)),
                new Item(4560, Misc.random(1, 3)),
                new Item(22122, Misc.random(1, 4)),
                new Item(22123, Misc.random(1, 2)),
                new Item(8788, Misc.random(1, 6)),
                new Item(23147, 1),
        };
    }

    @Override
    public int boxId() {
        return 25103;
    }
}
