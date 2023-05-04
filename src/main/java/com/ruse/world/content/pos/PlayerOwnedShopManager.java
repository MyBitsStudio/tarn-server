package com.ruse.world.content.pos;

import com.ruse.engine.GameEngine;
import com.ruse.model.GameMode;
import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.Input;
import com.ruse.model.input.impl.PosItemInput;
import com.ruse.model.input.impl.PosPlayerInput;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.net.packet.PacketBuilder;
import com.ruse.util.Condition;
import com.ruse.util.Misc;
import com.ruse.world.content.PlayerLogs;
import com.ruse.world.content.dialogue.Dialogue;
import com.ruse.world.content.dialogue.DialogueExpression;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.content.dialogue.DialogueType;
import com.ruse.world.content.pos.PlayerOwnedShop.ShopItem;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A management class for all player owned shops and sendInformation related to a
 * player owned shop on player instance basis.
 *
 * @author Nick Hartskeerl <apachenick@hotmail.com>
 */
public class PlayerOwnedShopManager {

    /**
     * A collection of all player owned shops ever created.
     */
    public static final List<PlayerOwnedShop> SHOPS = new ArrayList<>();

    public static List<PlayerOwnedShop.HistoryItem> ITEMS = new CopyOnWriteArrayList<>();

    public static List<PlayerOwnedShop.HistoryLog> CORE_LOGS = new CopyOnWriteArrayList<>();

    public static final List<PlayerOwnedShop.HistoryItem> HISTORY_OF_BOUGHT = new ArrayList<>();

    public static final int NPC_ID = 4651;

    /**
     * The directory for the player owned shops
     */
    public static final File DIRECTORY = new File("./data/saves/pos/shops/");

