package com.ruse.world.packages.shops;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class TabShop {

    private final int id;

    private final String name;

    private final boolean canSell;

    private final List<Shop> shops;

    private final List<Player> players = new CopyOnWriteArrayList<>();

    private boolean needsUpdate = false, forceUpdate = false;

    public TabShop(int id, String name, boolean canSell, List<Shop> shops) {
        this.id = id;
        this.name = name;
        this.canSell = canSell;
        this.shops = shops;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Shop getShop(int id) {
        return this.shops.get(id);
    }

    public void process(){
        for(Shop shop : shops){
            if(shop == null)
                continue;

            shop.process();
        }

    }

    public void quickRefresh(Player player, Shop shop){
        player.getPacketSender().sendItemContainer(player.getInventory(), 3823);
        shop.send(player);
    }

    private boolean canOpen(Player player){
        return player.getMode().canOpenShop(this.id);
    }

    public void send(Player player, boolean refresh){
        if(!canOpen(player))
            return;

        if(!players.contains(player))
            players.add(player);

        reset(player);

        if(refresh) {
            refresh(player);
        }

        player.getPacketSender().sendItemContainer(player.getInventory(), 3823);
        player.getPacketSender().sendInterfaceSet(160000, 3822);

        player.getPacketSender().sendString(160002, name);

        for(int i = 0; i < shops.size(); i++){
            Shop shop = shops.get(i);
            if(shop == null)
                continue;

            player.getPacketSender().sendInterfaceVisibility(160006 + (i * 2), true);
            player.getPacketSender().sendInterfaceVisibility(160007 + (i * 2), true);
            player.getPacketSender().sendString(160007 + (i * 2), shop.getName());
        }

        Shop shop = shops.get(player.getVariables().getIntValue("active-tab"));

        if(shop == null)
            return;

        shop.send(player);
    }

    private void reset(Player player){
        player.getPacketSender().sendInterfaceRemoval();
        player.setShopping(true);
        for(int i = 0; i < shops.size(); i++)
            player.getPacketSender().sendString(160007 + (i * 2), "");

        for(int i = 160006; i < 160020; i++)
            player.getPacketSender().sendInterfaceVisibility(i, false);

        for(int i = 162001; i < 162136; i++){
            player.getPacketSender().sendItemOnInterface(i, -1, 0);
        }
    }

    private void refresh(Player player){
        player.getVariables().setSetting("active-shop", this.id);
        player.getVariables().setSetting("active-tab", 0);
    }
}
