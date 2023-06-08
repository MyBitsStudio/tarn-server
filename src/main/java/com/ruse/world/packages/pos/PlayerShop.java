package com.ruse.world.packages.pos;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class PlayerShop {

    private final List<ShopItem> items = new CopyOnWriteArrayList<>();
    private final String owner;

    public PlayerShop(String owner) {
        this.owner = owner;
    }



}
