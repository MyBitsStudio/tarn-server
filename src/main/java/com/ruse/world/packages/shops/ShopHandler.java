package com.ruse.world.packages.shops;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruse.world.entity.impl.player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ShopHandler {

    public static final int BASE_TABS = 0;
    public static final int MAX_TABS = 10;

    private final List<TabShop> shops = new ArrayList<>();

    public ShopHandler(){

    }

    public void load(){
        try {
            Gson gson = new Gson();
            for (File file : Objects.requireNonNull(new File("./.core/server/shops/").listFiles())) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                JsonArray jsonObjects = gson.fromJson(reader, JsonArray.class);
                reader.close();

                for (JsonElement json : jsonObjects) {
                    JsonObject obj = json.getAsJsonObject();

                    int id = obj.get("id").getAsInt();

                    String name = obj.get("name").getAsString();

                    boolean canSell = obj.get("canSell").getAsBoolean();

                    List<Shop> shops = new ArrayList<>();
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

                            int itemId = itemObject.get("itemId").getAsInt();

                            int amount = itemObject.get("amount").getAsInt();

                            int price = itemObject.get("price").getAsInt();

                            items.add(new ShopItem(itemId, amount, price));
                        }
                        shops.add(new Shop(shopId, curr, shopName, items));

                    }

                    this.shops.add(new TabShop(id, name, canSell, shops));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process(){
        for(TabShop tabShop : shops){
            tabShop.process();
            if(tabShop.isNeedsUpdate()){
                for(Player player : tabShop.getPlayers()){

                }
            }
        }
    }

    public Optional<TabShop> getShop(int shopId){
        return shops.stream().filter(shop -> shop.getId() == shopId).findFirst();
    }

    public Optional<Shop> getTabbedShop(int shop, int index){
        return Optional.of(shops.get(shop).getShop(index));
    }
}
