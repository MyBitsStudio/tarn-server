package com.ruse.world.packages.packs.scratch.impl;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.packs.scratch.Scratch;

public class RareScratchI extends Scratch {
    public RareScratchI(Player player) {
        super(player);
    }

    @Override
    public Item[] commons() {
        return new Item[]{
                new Item(1050),new Item(6769),new Item(13736), new Item(1053),
                new Item(1057),new Item(1055)
        };
    }

    @Override
    public Item[] rares() {
        return new Item[]{
                new Item(1038),new Item(1040),new Item(1042),new Item(1044),
                        new Item(1046),new Item(1048)
        };
    }

    @Override
    public int cardId() {
        return 22121;
    }
}
