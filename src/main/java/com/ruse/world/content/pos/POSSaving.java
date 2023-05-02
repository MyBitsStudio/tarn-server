package com.ruse.world.content.pos;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.ruse.model.projectile.ItemEffect;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class POSSaving {

    public static void save(Object object, String path) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try(BufferedWriter writer = new BufferedWriter(new BufferedWriter(new FileWriter(path)))){
            gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object load(String path){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return gson.fromJson(path, Object.class);
    }

    public static String historyDir = "./data/saves/pos/posHistory.json";
    public static String coreDir = "./data/saves/pos/coreHistory.json";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<PlayerOwnedShop.HistoryItem> loadHistory() {
        List<PlayerOwnedShop.HistoryItem> history = new ArrayList<>();
        if (!Files.exists(Paths.get(historyDir))) {
            return Collections.emptyList();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(historyDir))) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            if (object == null) {
                return Collections.emptyList();
            }
            JsonArray jsonArray = object.getAsJsonArray("history");
            if (jsonArray == null) {
                return Collections.emptyList();
            }
            for (JsonElement jsonElement : jsonArray) {
                history.add(gson.fromJson(jsonElement, PlayerOwnedShop.HistoryItem.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return history;
    }

    public static List<PlayerOwnedShop.HistoryLog> loadHCore() {
        List<PlayerOwnedShop.HistoryLog> history = new ArrayList<>();
        if(!Files.exists(Paths.get(coreDir))){
            return new CopyOnWriteArrayList<>();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(coreDir))) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            if(object == null || object.getAsJsonArray("core") == null){
                return new CopyOnWriteArrayList<>();
            }
            for (JsonElement jsonElement : object.getAsJsonArray("core")) {
                history.add(gson.fromJson(jsonElement, PlayerOwnedShop.HistoryLog.class));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return history;
    }

    public static void saveCore(List<?> core){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(coreDir))) {
            gson.toJson(gson.toJsonTree(core).getAsJsonObject(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveShop(PlayerOwnedShop history, String path){
        JsonObject object = new JsonObject();
        object.add("shop", gson.toJsonTree(history.getItems()));
        object.addProperty("earnings", history.getEarnings());
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))){
            gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveShopHistory(List<PlayerOwnedShop.HistoryItem> history, String path){
        JsonObject object = new JsonObject();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            gson.toJsonTree(history, List.class).getAsJsonArray();
            gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<PlayerOwnedShop.HistoryItem> loadShopHistory(String path) {
        List<PlayerOwnedShop.HistoryItem> history = new ArrayList<>();

        if (!Files.exists(Paths.get(path))) {
            return history;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            if (object != null) {
                JsonElement element = object.get("shop-history");
                if (element != null) {
                    Type type = new TypeToken<List<PlayerOwnedShop.HistoryItem>>() {}.getType();
                    history = gson.fromJson(element, type);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return history;
    }

    public static PlayerOwnedShop.ShopItem[] loadShop(String path) {
        try(BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            if(object != null && object.get("shop") != null) {
                return gson.fromJson(object.get("shop").getAsJsonArray(), PlayerOwnedShop.ShopItem[].class);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return new PlayerOwnedShop.ShopItem[]{};
    }

    public static long loadEarnings(Path path)  {
        if (!Files.exists(path)) {
            return 0L;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            JsonObject object = gson.fromJson(reader, JsonObject.class);
            if (object == null) {
                return 0L;
            }
            return object.get("earnings").getAsLong();
        } catch(IOException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    public static void main(String[] args) throws IOException {
        List<PlayerOwnedShop> SHOPS = new ArrayList<>();
        File DIRECTORY = new File("./data/saves/shops/");

        for (File file : Objects.requireNonNull(DIRECTORY.listFiles())) {
            Path path = Paths.get(DIRECTORY + File.separator, file.getName());
            PlayerOwnedShop shop = new PlayerOwnedShop();

            shop.setUsername(file.getName().replaceAll(".txt", ""));
            shop.getHistoryItems().clear();
            try (BufferedReader reader = Files.newBufferedReader(path)) {

                String line;
                int offset = 0;

                shop.setEarnings(Long.parseLong(reader.readLine()));

                while ((line = reader.readLine()) != null) {

                    String[] split = line.split(" - ");
                    if (split.length == 6) {

                        int id = Integer.parseInt(split[0]);
                        int amount = Integer.parseInt(split[1]);
                        long price = Long.parseLong(split[2]);
                        int maxAmount = Integer.parseInt(split[3]);
                        ItemEffect effect = ItemEffect.getEffectForName(split[4]);
                        int bonus = Integer.parseInt(split[5]);
                        shop.getItems()[offset++] = new PlayerOwnedShop.ShopItem(id, amount, price, maxAmount, effect, bonus);

                    }

                    String[] splitHistory = line.split(" _ ");
                 //   System.out.println(file.getName().replaceAll(".txt", "") + " "+ Arrays.toString(splitHistory) +" " + splitHistory.length);
                    if (splitHistory.length == 6) {
                        int id = Integer.parseInt(splitHistory[0]);
                        int amount = Integer.parseInt(splitHistory[1]);
                        long price = Long.parseLong(splitHistory[2]);
                        String buyer = splitHistory[3];
                        String effect = splitHistory[4];
                        int bonus = Integer.parseInt(splitHistory[5]);
                        shop.getHistoryItems().add(new PlayerOwnedShop.HistoryItem(id, amount, price, buyer, effect, bonus));
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            SHOPS.add(shop);
        }



        for(PlayerOwnedShop shop : SHOPS){

            if(!Files.exists(Paths.get("./data/saves/pos/shops/"+shop.getUsername()+"/")))
                Files.createDirectories(Paths.get("./data/saves/pos/shops/"+shop.getUsername()+"/"));


            saveShop(shop, "./data/saves/pos/shops/"+shop.getUsername()+"/"+ shop.getUsername()+".json");
            saveShopHistory(shop.getHistoryItems(), "./data/saves/pos/shops/"+shop.getUsername()+"/posHistory.json");
        }

      //  System.out.println("Loaded " + SHOPS.size() + " shops. "+SHOPS);
    }
}
