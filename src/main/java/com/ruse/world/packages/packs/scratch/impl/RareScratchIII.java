package com.ruse.world.packages.packs.scratch.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.scratch.Scratch;

public class RareScratchIII extends Scratch {
    public RareScratchIII(Player player) {
        super(player);
    }

    @Override
    public Item[] commons() {
        return new Item[]{
                new Item(19132),new Item(18405),new Item(18406),new Item(18407),
                new Item(18408),new Item(18409)
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(19134),new Item(18410),new Item(18411),new Item(18412),
                        new Item(18413),new Item(18414)
        };
    }

    @Override
    public int cardId() {
        return 22123;
    }
}