    static {
        try {
            Files.createDirectories(DIRECTORY.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A reference to the player instance.
     */
    private final Player player;

    /**
     * The current player owned shop being visited by the player.
     */
    private PlayerOwnedShop current;

    /**
     * The player owned shop owned by the player relative to the current
     * {@link PlayerOwnedShopManager} instance.
     */
    private PlayerOwnedShop myShop;

    @Getter
    @Setter
    private Sorting sorting;

    /**
     * A collection of the shops filtered for this player's instance.
     */
    private final List<PlayerOwnedShop.HistoryItem> filtered = new ArrayList<>();

    private List<PlayerOwnedShop.HistoryItem> recentHistory = new ArrayList<>();


    @Getter
    @Setter
    private String filterPlayer = "";

    @Getter
    @Setter
    private String filterItem = "";

    /**
     * Construct a new {@code PlayerOwnedShopManager} {@code Object}.
     *
     * @param player The reference to the player owning this instance.
     */
    public PlayerOwnedShopManager(Player player) {
        this.player = player;
    }

    public static void loadHistory() {
        try {
            Misc.createFilesIfNotExist("./data/saves/pos/posHistory.json", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Thread
        GameEngine.submit(() -> ITEMS = POSSaving.loadHistory());
//        new Condition(
//                new Thread(() -> ITEMS = POSSaving.loadHistory()),
//                () -> ITEMS = new ArrayList<>(),
//                ITEMS != null
//        ).run();


    }

    public static void loadCore() {
        try {
            Misc.createFilesIfNotExist("./data/saves/pos/coreHistory.json", false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GameEngine.submit(() -> CORE_LOGS = POSSaving.loadHCore());

        //CORE_LOGS = POSSaving.loadHCore();


    }

    public static void loadShops() {
        loadHistory();
        loadCore();

        GameEngine.submit(() -> {
            for (File file : Objects.requireNonNull(DIRECTORY.listFiles())) {
                PlayerOwnedShop shop = new PlayerOwnedShop();

                for (File filez : Objects.requireNonNull(file.listFiles())) {
                    Path path = Paths.get(filez.getPath());

                    if (filez.getName().contains(".json") && !filez.getName().contains("posHistory")) {
                        shop.setUsername(filez.getName().replaceAll(".json", ""));
                        shop.setItems(POSSaving.loadShop(path.toString()));
                        shop.setEarnings(POSSaving.loadEarnings(path));
                    }

                    if (filez.getName().contains(".json") && filez.getName().contains("posHistory")) {
                        shop.setHistory(POSSaving.loadShopHistory(path.toString()));
                    }
                }

                if(shop.getHistoryItems() == null)
                    shop.setHistory(new ArrayList<>());

                SHOPS.add(shop);
            }
        });
    }

    public static void saveCore(){
        try {
            Misc.createFilesIfNotExist("./data/saves/pos/coreHistory.json", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Thread
       // GameEngine.submit(() -> POSSaving.saveCore(CORE_LOGS));

//        new Condition(
//                new Thread(() -> POSSaving.saveCore(CORE_LOGS)),
//                () -> POSSaving.saveCore(CORE_LOGS),
//                CORE_LOGS != null
//        ).run();
    }

    public static void saveShops() {
        for (PlayerOwnedShop shop : SHOPS) {
            shop.save();
        }
        saveCore();
    }

    public static void addItem(ShopItem item, String seller) {

        PlayerOwnedShop.HistoryItem historyItem = new PlayerOwnedShop.HistoryItem(item.getId(), item.getAmount(), item.getPrice(), seller, item.getEffect().toString(), item.getBonus());
        ITEMS.add(historyItem);

        if (ITEMS.size() >= 100)
            ITEMS.remove(0);


    }

    public static void removeItem(ShopItem item, String seller) {
        for (int i = 0; i < ITEMS.size(); i++) {
            if (ITEMS.get(i).getBuyer().equalsIgnoreCase(seller) && ITEMS.get(i).getId() == item.getId() && ItemEffect.getEffectForName(ITEMS.get(i).getEffect()) == item.getEffect()
            && ITEMS.get(i).getBonus() == item.getBonus()) {
                ITEMS.remove(i);
                return;
            }
        }
    }

    /**
     * Open the player owned shop management interface.
     */
    public void open() {

        if (player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN || player.getGameMode() == GameMode.GROUP_IRONMAN) {
            player.getPacketSender().sendMessage("Ironman players are not allowed to trade.");
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }
        player.getPacketSender().sendString(32610, "Player Owned Shops");

        int i = 0;
        filtered.clear();

        for (PlayerOwnedShop shop : SHOPS) {
            if (shop != null && shop.size() > 0) {

                player.getPacketSender().sendString(32623 + (i++), shop.username); // try this. ok test now.
                //filtered.add(shop);

            }

        }

        for (; i < 100; i++) {
            player.getPacketSender().sendString(32623 + i, "");
        }

        PlayerOwnedShop.resetItems(player);
        player.getPacketSender().sendInterface(32600);

    }

    public void openMain() {
        if (player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN || player.getGameMode() == GameMode.GROUP_IRONMAN) {
            player.getPacketSender().sendMessage("Ironman players are not allowed to trade.");
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }

        player.getPacketSender().sendString(113006, "" + Misc.insertCommasToNumber(player.getInventory().getAmount(ItemDefinition.TOKEN_ID)));

        int length = getMyShop() != null && getMyShop().getHistoryItems() != null && getMyShop().getHistoryItems().size() >= 4 ? getMyShop().getHistoryItems().size()  : 4;

        player.getPacketSender().setScrollBar(113100, length * 37);
        int interfaceID = 113151;
        for (int i = 0; i < length; i++) {
            if (getMyShop() != null && getMyShop().getHistoryItems() != null && getMyShop().getHistoryItems().size() > i) {
                PlayerOwnedShop.HistoryItem item = getMyShop().getHistoryItems().get(getMyShop().getHistoryItems().size() - i - 1);
                if (item != null) {
                    Item item_ = new Item(item.getId(), item.getAmount());
                    item_.setEffect(ItemEffect.getEffectForName(item.getEffect()));
                    item_.setBonus(item.getBonus());
                    player.getPacketSender().sendItemOnInterface(interfaceID++, item_);
                    player.getPacketSender().sendString(interfaceID++, "Sold to " + item.getBuyer());
                    player.getPacketSender().sendString(interfaceID++, "" + item.getPrice());
                    interfaceID += 2;
                }
            } else {
                player.getPacketSender().sendItemOnInterface(interfaceID++, -1, -1);
                player.getPacketSender().sendString(interfaceID++, "---");
                player.getPacketSender().sendString(interfaceID++, "---");
                interfaceID += 2;
            }
        }


        length = 30; //getMyShop() != null && getMyShop().getItems() != null && getMyShop().getItems().length >= 4 ? getMyShop().getItems().length + 1 : 4;

        player.getPacketSender().setScrollBar(113500, length * 59);
        int itemLength = 0;
        interfaceID = 113552;
        if (getMyShop() != null && getMyShop().getItems() != null) {
            for (int i = 0; i < getMyShop().getItems().length; i++) {
                ShopItem item = getMyShop().getItems()[i];
                if (item != null) {
                    Item item_ = new Item(item.getId(), item.getAmount());
                    item_.setEffect(item.getEffect());
                    item_.setBonus(item.getBonus());
                    player.getPacketSender().sendItemOnInterface(interfaceID++, item_);
                    player.getPacketSender().sendString(interfaceID++, item.getDefinition().getName());
                    player.getPacketSender().sendString(interfaceID++, "" + item.getPrice());
                    interfaceID++;
                    player.getPacketSender().sendProgressBar(interfaceID++, 0, (int) (((double) (item.getMaxAmount() - item.getAmount()) / (double) item.getMaxAmount()) * 100), 0);
                    player.getPacketSender().sendString(interfaceID++, (item.getMaxAmount() - item.getAmount()) + "/" + item.getMaxAmount());
                    player.getPacketSender().sendSpriteChange(interfaceID++, 1493);
                    player.getPacketSender().sendString(interfaceID++, "");
                    interfaceID++;
                    itemLength++;
                }
            }
        }

        for (; itemLength < length; itemLength++) {
            player.getPacketSender().sendItemOnInterface(interfaceID++, -1, -1);
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendString(interfaceID++, "");
            interfaceID += 2;
            player.getPacketSender().sendString(interfaceID++, "");
            player.getPacketSender().sendSpriteChange(interfaceID++, 1480);
            player.getPacketSender().sendString(interfaceID++, "Click to list an item\\nfor sale");
            interfaceID++;
        }


        player.getPacketSender().sendInterface(113000);

    }

    public void openListing() {
        if (player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN || player.getGameMode() == GameMode.GROUP_IRONMAN) {
            player.getPacketSender().sendMessage("Ironman players are not allowed to trade.");
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }


        if (filtered.size() > 0) {
            sortFiltered();

            int length = 100;
            player.getPacketSender().setScrollBar(114100, length * 35);
            int interfaceID = 114201;
            for (int i = 0; i < length; i++) {
                if (filtered.size() > i) {
                    PlayerOwnedShop.HistoryItem item = filtered.get(i);
                    Item item_ = new Item(item.getId(), item.getAmount());
                    item_.setEffect(ItemEffect.getEffectForName(item.getEffect()));
                    item_.setBonus(item.getBonus());
                    player.getPacketSender().sendItemOnInterface(interfaceID++, item_);
                    player.getPacketSender().sendString(interfaceID++, item.getDefinition().getName());
                    player.getPacketSender().sendString(interfaceID++, "" + item.getPrice());
                    player.getPacketSender().sendString(interfaceID++, "" + item.getBuyer());
                    player.getPacketSender().sendString(interfaceID++, "Open");
                } else {
                    player.getPacketSender().sendItemOnInterface(interfaceID++, -1, -1);
                    player.getPacketSender().sendString(interfaceID++, "");
                    player.getPacketSender().sendString(interfaceID++, "");
                    player.getPacketSender().sendString(interfaceID++, "");
                    player.getPacketSender().sendString(interfaceID++, "");
                }
            }
        } else {
            recentHistory = new ArrayList<>();
            recentHistory.addAll(ITEMS);
            Collections.reverse(recentHistory);


            int length = 100;
            player.getPacketSender().setScrollBar(114100, length * 35);
            int interfaceID = 114201;
            for (int i = 0; i < length; i++) {
                if (recentHistory.size() > i) {
                    PlayerOwnedShop.HistoryItem item = recentHistory.get(i);
                    Item item_ = new Item(item.getId(), item.getAmount());
                    item_.setEffect(ItemEffect.getEffectForName(item.getEffect()));
                    item_.setBonus(item.getBonus());
                    player.getPacketSender().sendItemOnInterface(interfaceID++,item_);
                    player.getPacketSender().sendString(interfaceID++, item.getDefinition().getName());
                    player.getPacketSender().sendString(interfaceID++, "" + item.getPrice());
                    player.getPacketSender().sendString(interfaceID++, "" + item.getBuyer());
                    player.getPacketSender().sendString(interfaceID++, "Open");
                } else {
                    player.getPacketSender().sendItemOnInterface(interfaceID++, -1, -1);
                    player.getPacketSender().sendString(interfaceID++, "");
                    player.getPacketSender().sendString(interfaceID++, "");
                    player.getPacketSender().sendString(interfaceID++, "");
                    player.getPacketSender().sendString(interfaceID++, "");
                }
            }
        }

        player.getPacketSender().sendInterface(114000);

    }

    public void sortFiltered() {

        if (getSorting() == null){
            setSorting(Sorting.PRICE);
        }
        if (getSorting().equals(Sorting.QUANTITY)) {
            Collections.sort(filtered, (shop, shop1) -> {
                int value1 = shop.getAmount();
                int value2 = shop1.getAmount();
                if (value1 == value2) {
                    return 0;
                } else if (value1 > value2) {
                    return 1;
                } else {
                    return -1;
                }
            });
        } else if (getSorting().equals(Sorting.PRICE)) {
            Collections.sort(filtered, (shop, shop1) -> {
                long value1 = shop.getPrice();
                long value2 = shop1.getPrice();
                if (value1 == value2) {
                    return 0;
                } else if (value1 > value2) {
                    return 1;
                } else {
                    return -1;
                }
            });
        } else if (getSorting().equals(Sorting.NAME)) {
            Collections.sort(filtered, (shop, shop1) -> shop.getDefinition().getName().compareToIgnoreCase(shop1.getDefinition().getName()));
        } else if (getSorting().equals(Sorting.SELLER)) {
            Collections.sort(filtered, (shop, shop1) -> shop.getBuyer().compareToIgnoreCase(shop1.getBuyer()));
        }

        if (getSorting().isAscending())
            Collections.reverse(filtered);
    }


    public void options() {

        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.OPTION;
            }

            @Override
            public DialogueExpression animation() {
                return null;
            }

            @Override
            public String[] dialogue() {
                return new String[]{"Open Main Interface", "Manage my own shop", "Claim earnings", "History", "Cancel"};
            }

            @Override
            public void specialAction() {
                player.setDialogueActionId(101);
            }

        });

    }

    /**
     * Open the interface to edit your own shop.
     */
    public void openEditor() {
        if (player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN || player.getGameMode() == GameMode.GROUP_IRONMAN) {
            player.getPacketSender().sendMessage("Ironman players are not allowed to trade.");
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }

        if (myShop == null) {
            myShop = new PlayerOwnedShop();
            myShop.setOwner(player);
            newShop(myShop);
        }

        (current = myShop).refresh(player, true);
        refreshInventory();

    }

    /**
     * Create a new player owned shop and assign the current player as owner of this
     * shop.
     *
     * @param shop A reference to the player owned shop.
     */
    public void newShop(PlayerOwnedShop shop) {
        shop.setUsername(player.getUsername());
        SHOPS.add(shop);
    }

    /**
     * Refresh the inventory of the player owning this player owned shop management
     * instance.
     */
    public void refreshInventory() {
        /*
         * for (int i = 0; i < player.getInventory().getItems().length; i++) { int id =
         * player.getInventory().getItems()[i].getId(), amount =
         * player.getInventory().getItems()[i].getAmount(); if (id <= 0 && amount <= 0)
         * { player.getPacketSender().sendItemOnInterface(37103 + i, -1, 0, 0); } else {
         * player.getPacketSender().sendItemOnInterface(37103 + i, id, 0, amount); } }
         */

        player.getPacketSender().sendItemContainer(player.getInventory(), 37154);

        if (player.getSession().getChannel().isOpen() && player != null) {
            PacketBuilder out = new PacketBuilder(248);
            out.putInt(33600);
            out.putShort(37153);
            player.getSession().queueMessage(out);
        }

    }

    public void handleFilter(int index) {

        if (getSorting().equals(Sorting.values()[index])) {
            getSorting().setAscending(!getSorting().isAscending());
        } else {
            setSorting(Sorting.values()[index]);
            getSorting().setAscending(true);
        }

        for (int i = 0; i < 4; i++) {
            if (getSorting().equals(Sorting.values()[i])) {
                player.getPacketSender().sendString(114010 + i, "@whi@" + (Misc.ucFirst(getSorting().name())));
                player.getPacketSender().sendSpriteChange(114014 + i, 1504 + (getSorting().isAscending() ? 0 : 1));
            } else {
                player.getPacketSender().sendString(114010 + i, (Misc.ucFirst(Sorting.values()[i].name())));
                player.getPacketSender().sendSpriteChange(114014 + i, 1498);
            }
        }

    }


    public void handleFilterHistory(int index) {

        if (getSorting().equals(Sorting.values()[index])) {
            getSorting().setAscending(!getSorting().isAscending());
        } else {
            setSorting(Sorting.values()[index]);
            getSorting().setAscending(true);
        }

        for (int i = 0; i < 4; i++) {
            if (getSorting().equals(Sorting.values()[i])) {
                player.getPacketSender().sendString(115008 + i, "@whi@" + (Misc.ucFirst(getSorting().name())));
                player.getPacketSender().sendSpriteChange(115012 + i, 1504 + (getSorting().isAscending() ? 0 : 1));
            } else {
                player.getPacketSender().sendString(115008 + i, (Misc.ucFirst(Sorting.values()[i].name())));
                player.getPacketSender().sendSpriteChange(115012 + i, 1498);
            }
        }

    }

    public void openStore(int index) {
        if ((filtered.size() > 0 && filtered.size() <= index) ||
                (filtered.size() == 0 && recentHistory.size() <= index)) {
            return;
        }
        PlayerOwnedShop.HistoryItem historyItem = (filtered.size() > 0 ? filtered : recentHistory).get(index);
        for (PlayerOwnedShop shops : SHOPS) {
            if (shops.getUsername().equalsIgnoreCase(historyItem.getBuyer())) {
                if (shops.isUpdating() && !player.getUsername().equals(shops.getUsername())) {
                    player.getPacketSender().sendMessage("This shop is currently being updated, please wait.");
                    return;
                }
                (current = shops).open(player);
                return;
            }
        }
    }

    /**
     * Handle a button on the management interface for this player.
     *
     * @param buttonId The button component id.
     */
    public boolean handleButton(int buttonId) {
        if (buttonId == 114008 || buttonId == 113009) {
            player.setInputHandling(new PosItemInput());
            player.getPacketSender().sendEnterInputPrompt("Item Name:");
            return true;
        }
        if (buttonId == 114009 || buttonId == 113010) {
            player.setInputHandling(new PosPlayerInput());
            player.getPacketSender().sendEnterInputPrompt("Player Name:");
            return true;
        }
        if (buttonId >= 114205 && buttonId <= 114700) {
            if (buttonId % 5 == 0) {
                openStore((buttonId - 114205) / 5);
                return true;
            }
        }

        if (buttonId >= 114010 && buttonId <= 114013) {
            handleFilter(buttonId - 114010);
            openListing();
            return true;
        }
        if (buttonId >= 115008 && buttonId <= 115011) {
            handleFilterHistory(buttonId - 115008);
            openHistory();
            return true;
        }
        if (buttonId == 113007) {
            claimEarnings();
            return true;
        }
        if (buttonId == 114006 || buttonId == -31925 || buttonId == 115006) {
            if(getMyShop() != null){
                getMyShop().setUpdating(false);
            }
            openMain();
            return true;
        }
        if (buttonId == 114007 || buttonId == 32611) {
            openListing();
            return true;
        }
        if (buttonId == 115007) {
            openHistory();
            return true;
        }
        if (buttonId == 113008) {
            filtered.clear();
            setSorting(Sorting.RECENT);
            openListing();
            return true;
        }
        if (buttonId >= 113559 && buttonId <= 113820) {
            openEditor();
            return true;
        }

        if (buttonId >= 32623 && buttonId <= 32722) {
/*
            buttonId -= 32623;

            boolean f = filtered.size() > 0;

            if (buttonId >= (f ? filtered : SHOPS).size()) {
                return false;
            }

            PlayerOwnedShop shop = (f ? filtered : SHOPS).get(buttonId);

            if (shop != null) {
                (current = shop).open(player);
            } else {
                PlayerOwnedShop.resetItems(player);
            }*/

        }
        return false;
    }

    /**
     * Handle the action to buy an item from a player owned shop for the player this
     * management instance is relative to.
     *
     * @param slot   The item slot.
     * @param id     The item id.
     * @param amount The amount he/she would like to buy of this item.
     */
    public void handleBuy(int slot, int id, int amount) {

        if (current == null) {
            return;
        }

        if(current.isUpdating()){
            player.getPacketSender().sendMessage("This shop is currently being updated, please wait.");
            return;
        }

        ShopItem item = current.getItem(slot);

        if (item == null) {
            return;
        }

        ItemDefinition definiton = item.getDefinition();

        if (amount == -1) {
            if (definiton != null) {
                String formatPrice = Misc.sendCashToString(item.getPrice());
                player.sendMessage("<col=FF0000>" + definiton.getName() + "</col> costs " + formatPrice
                        + " Tokens each in <col=FF0000>" + current.username + "</col>'s shop.");
            }
            return;
        }

        if (current == myShop) {
            player.sendMessage("You cannot buy items from your own shop.");
            return;
        }

        /*
         * if(Arrays.stream(GameSettings.UNSELLABLE_ITEMS).anyMatch(i -> i ==
         * item.getId())) { player.sendMessage("You cannot buy "+definiton.getName()
         * +" from this shop."); return; }
         */
        /*
         * if (player.getAchievements().isAchievementItem(id)) {
         * player.sendMessage("You cannot buy an achievement reward item!"); return; }
         */

        if (!(new Item(item.getId()).tradeable())) {
            player.sendMessage("You can't trade this item.");
            return;
        }

        if (System.currentTimeMillis() - player.lastPurchase < 1500) {
            player.getPacketSender().sendMessage("Please wait a few seconds between purchases.");
            return;
        }

        int currency = player.getInventory().getAmount(ItemDefinition.TOKEN_ID);
        int prevamount = amount;
        long coin = 0;
      //  System.out.println("amount: " + amount + " item.getAmount(): " + item.getAmount() + " currency: " + currency
             //   + " item.getPrice(): " + item.getPrice() + " coin: " + coin);
        if (((long) amount * item.getPrice()) > currency) {

            amount = (int) Math.round(((double) currency / item.getPrice()) - 0.3);

            if (amount < 0) {
                amount = 0;
            }

        }
       // System.out.println("Amount : "+amount);
        if (amount == 0) {
            // check if money pouch is helpful by checking if it has more than
            // in inv
            if (-1 > currency) {
                amount = prevamount;
                if (((long) amount * item.getPrice()) > coin) {

                    amount = (int) Math.round(((double) coin / item.getPrice()) - 0.3);

                    if (amount < 0) {
                        amount = 0;
                    }
                }
                if (amount == 0) {
                    player.sendMessage("You do not have enough Tokens in your pouch.");
                } else {
                    if (amount >= item.getAmount()) {
                        amount = item.getAmount();
                    }
                    if (!item.getDefinition().isStackable()
                            || (item.getDefinition().isStackable() && !player.getInventory().contains(item.getId()))) {
                        if (player.getInventory().getFreeSlots() == 0) {
                            player.sendMessage("Not enough inventory space.");
                            return;
                        } else if (player.getInventory().getFreeSlots() < amount) {
                            amount = player.getInventory().getFreeSlots();
                        }
                    } else {

                        int inventoryAmount = player.getInventory().getAmount(id);

                        if ((long) (amount + inventoryAmount) > Integer.MAX_VALUE) {
                            amount = Integer.MAX_VALUE - inventoryAmount;
                        }

                    }
                    // else, check off his pouch
                    if (-1 > (item.getPrice() * amount)) {
                        HISTORY_OF_BOUGHT.add(new PlayerOwnedShop.HistoryItem(id, amount, item.getPrice(), player.getUsername(), item.getEffect().toString(), item.getBonus()));
                        current.getHistoryItems().add(new PlayerOwnedShop.HistoryItem(id, amount, item.getPrice(), player.getUsername(), item.getEffect().toString(), item.getBonus()));
                        int removed = current.remove(slot, amount);
                        Item toAdd = new Item(item.getId(), removed);
                        toAdd.setEffect(item.getEffect());
                        toAdd.setBonus(item.getBonus());
                        player.getInventory().add(toAdd);
                        current.addEarnings(item.getPrice() * removed);
                        CORE_LOGS.add(new PlayerOwnedShop.HistoryLog(id, amount, item.getPrice(), player.getUsername(), item.getEffect().toString(), item.getBonus(), current.getUsername(), System.currentTimeMillis()));
                        saveCore();
                      //  System.out.println("HERE!");
                    }
                }
            } else
                player.sendMessage("You do not have enough Tokens in your inventory.");
        } else {

            if (amount >= item.getAmount()) {
                amount = item.getAmount();
            }

            if (!item.getDefinition().isStackable()
                    || (item.getDefinition().isStackable() && !player.getInventory().contains(item.getId()))) {
                if (player.getInventory().getFreeSlots() == 0) {
                    player.sendMessage("Not enough inventory space.");
                    return;
                } else if (player.getInventory().getFreeSlots() < amount) {
                    amount = player.getInventory().getFreeSlots();
                }
            } else {

                int inventoryAmount = player.getInventory().getAmount(id);

                if ((long) (amount + inventoryAmount) > Integer.MAX_VALUE) {
                    amount = Integer.MAX_VALUE - inventoryAmount;
                }

            }

            // if player has enough in his inv proceed
            if (player.getInventory().getAmount(ItemDefinition.TOKEN_ID) >= (item.getPrice() * amount)) {
                player.lastPurchase = System.currentTimeMillis();
                HISTORY_OF_BOUGHT.add(new PlayerOwnedShop.HistoryItem(id, amount, item.getPrice(), player.getUsername(), item.getEffect().toString(), item.getBonus()));
                current.getHistoryItems().add(new PlayerOwnedShop.HistoryItem(id, amount, item.getPrice(), player.getUsername(), item.getEffect().toString(), item.getBonus()));
                int removed = current.remove(slot, amount);
                int cashAmount = (int) item.getPrice() * removed;
                player.getInventory().delete(ItemDefinition.TOKEN_ID, cashAmount);
                Item toAdd = new Item(item.getId(), removed);
                toAdd.setEffect(item.getEffect());
                toAdd.setBonus(item.getBonus());
                player.getInventory().add(toAdd);
                current.addEarnings(item.getPrice() * removed);
                CORE_LOGS.add(new PlayerOwnedShop.HistoryLog(id, amount, item.getPrice(), player.getUsername(), item.getEffect().toString(), item.getBonus(), current.getUsername(), System.currentTimeMillis()));
                saveCore();

                PlayerLogs.log(player.getUsername(), "Player bought " + item.getId() + " x " + removed + " from "
                        + current.username + "'s pos shop for " + cashAmount + " Tokens");
                PlayerLogs.log(current.username, "Player sold " + item.getId() + " x " + removed + " to "
                        + player.getUsername() + " for " + cashAmount + " Tokens in their pos shop");
                if (current.getOwner() != null) {
                    current.getOwner().getPacketSender()
                            .sendMessage(player.getUsername() + " bought " + item.getAmount() + "x "
                                    + ItemDefinition.getDefinitions()[item.getId()].getName() + " for " + cashAmount
                                    + " Tokens from your shop");
                }
            }

        }
        current.save();
    }

    /**
     * Handle the withdraw action for this player's own shop.
     *
     * @param slot   The item slot.
     * @param id     The item id.
     * @param amount The amount the player would like to withdraw.
     */
    public void handleWithdraw(int slot, int id, int amount) {

        if (current != myShop) {
            return;
        }

        ShopItem item = current.getItem(slot);

        if(item == null) {
            player.sendMessage("@red@This shop item does not exist.");
            return;
        }

        ItemDefinition definiton = item.getDefinition();

        if (amount == -1) {
            if (definiton != null) {
                String formatPrice = Misc.sendCashToString(item.getPrice());
                player.sendMessage("<col=FF0000>" + definiton.getName() + "</col> is set to cost " + formatPrice
                        + " Tokens in your shop.");
            }
            return;
        }

        if (amount >= item.getAmount()) {
            amount = item.getAmount();
        }

        if (!item.getDefinition().isStackable()
                || (item.getDefinition().isStackable() && !player.getInventory().contains(item.getId()))) {
            if (player.getInventory().getFreeSlots() == 0) {
                player.sendMessage("Not enough inventory space.");
                return;
            } else if (player.getInventory().getFreeSlots() < amount) {
                amount = player.getInventory().getFreeSlots();
            }
        } else {

            int inventoryAmount = player.getInventory().getAmount(id);

            if ((long) (amount + inventoryAmount) > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE - inventoryAmount;
            }

        }

        if (amount == 0) {
            player.sendMessage("Not enough inventory space.");
            return;
        }

        int removed = current.remove(slot, amount);

        Item toAdd = new Item(item.getId(), removed);
        toAdd.setEffect(item.getEffect());
        toAdd.setBonus(item.getBonus());
        player.getInventory().add(toAdd);

        myShop.refresh(player, true);
        refreshInventory();

    }

    public void handleStore(int slot, int id, int amount) {
        handleStore(slot, id, amount, -1);
    }

    public void handleStore(int slot, int id, int amount, long price) {

        if (player.getInventory().get(slot) == null) {
            return;
        }

        if(!myShop.isUpdating()){
            myShop.setUpdating(true);
        }

        int itemId = player.getInventory().get(slot).getId();
        ItemEffect effect = player.getInventory().get(slot).getEffect();
        int bonus = player.getInventory().get(slot).getBonus();
        int itemAmount = player.getInventory().getAmount(itemId, effect, bonus);

      //  System.out.println("Item id: " + itemId + " effect: " + effect + " bonus: " + bonus);

        if(effect == null) {
            player.sendMessage("@red@Rarity or effect is null: contact staff");
            return;
        }

        if (itemId == id) {

            if (id == ItemDefinition.COIN_ID) {
                player.sendMessage("You cannot store Coins in your shop.");
                return;
            }
            if (id == ItemDefinition.TOKEN_ID) {
                player.sendMessage("You cannot store Tokens in your shop.");
                return;
            }

            ItemDefinition definition = ItemDefinition.forId(itemId);

            /*
             * Allow unsellable items but not untradables
             */
            /*
             * if(Arrays.stream(GameSettings.UNSELLABLE_ITEMS).anyMatch(i -> i == itemId)) {
             * if(definition != null) {
             * player.sendMessage("You cannot sell "+definition.getName()
             * +" in your shop."); } return; }
             */

            if (!(new Item(id).tradeable())) {
                player.sendMessage("You can't trade this item.");
                return;
            }

            /*
             * if (Pet.get(id) != null) { player.sendMessage("You cannot sell a pet item!");
             * return; }
             *
             * if (player.getAchievements().isAchievementItem(id)) {
             * player.sendMessage("You cannot trade an achievement reward item!" ); return;
             * }
             *
             * if (ClueDifficulty.isClue(id)) {
             * player.sendMessage("You cannot trade clue scrolls!"); return; }
             */

            if (amount >= itemAmount) {
                amount = itemAmount;
            }

            int currentAmount = myShop.getAmount(id, effect, bonus);
           // System.out.println("Current amount: " + currentAmount);
            if (currentAmount == 0 && price == -1) {

                final int amount2 = amount;

                player.setInputHandling(new Input() {

                    @Override
                    public void handleLongAmount(Player player, long value) {
                        handleStore(slot, id, amount2, value);
                    }

                });
                player.getPacketSender().sendEnterLongAmountPrompt("Enter the price you want to sell this for: (currency: Tokens)");

                return;

            }

            if (myShop.size() >= 32) {
                player.sendMessage("Your shop cannot contain any more items.");
                return;
            }

            if (currentAmount == Integer.MAX_VALUE) {
                player.sendMessage("You cannot store any more of this item in your shop.");
                return;
            }

            long total = ((long) currentAmount + (long) amount);

            if (total > Integer.MAX_VALUE) {
                amount = Integer.MAX_VALUE - currentAmount;
            }

            if (price == -1) {
                myShop.add(id, amount, effect, bonus);
            } else {
                myShop.add(id, amount, price, effect, bonus);
            }

            if (amount != 0) {
                player.getInventory().delete(new Item(id, amount, effect, bonus));
            }

            refreshInventory();

        }

    }

    public void setCustomPrice(int slot, int id, long price) {

        if (current != myShop) {
            return;
        }

        ShopItem item = current.getItem(slot);

        if (item == null) {
            return;
        }

        ItemDefinition definiton = item.getDefinition();

        if (price > 0 && price <= Long.MAX_VALUE) {
            item.setPrice(price);
            String formatPrice = Misc.sendCashToString(price);
            player.sendMessage("You have set <col=FF0000>" + definiton.getName() + "</col> to cost <col=FF0000>"
                    + formatPrice + "</col> Tokens in your shop.");
            myShop.save();

            for (int i = 0; i < ITEMS.size(); i++) {
                if (ITEMS.get(i).getBuyer().equalsIgnoreCase(myShop.getUsername()) && ITEMS.get(i).getId() == item.getId()) {
                    ITEMS.get(i).setPrice(price);
                    return;
                }
            }
        }

    }

    public void hookShop() {
        for (PlayerOwnedShop shop : SHOPS) {
            if (shop == null) {
                continue;
            }
            if (shop.getUsername().equalsIgnoreCase(player.getUsername())) {
                myShop = shop;
                shop.setOwner(player);
                break;
            }
        }
    }

    public void unhookShop() {

        if (myShop == null) {
            hookShop();
        }

        if (myShop != null) {
            myShop.setOwner(null);
        }

    }

    public void searchItem(String string) {
        filtered.clear();

        boolean l = string.length() == 0;

        for (PlayerOwnedShop shop : SHOPS) {
            if (shop != null && shop.size() > 0 && (l || shop.contains(string))) {
                ArrayList<ShopItem> list = shop.forName(string);
                for (ShopItem shopItem : list) {
                    PlayerOwnedShop.HistoryItem historyItem = new PlayerOwnedShop.HistoryItem(shopItem.getId(), shopItem.getAmount(), shopItem.getPrice(), shop.getUsername(), shopItem.getEffect().toString(), shopItem.getBonus());
                    filtered.add(historyItem);
                }
            }
        }

        if (filtered.size() == 0){
            player.sendMessage("@red@No results found. Displaying recent listings instead.");
        }

        openListing();
    }

    public void searchPlayer(String string) {
        filtered.clear();

        boolean l = string.length() == 0;

        for (PlayerOwnedShop shop : SHOPS) {
            if (shop != null && shop.size() > 0 && (l || shop.getUsername().toLowerCase().contains(string.toLowerCase()))) {
                for (ShopItem shopItem : shop.getItems()) {
                    if (shopItem != null) {
                        PlayerOwnedShop.HistoryItem historyItem = new PlayerOwnedShop.HistoryItem(shopItem.getId(), shopItem.getAmount(), shopItem.getPrice(), shop.getUsername(), shopItem.getEffect().toString(), shopItem.getBonus());
                        filtered.add(historyItem);
                    }
                }
            }
        }

        if (filtered.size() == 0){
            player.sendMessage("@red@No results found. Displaying recent listings instead.");
        }
        
        openListing();
    }

    private void statement(Player player, String... messages) {

        DialogueManager.start(player, new Dialogue() {

            @Override
            public DialogueType type() {
                return DialogueType.STATEMENT;
            }

            @Override
            public DialogueExpression animation() {
                return null;
            }

            @Override
            public String[] dialogue() {
                return messages;
            }

        });

    }

    public void claimEarnings() {
        if (myShop == null || myShop.getEarnings() <= 0) {
            player.sendMessage("@red@You do not currently have any available earnings.");
            return;
        }
        String formatPrice = Misc.sendCashToString(myShop.getEarnings());
        //player.setMoneyInPouch(player.getMoneyInPouch() + myShop.getEarnings());
        player.getInventory().add(ItemDefinition.TOKEN_ID, (int) myShop.getEarnings());
        //player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch());
        myShop.setEarnings(0);
        player.getPlayerOwnedShopManager().getMyShop().setEarnings(0);
        player.sendMessage("@red@You have claimed " + formatPrice + " Tokens into your inventory.");
        PlayerLogs.log(player.getUsername(), "Played claimed " + formatPrice + " Tokens from pos");
    }

    public PlayerOwnedShop getCurrent() {
        return current;
    }

    public void setCurrent(PlayerOwnedShop current) {
        this.current = current;
    }

    public PlayerOwnedShop getMyShop() {
        return myShop;
    }

    public void openHistory() {
        setSorting(Sorting.RECENT);

        if (player.getGameMode() == GameMode.IRONMAN || player.getGameMode() == GameMode.ULTIMATE_IRONMAN || player.getGameMode() == GameMode.GROUP_IRONMAN) {
            player.getPacketSender().sendMessage("Ironman players are not allowed to trade.");
            player.getPacketSender().sendInterfaceRemoval();
            return;
        }


      //  System.out.println("HISTORY_OF_BOUGHT: " + HISTORY_OF_BOUGHT.size());

        recentHistory = new ArrayList<>();
        recentHistory.addAll(HISTORY_OF_BOUGHT);
        Collections.reverse(recentHistory);

        int length = 100;
        player.getPacketSender().setScrollBar(115100, length * 35);
        int interfaceID = 115201;
        for (int i = 0; i < length; i++) {
            if (recentHistory.size() > i) {
                PlayerOwnedShop.HistoryItem item = recentHistory.get(i);
                Item item_ = new Item(item.getId(), item.getAmount());
                item_.setEffect(ItemEffect.getEffectForName(item.getEffect()));
                item_.setBonus(item.getBonus());
                player.getPacketSender().sendItemOnInterface(interfaceID++, item_);
                player.getPacketSender().sendString(interfaceID++, item.getDefinition().getName());
                player.getPacketSender().sendString(interfaceID++, "" + item.getPrice());
                player.getPacketSender().sendString(interfaceID++, "" + item.getBuyer());
            } else {
                player.getPacketSender().sendItemOnInterface(interfaceID++, -1, -1);
                player.getPacketSender().sendString(interfaceID++, "");
                player.getPacketSender().sendString(interfaceID++, "");
                player.getPacketSender().sendString(interfaceID++, "");
            }
        }

        player.getPacketSender().sendInterface(115000);
    }


    private enum Sorting {

        QUANTITY,
        NAME,
        PRICE,
        SELLER,
        RECENT,

        ;

        @Getter
        @Setter
        private boolean ascending = true;

        Sorting() {
        }
    }
}
