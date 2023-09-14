package com.ruse.world.packages.starter;

import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.shops.ShopItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarterShop {

    private final Map<StartShopItems, Integer> items = new HashMap<>();
    private final Player player;

    public StarterShop(Player player){
        this.player = player;
        setUp();
    }

    public Map<StartShopItems, Integer> getItems() {
        return items;
    }

    public void load(Map<StartShopItems, Integer> items) {
        this.items.putAll(items);
    }

    private void setUp(){
        for(StartShopItems item : StartShopItems.values()){
            items.put(item, item.getCap());
        }
    }

    public void open(boolean refresh){
        player.getPacketSender().sendInterfaceRemoval();
        reset();
        if(refresh)
            refresh();
        player.getPacketSender().sendInterface(164000);
        send(player.getVariables().getIntValue("active-tab"));

    }

    public void send(int category){
        List<ShopItem> itemL = new ArrayList<>();
        for(StartShopItems item : items.keySet()){
            if(item.getCategory() != category)
                continue;
            if(items.get(item) == 0)
                continue;
            itemL.add(new ShopItem(item.getId(), items.get(item), item.getPrice()));
        }
        player.getPacketSender().sendItemContainerArray(itemL.toArray(new ShopItem[0]), 164101);
    }

    private void reset(){
        for(int i = 164014; i < 164020; i++){
            player.getPacketSender().sendInterfaceVisibility(i, false);
        }
        player.getPacketSender().sendString(164007, "Packs");
        player.getPacketSender().sendString(164009, "Items");
        player.getPacketSender().sendString(164011, "Special");
        player.getPacketSender().sendString(164013, "Misc");
    }

    private void refresh(){
        player.getVariables().setSetting("active-tab", 0);
    }

    public boolean handleShop(int inter, int option, int slot, int id){
        if(inter != -32507)
            return false;
        //System.out.println("Interface : "+inter+"Option: " + option + " Slot: " + slot + " Id: " + id);
        StartShopItems item = getItem(id);
        if(item == null)
            return false;

        switch(option){
            case 1-> {
                player.sendMessage(ItemDefinition.forId(item.getId()).getName() + " costs " + item.getPrice() + " starter tokens.");
                return true;
            }
            case 2 -> {
                int amount = player.getInventory().getAmount(12852);
                if(amount < item.getPrice()){
                    player.sendMessage("You do not have enough starter tokens to purchase this item.");
                    return true;
                }
                if(items.get(item) <= 0){
                    player.sendMessage("There are no more of this item in stock.");
                    return true;
                }
                player.getInventory().delete(12852, item.getPrice());
                items.put(item, items.get(item) - 1);
                player.getInventory().add(item.getId(), 1);
                player.sendMessage("You have purchased a " + ItemDefinition.forId(item.getId()).getName() + ".");
                AchievementHandler.progress(player, item.getPrice(), 2);
                AchievementHandler.progress(player, item.getPrice(), 3);
                open(false);
                return true;
            }
        }
        return false;
    }

    public StartShopItems getItem(int id){
        return items.keySet().stream().filter(item -> item.getId() == id).findFirst().orElse(null);
    }

    public boolean handleButton(int button){
        switch(button){
            case 164006 -> {
                player.getVariables().setSetting("active-tab", 0);
                open(false);
                return true;
            }
            case 164008 -> {
                player.getVariables().setSetting("active-tab", 1);
                open(false);
                return true;
            }
            case 164010 -> {
                player.getVariables().setSetting("active-tab", 2);
                open(false);
                return true;
            }
            case 164012 -> {
                player.getVariables().setSetting("active-tab", 3);
                open(false);
                return true;
            }
        }
        return false;
    }
}
