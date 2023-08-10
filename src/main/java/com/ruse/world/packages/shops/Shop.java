package com.ruse.world.packages.shops;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

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

    public void send(@NotNull Player player){
        player.getPacketSender().sendItemContainerArray(items.toArray(new ShopItem[0]), 162001);
    }

    public ShopItem getItem(int id){
        return this.items.stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    public ShopItem[] asArray(){
        return this.items.toArray(new ShopItem[0]);
    }

    public void resetUpdate(){
        this.updated = false;
        this.forceUpdate = false;
    }

}
