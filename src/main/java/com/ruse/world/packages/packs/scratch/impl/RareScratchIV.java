package com.ruse.world.packages.packs.scratch.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.scratch.Scratch;

public class RareScratchIV extends Scratch {
    public RareScratchIV(Player player) {
        super(player);
    }

    @Override
    public Item[] commons() {
        return new Item[]{
                new Item(19134),new Item(18410),new Item(18411),new Item(18412),
                new Item(18413),new Item(18414)
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(18415),new Item(18416),new Item(18417),new Item(19133),
                        new Item(18418),new Item(18419)
        };
    }

    @Override
    public int cardId() {
        return 22124;
    }
}
