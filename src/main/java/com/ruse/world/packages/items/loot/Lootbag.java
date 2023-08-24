package com.ruse.world.packages.items.loot;

import com.ruse.model.container.ItemContainer;
import com.ruse.model.container.StackType;
import com.ruse.world.entity.impl.player.Player;

public class Lootbag {

    public static int LOOT_DEVICE = 21819;

    private ItemContainer container;

    private final Player player;

    public Lootbag(Player player) {
        this.player = player;
        container = new ItemContainer(player) {
            @Override
            public int capacity() {
                return 42;
            }

            @Override
            public StackType stackType() {
                return StackType.STACKS;
            }

            @Override
            public ItemContainer refreshItems() {
                return this;
            }

            @Override
            public ItemContainer full() {
                return this;
            }
        };
    }

    public void open() {

    }
}
