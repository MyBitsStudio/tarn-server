package com.ruse.world.packages.shops;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class Shop {

    private final int id;
    private final Currency currency;
    private final String name;
    private final List<ShopItem> items = new CopyOnWriteArrayList<>();
    private long lastUpdate;

    private boolean updated = false, forceUpdate = false;

    public Shop(int id, Currency currency, String name, List<ShopItem> items) {
        this.id = id;
        this.currency = currency;
        this.name = name;
        this.items.addAll(items);
    }

    public void process() {
        update();
    }

    private void update() {
        boolean update = false;
        for (ShopItem item : items) {
            if (item.getStock() < item.getDefaultStock() && lastUpdate < System.currentTimeMillis() - 15000) {
                item.setStock(item.getStock() + 1);
                update = true;
            }
        }
        if (update || forceUpdate) {
            updated = true;
            lastUpdate = System.currentTimeMillis();
        }
    }

    public void send(Player player){

    }

    public boolean sell(Player player, int itemId, int amount){

        return false;
    }

    public boolean buy(Player player, int itemId, int amount){

        return false;
    }

}
