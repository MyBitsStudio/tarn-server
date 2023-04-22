package com.ruse.model.container.impl;

import com.ruse.model.Item;
import com.ruse.model.container.ItemContainer;
import com.ruse.model.container.StackType;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;

public class UimBank extends ItemContainer {

    public UimBank(Player player) {
        super(player);
    }

    @Override
    public int capacity() {
        return 70;
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

    public ItemContainer sortItems() {
        for (int k = 0; k < capacity(); k++) {
            if (getItems()[k] == null)
                continue;
            for (int i = 0; i < (capacity() - 1); i++) {
                if (getItems()[i] == null || getItems()[i].getId() <= 0) {
                    swap((i + 1), i);
                }
            }
        }
        return this;
    }

    @Override
    public UimBank switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
        if (getItems()[slot].getId() != item.getId())
            return this;
        if (to.getFreeSlots() <= 0 && !to.contains(item.getId())) {
            //if (!item.getDefinition().isStackable())
            //if (!(to instanceof Bank)) {
            to.full();
            return this;
            //}
        }

        delete(item, slot, refresh, to);

        to.add(item);
        if (sort && getAmount(item.getId()) <= 0)
            sortItems();
        if (refresh) {
            refreshItems();
            to.refreshItems();
        }

        return this;
    }
}
