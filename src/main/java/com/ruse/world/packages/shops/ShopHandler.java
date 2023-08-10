package com.ruse.world.packages.shops;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruse.model.Item;
import com.ruse.security.save.impl.server.JunkPriceLoad;
import com.ruse.world.entity.impl.player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class ShopHandler {

    private static final List<TabShop> shops = new ArrayList<>();
    public static Map<Integer, Integer> junkPrices = new HashMap<>();

    public ShopHandler(){

    }

    public static void load(){
        try {
            Gson gson = new Gson();
            for (File file : Objects.requireNonNull(new File("./.core/server/shops/").listFiles())) {
                if(file.getName().contains("junkPrices"))
                    continue;
                BufferedReader reader = new BufferedReader(new FileReader(file));
                JsonArray jsonObjects = gson.fromJson(reader, JsonArray.class);
                reader.close();

                for (JsonElement json : jsonObjects) {
                    JsonObject obj = json.getAsJsonObject();

                    int id = obj.get("id").getAsInt();

                    String name = obj.get("name").getAsString();

                    boolean canSell = obj.get("canSell").getAsBoolean();

                    List<Shop> shopsa = new ArrayList<>();
                    JsonArray shopsObject = obj.getAsJsonArray("shops");
                    for (JsonElement shopKey : shopsObject) {
                        JsonObject shopObject = shopKey.getAsJsonObject();

                        int shopId = shopObject.get("shopId").getAsInt();

                        String shopName = shopObject.get("shopName").getAsString();

                        String currency = shopObject.get("currency").getAsString();
                        Currency curr = Currency.getCurrency(currency.toUpperCase());

                        List<ShopItem> items = new ArrayList<>();
                        JsonArray itemsObject = shopObject.getAsJsonArray("items");
                        for (JsonElement itemKey : itemsObject) {
                            JsonObject itemObject = itemKey.getAsJsonObject();

                            int itemId = itemObject.get("id").getAsInt();

                            int amount = itemObject.get("amount").getAsInt();

                            int price = itemObject.get("price").getAsInt();

                            items.add(new ShopItem(itemId, amount, price));
                        }
                        shopsa.add(new Shop(shopId, curr, shopName, items));

                    }

                    shops.add(new TabShop(id, name, canSell, shopsa));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadPrices(){
        new JunkPriceLoad().loadJSON("./.core/server/shops/junkPrices.json").run();
    }

    public static void process(){
        for(TabShop tabShop : shops){
            if(tabShop == null)
                continue;
            tabShop.process();
            for(Shop shops : tabShop.getShops()){
                if(shops.isUpdated()){
                    for(Player player : tabShop.getPlayers()){
                        if(player != null){
                            shops.send(player);
                        }
                    }
                    shops.resetUpdate();
                }
            }

        }
    }

    public static Optional<TabShop> getShop(int shopId){
        return shops.stream().filter(shop -> shop.getId() == shopId).findFirst();
    }

    public static Optional<Shop> getTabbedShop(int shop, int index){
        return Optional.of(shops.get(shop).getShop(index));
    }

    public static boolean sell(Player player, int slot, int amount){
        Optional<TabShop> option = getShop(player.getVariables().getIntValue("active-shop"));

        if(option.isPresent()){
            TabShop shop = option.get();

            if(!shop.isCanSell()){
                player.sendMessage("You cannot sell items to this shop.");
                return false;
            }

            Item item = player.getInventory().getItems()[slot];

            Optional<Shop> shops = getTabbedShop(shop.getId(), player.getVariables().getIntValue("active-tab"));
            Optional<Integer> price = Optional.ofNullable(junkPrices.get(item.getId()));

            if(price.isPresent()){
                int prices = price.get();

                amount = Math.min(amount, item.getAmount());

                if(shops.isPresent()){
                    Shop shop1 = shops.get();

                    if(player.getInventory().contains(item.getId(), amount)){
                        player.getInventory().delete(new Item(item.getId(), amount));
                        player.getInventory().add(new Item(995, prices * amount));
                        player.sendMessage("You sold " + amount + " " + item.getDefinition().getName() + " for " + prices * amount + " coins.");
                        shop.quickRefresh(player, shop1);
                        return true;
                    } else {
                        player.sendMessage("You do not have enough of this item to sell.");
                        return false;
                    }
                }


            } else {
                player.sendMessage("You cannot sell this item to this shop.");
                return false;
            }


        }
        return false;
    }

    public static boolean buy(Player player, int itemId, int amount){
        Optional<TabShop> option = getShop(player.getVariables().getIntValue("active-shop"));

        if(option.isPresent()){
            TabShop shop = option.get();

            Optional<Shop> shopOption = getTabbedShop(shop.getId(), player.getVariables().getIntValue("active-tab"));

            if(shopOption.isPresent()){
                Shop shop1 = shopOption.get();

                ShopItem item = shop1.getItem(itemId);

                if(Objects.equals(item, null)){
                    return false;
                } else {
                    Currency currency = shop1.getCurrency();

                    int amountLeft = Math.min(item.getStock(), amount);

                    if(!hasEnough(player, currency, amountLeft * item.getPrice())){
                        player.sendMessage("You do not have enough " + currency.getName() + " to buy this item.");
                        return false;
                    }

                    if(player.getInventory().canHold(new Item(item.getId(), amountLeft))){
                        takeCurrency(player, currency, amountLeft * item.getPrice());
                        player.getInventory().add(new Item(item.getId(), amountLeft));
                        item.setStock(item.getStock() - amountLeft);
                        shop.quickRefresh(player, shop1);
                        return true;
                    } else {
                        player.sendMessage("You do not have enough inventory space to buy this item.");
                        return false;
                    }
                }
            }
        }

        return false;
    }

    private static boolean hasEnough(Player player, Currency currency, int amount){
        if(currency.getItemId() != -1)
            return player.getInventory().contains(currency.getItemId(), amount);

        return switch(currency){
            case DONATION -> player.getPlayerVIP().getPoints() >= amount;
            case SLAYER -> player.getPointsHandler().getSlayerPoints() >= amount;
            default -> false;
        };
    }

    private static void takeCurrency(Player player, Currency currency, int amount){
        if(currency.getItemId() != -1)
            player.getInventory().delete(currency.getItemId(), amount);
        else
            switch(currency){
                case DONATION -> player.getPlayerVIP().takePoints(amount);
                case SLAYER -> player.getPointsHandler().setSlayerPoints(player.getPointsHandler().getSlayerPoints() - amount, false);
            }
    }

    public static boolean handleShop(Player player, int interfaceId, int option, int slot){
        if(interfaceId != 30929)
            return false;

        switch(option){
            case 1 -> {
                Optional<Shop> option1 = getTabbedShop(player.getVariables().getIntValue("active-shop"), player.getVariables().getIntValue("active-tab"));
                if(option1.isPresent()){
                    Shop shop = option1.get();

                    if(slot >= shop.getItems().size())
                        return true;

                    ShopItem item = shop.getItems().get(slot);

                    if(item == null)
                        return true;

                    player.sendMessage("This item costs " + item.getPrice() + " " + shop.getCurrency().getName() + ".");
                    return true;
                }
                return true;
            }
            case 2 -> {
                Optional<Shop> option1 = getTabbedShop(player.getVariables().getIntValue("active-shop"), player.getVariables().getIntValue("active-tab"));

                if(option1.isPresent()){
                    Shop shop = option1.get();

                    if(slot >= shop.getItems().size())
                        return true;

                    ShopItem item = shop.getItems().get(slot);

                    if(item == null)
                        return true;

                    if(item.getStock() == 0){
                        player.sendMessage("This item is out of stock.");
                        return true;
                    }

                    buy(player, item.getId(), 1);
                    return true;
                }
            }

        }

        return false;
    }

    public static boolean handleButton(Player player, int id){
        switch(id){
            case 160006 -> { player.getVariables().setSetting("active-tab", 0); getShop(player.getVariables().getIntValue("active-shop")).ifPresent(s -> s.send(player, false)); return true; }
            case 160008 -> { player.getVariables().setSetting("active-tab", 1); getShop(player.getVariables().getIntValue("active-shop")).ifPresent(s -> s.send(player, false));return true; }
            case 160010 -> { player.getVariables().setSetting("active-tab", 2); getShop(player.getVariables().getIntValue("active-shop")).ifPresent(s -> s.send(player, false));return true; }
            case 160012 -> { player.getVariables().setSetting("active-tab", 3); getShop(player.getVariables().getIntValue("active-shop")).ifPresent(s -> s.send(player, false));return true; }
            case 160014 -> { player.getVariables().setSetting("active-tab", 4); getShop(player.getVariables().getIntValue("active-shop")).ifPresent(s -> s.send(player, false));return true; }
        }
        return false;
    }
}
