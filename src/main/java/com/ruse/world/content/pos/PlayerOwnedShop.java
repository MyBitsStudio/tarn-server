//package com.ruse.world.content.pos;
//
//import com.ruse.engine.GameEngine;
//import com.ruse.model.definitions.ItemDefinition;
//import com.ruse.model.projectile.ItemEffect;
//import com.ruse.net.packet.Packet.PacketType;
//import com.ruse.net.packet.PacketBuilder;
//import com.ruse.util.Misc;
//import com.ruse.world.World;
//import com.ruse.world.entity.impl.player.Player;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
///**
// * A class representing a single player owned shop. In this we hold and manage
// * all the items that are added or sold using an instance of this class. A
// * single instance of this class shows a single player owned shop in the manager
// * class.
// *
// * @author Nick Hartskeerl <apachenick@hotmail.com>
// */
//public class PlayerOwnedShop {
//
//    /**
//     * The total capacity of items a shop can contain.
//     */
//    public static final int SHOP_CAPACITY = 32;
//    /**
//     * The name of the player owning this player owned shop.
//     */
//    String username;
//    /**
//     * A collection of all the items in this player owned shop. If an item slot is
//     * empty this is represented as {@code null}, else as an {@link ShopItem}
//     * instance.
//     */
//    private ShopItem[] items = new ShopItem[SHOP_CAPACITY];
//    /**
//     * A collection of all the items in this player owned shop. If an item slot is
//     * empty this is represented as {@code null}, else as an {@link ShopItem}
//     * instance.
//     */
//    private List<HistoryItem> history = new ArrayList<>();
//    /**
//     * A reference to the player owning this shop. We use this reference to notify
//     * the shop owner of certain events.
//     */
//    private Player owner;
//    private long earnings;
//
//    private boolean updating = false;
//
//    public boolean isUpdating() {
//        return updating;
//    }
//
//    public void setUpdating(boolean updating) {
//        this.updating = updating;
//    }
//
//    public static void resetItems(Player player) {
//
//        for (int i = 0; i < SHOP_CAPACITY; i++) {
//
//            PacketBuilder out = new PacketBuilder(34, PacketType.SHORT);
//
//            out.putShort(32621);
//            out.put(i);
//            out.putShort(0);
//            out.put(0);
//            out.put(0);
//            player.getSession().queueMessage(out);
//
//        }
//
//    }
//
//    public void open(Player player) {
//        if(player.getUsername().equals(getUsername())){
//            updating = true;
//        }
//        player.getPacketSender().sendString(32610, "Player Owned Shop - " + username);
//        resetItems(player);
//        refresh(player, false);
//        player.getPacketSender().sendInterface(32600);
//    }
//
//    public void refresh(Player player, boolean myShop) {
//        player.getPacketSender().sendItemContainer(items, myShop ? 33621 : 32621);
//    }
//
//    public void add(int id, int amount, ItemEffect effect, int bonus) {
//
////        if(!updating)
////            updating = true;
////
////        ItemDefinition definition = ItemDefinition.forId(id);
////        long price = 0;
////
////        if (definition != null) {
////            price = definition.getValue();
////        }
////
////        add(id, amount, price, effect, bonus);
//
//    }
//
//    public void add(int id, int amount, long price, ItemEffect effect, int bonus) {
////        if(!updating)
////            updating = true;
////        add(new ShopItem(id, amount, price, amount, effect, bonus));
////        refreshAll();
////        save();
////        updating = false;
//    }
//
//    public void add(ShopItem item) {
//
////        for (int i = 0; i < items.length; i++) {
////            if (items[i] != null && (items[i].getId() == item.getId() && items[i].effect == item.effect && items[i].getBonus() == item.bonus)) {
////                items[i].setAmount(items[i].getAmount() + item.getAmount());
////                items[i].setMaxAmount(items[i].getMaxAmount() + item.getMaxAmount());
////                return;
////            }
////        }
////
////        int index = freeSlot();
////
////        if(item.price <= 5){
////            owner.sendMessage("You must set the amount higher.");
////            return;
////        }
////
////        if(item.price >= Integer.MAX_VALUE){
////            owner.sendMessage("You can't place an item with a price this high.");
////            return;
////        }
////
////        if (index != -1) {
////            if (items[index] == null) {
////                items[index] = item;
////                String itemName = item.getDefinition().getName();
////                owner.sendMessage("You have set <col=FF0000>" + (itemName == null ? "the merchandise" : itemName)
////                        + "</col> to cost <col=FF0000>" + Misc.sendCashToString(item.price)
////                        + "</col> Tokens in your shop.");
////
////                PlayerOwnedShopManager.addItem(item, owner.getUsername());
////            }
////        }
//
//    }
//
//    public int remove(int slot, int amount) {
//        if(!updating)
//            updating = true;
//        ShopItem item = getItem(slot);
//        int removed = -1;
//
//        if (item != null) {
//            if (amount >= item.getAmount()) {
//                PlayerOwnedShopManager.removeItem(item, getUsername());
//                items[slot] = null;
//                shift();
//                removed = item.getAmount();
//            } else {
//                item.setAmount(item.getAmount() - amount);
//                removed = amount;
//            }
//            save();
//            refreshAll();
//        }
//        updating = false;
//        return removed;
//
//    }
//
//    public void shift() {
//
//        List<ShopItem> temp = new ArrayList<>();
//
//        for (ShopItem item : items) {
//            if (item != null) {
//                temp.add(item);
//            }
//        }
//
//        items = temp.toArray(new ShopItem[SHOP_CAPACITY]);
//
//    }
//
//    public int freeSlot() {
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] == null) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    public ShopItem getItem(int slot) {
//        return items[slot];
//    }
//
//    public int getAmount(int id, ItemEffect effect, int bonus) {
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null && items[i].getId() == id && items[i].effect == effect  && items[i].getBonus() == bonus) {
//                return items[i].getAmount();
//            }
//        }
//        return 0;
//    }
//
//
//    public long getValueFromName(String name) {
//        long lowest = Long.MAX_VALUE;
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null && items[i].getDefinition() != null && items[i].getDefinition().getName() != null
//                    && items[i].getDefinition().getName().toLowerCase().contains(name) && items[i].price < lowest) {
//                lowest = items[i].price;
//            }
//        }
//        return lowest;
//    }
//
//    public boolean contains(int id) {
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null && items[i].getId() == id) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public int forSlot(int id) {
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null && items[i].getId() == id) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    public boolean contains(String name) {
//
//        if (name == null) {
//            return false;
//        }
//
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null && items[i].getDefinition() != null && items[i].getDefinition().getName() != null
//                    && items[i].getDefinition().getName().toLowerCase().contains(name)) {
//                return true;
//            }
//        }
//        return false;
//
//    }
//    public ArrayList<ShopItem> forName(String name) {
//
//        if (name == null) {
//            return null;
//        }
//        ArrayList<ShopItem> foundItems = new ArrayList<>();
//
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null && items[i].getDefinition() != null && items[i].getDefinition().getName() != null
//                    && items[i].getDefinition().getName().toLowerCase().contains(name)) {
//                foundItems.add(items[i]);
//            }
//        }
//        return foundItems;
//
//    }
//
//    public int size() {
//        int size = 0;
//        for (int i = 0; i < items.length; i++) {
//            if (items[i] != null) {
//                size++;
//            }
//        }
//        return size;
//    }
//
//    public void refreshAll() {
//        for (Player player : World.getPlayers()) {
//            if (player != null && player.getPlayerOwnedShopManager().getCurrent() == this) {
//                refresh(player, player.getPlayerOwnedShopManager().getMyShop() == this);
//            }
//        }
//    }
//
//    /**
//     * Access the online or offline player instance of the owner of this player
//     * owned shop. If the owner is indeed offline his/her details will be loaded
//     * from the saved serialized character file and reinterpreted to actual OOP
//     * objects to access the instance within the JVM.
//     *e player instance to the owner.
//     */
//    /*
//     * public Player accessOwner(boolean force) {
//     *
//     * if(owner == null && !force) {
//     *
//     * owner = World.getPlayerByName(username);
//     *
//     * Path path = Paths.get(GameSettings.getDataLocation()+"saves/characters/",
//     * username + ".json"); File file = path.toFile();
//     *
//     * if(owner == null && file.exists()) {
//     *
//     * Player player = new Player(null);
//     *
//     * player.setUsername(username);
//     * player.setLongUsername(NameUtils.stringToLong(username));
//     *
//     * PlayerLoading.getResult(player, true);
//     *
//     * return player;
//     *
//     * }
//     *
//     * }
//     *
//     * return owner;
//     *
//     * }
//     */
//
//    /*
//     * public void addMoney(long amount) {
//     *
//     * Player owner = accessOwner(false);
//     *
//     * if(owner != null) {
//     *
//     * owner.getPlayerOwnedShopManager().addEarnings(amount);
//     *
//     * //owner.getItems().sendItemToAnyTabOffline(995, amount, owner.isActive());
//     *
//     * if(!owner.isActive()) { owner.setShopUpdated(true); } else { String
//     * formatPrice = Misc.sendCashToString(amount);
//     * owner.sendMessage("<col=FF0000>You have earned "
//     * +formatPrice+" coins from your shop. Make sure to claim it.</col>"); }
//     *
//     * PlayerSaving.save(owner);
//     *
//     * }
//     *
//     * }
//     */
//    public void saveHistory() {
//
//        Path path = Paths.get("./data/saves/pos/shops/"+getUsername()+"/", "posHistory.json");
//
//        if (Files.notExists(path)) {
//            try {
//                Files.createDirectories(path.getParent());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        GameEngine.submit(() -> POSSaving.saveShopHistory(history, path.toString()));
//
////        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
////
//////            for (HistoryItem item : PlayerOwnedShopManager.ITEMS) {
//////                if (item != null) {
//////                    writer.write(item.getId() + " _ " + item.getAmount() + " _ " + item.getPrice() + " _ " + item.getBuyer() + " _ " + item.getEffect().toString() + " _ " + item.getBonus());
//////                    writer.newLine();
//////                }
//////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//    }
//    public void save() {
//        saveHistory();
//        Path path = Paths.get("./data/saves/pos/shops/"+getUsername()+"/", getUsername()+".json");
//
//        if (Files.notExists(path)) {
//            try {
//                Files.createDirectories(path.getParent());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        GameEngine.submit(() -> POSSaving.saveShop(this, path.toString()));
//        //POSSaving.saveShop(this, path.toString());
//
////        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
////            writer.write(Long.toString(this.earnings));
////            writer.newLine();
////            for (ShopItem item : getItems()) {
////                if (item != null) {
////                    writer.write(item.getId() + " - " + item.getAmount() + " - " + item.getPrice() + " - " + item.getMaxAmount() + " - " + item.effect.toString()  + " - " + item.bonus);
////                    writer.newLine();
////                }
////            }
////
////            writer.write("History:");
////            writer.newLine();
////            for (HistoryItem item : getHistoryItems()) {
////                if (item != null) {
////                    writer.write(item.getId() + " _ " + item.getAmount() + " _ " + item.getPrice() + " _ " + item.getBuyer() + " _ " + item.getEffect().toString()  + " _ " + item.getBonus());
////                    writer.newLine();
////                }
////            }
////
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//
//    }
//
//    /**
//     * Get a reference to a collection of all the items in this player owned shop.
//     * If an item slot is empty it will be shown as {@code null}, else as a
//     * {@link ShopItem} instance.
//     *
//     * @return The array of items in this player owned shop.
//     */
//    public ShopItem[] getItems() {
//        return items;
//    }
//    public List<HistoryItem> getHistoryItems() {
//        return history;
//    }
//
//    /**
//     * Set a new array of items to represent the collection of all items in this
//     * player owned shop. If an item slot is empty it must be shown as {@code null},
//     * else as a {@link ShopItem} instance.
//     *
//     * @param items The new array of items for this shop.
//     */
//    public void setItems(ShopItem[] items) {
//        this.items = items;
//    }
//
//    public void setHistory(List<HistoryItem> history) {
//        this.history = history;
//    }
//
//    public long getEarnings() {
//        return this.earnings;
//    }
//
//    public void setEarnings(long earnings) {
//        this.earnings = earnings;
//    }
//
//    public void addEarnings(long toAdd) {
//        this.earnings += toAdd;
//    }
//
//    /**
//     * Get the reference to the player instance of the owner of this shop. It is
//     * important to notice that with this reference the player instance can refer to
//     * an offline player. If you would like to gain access to the player owning this
//     * shop while this player is online or offline use the
//     *
//     * @return A reference to the player owning this shop.
//     */
//    public Player getOwner() {
//        return owner;
//    }
//
//    /**
//     * @param owner
//     */
//    public void setOwner(Player owner) {
//        this.owner = owner;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    @Getter
//    @Setter
//    public static class ShopItem {
//
//        private int id;
//
//        private int amount;
//
//        private int maxAmount;
//
//        private long price;
//
//        private ItemEffect effect = ItemEffect.NOTHING;
//
//        private int bonus;
//
//        public ShopItem(int id, int amount, int maxAmount) {
//            this.id = id;
//            this.amount = amount;
//            this.maxAmount = maxAmount;
//        }
//
//        public ShopItem(int id, int amount, long price, int maxAmount, ItemEffect effect, int bonus) {
//            this(id, amount, maxAmount);
//            this.price = price;
//            this.effect = effect;
//            this.bonus = bonus;
//        }
//
//        public ItemDefinition getDefinition() {
//            return ItemDefinition.forId(id);
//        }
//
//    }
//
//
//    @Getter
//    @Setter
//    public static class HistoryItem {
//        private int id, amount;
//        private String buyer;
//        private long price;
//        private String effect;
//        private int bonus;
//
//        public HistoryItem(int id, int amount, long price, String buyer, String effect, int bonus) {
//            this.id = id;
//            this.amount = amount;
//            this.price = price;
//            this.buyer = buyer;
//            this.effect = effect;
//            this.bonus = bonus;
//        }
//
//        public ItemDefinition getDefinition() {
//            return ItemDefinition.forId(id);
//        }
//
//    }
//
//    @Getter
//    @Setter
//    public static class HistoryLog {
//        private int id, amount;
//        private String buyer;
//        private long price;
//        private String effect;
//        private int bonus;
//        private String owner;
//        private long time;
//
//        public HistoryLog(int id, int amount, long price, String buyer, String effect, int bonus, String owner, long time) {
//            this.id = id;
//            this.amount = amount;
//            this.price = price;
//            this.buyer = buyer;
//            this.effect = effect;
//            this.bonus = bonus;
//            this.owner = owner;
//            this.time = time;
//        }
//
//        public ItemDefinition getDefinition() {
//            return ItemDefinition.forId(id);
//        }
//
//        public String getTimeFormat(){
//            return new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(time));
//        }
//
//    }
//}
