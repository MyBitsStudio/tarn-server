package com.ruse.world.packages.chests;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

import java.util.List;

public abstract class Chest {

    public abstract boolean open(Player player);

    public abstract int keyId();
    public abstract int animationId();
    public abstract int graphicId();

    public abstract List<Item> rewards();
}
