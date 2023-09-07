package com.ruse.world.packages.packs.locked.impl;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.packages.packs.locked.LockBox;

public class SlayerChest extends LockBox {
    @Override
    public int key() {
        return 23104;
    }

    @Override
    public Item[] commons() {
        return new Item[]{
            new Item(10835, 5),
            new Item(23104, 1),
            new Item(5023, Misc.random(10, 40)),
            new Item(4155, 3),
            new Item(2380, Misc.random(1, 3)),
            new Item(21814, Misc.random(1, 5)),
            new Item(21815, Misc.random(1, 5)),
            new Item(13727, Misc.random(10, 50)),
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(10835, 10),
                new Item(23104, 3),
                new Item(5023, Misc.random(20, 85)),
                new Item(4155, Misc.random(3, 7)),
                new Item(2381, Misc.random(1, 3)),
                new Item(2382, Misc.random(1, 3)),
                new Item(21814, Misc.random(3, 12)),
                new Item(21815, Misc.random(3, 12)),
                new Item(13727, Misc.random(30, 100)),
        };
    }

    @Override
    public Item[] ultra() {
        return new Item[]{
                new Item(10835, 25),
                new Item(23104, 5),
                new Item(5023, Misc.random(35, 145)),
                new Item(4155, Misc.random(5, 12)),
                new Item(2383, Misc.random(1, 3)),
                new Item(21816, Misc.random(1, 10)),
                new Item(13727, Misc.random(55, 210)),
        };
    }

    @Override
    public int boxId() {
        return 25102;
    }
}
