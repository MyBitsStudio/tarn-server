package com.ruse.world.packages.packs.scratch.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.scratch.Scratch;

public class RareScratchII extends Scratch {
    public RareScratchII(Player player) {
        super(player);
    }

    @Override
    public Item[] commons() {
        return new Item[]{
                new Item(1038),new Item(1040),new Item(1042),new Item(1044),
                new Item(1046),new Item(1048)
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(19132),new Item(18405),new Item(18406),new Item(18407),
                        new Item(18408),new Item(18409)
        };
    }

    @Override
    public int cardId() {
        return 22122;
    }
}
