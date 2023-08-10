package com.ruse.world.packages.packs.scratch.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.scratch.Scratch;

public class ScratchI extends Scratch {
    public ScratchI(Player player) {
        super(player);
    }

    @Override
    public Item[] commons() {
        return new Item[]{
                new Item(1050)
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(1038)
        };
    }

    @Override
    public int cardId() {
        return 455;
    }
}
