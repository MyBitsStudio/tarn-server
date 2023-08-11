package com.ruse.world.packages.tradingpost;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.EnterAmount;
import com.ruse.util.Misc;
import com.ruse.world.packages.tradingpost.models.*;
import com.ruse.world.packages.tradingpost.persistance.Database;
import com.ruse.world.packages.tradingpost.persistance.SQLDatabase;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.dialogue.DialogueManager;
import com.ruse.world.packages.dialogue.impl.tp.CancelTPOptions;
import com.ruse.world.packages.dialogue.impl.tp.PurchaseTPStatement;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

public class TradingPost {
    public static final int CURRENCY_ID = 995;
    public static final int MAIN_INTERFACE_ID = 150250;
    public static final int ITEM_CONTAINER_ID = 19999;
    private static final int BUYING_INTERFACE_ID = 150440;

    private static final LinkedList<Offer> LIVE_OFFERS = new LinkedList<>();
    private static final HashMap<Integer, LinkedList<History>> ITEM_HISTORIES = new HashMap<>();
    private static final HashMap<String, Coffer> COFFERS = new HashMap<>();

    private static final Database DATABASE;

    static {
        DATABASE = new SQLDatabase();
    }

    private final Player player;
    private Item selectedItemToAdd;
    private int slotSelected;
    private int page;
    private List<Offer> offerList;
    private List<Offer> viewingOffers;
    private int searchedItem;
    public TradingPost(Player player) {
        this.player = player;
    }

    public void openMainInterface() {
        slotSelected = 0;
        searchedItem = -1;
        page = 0;
        offerList = getMyOffers();
        player.getPacketSender().sendString(150436, getCofferAmount(player.getUsername()) + " " + ItemDefinition.forId(CURRENCY_ID).getName()+"s");
        for(int i = 0; i < 10; i++) {
            updateSlot(i);
        }
        player.getPacketSender().sendInterface(MAIN_INTERFACE_ID);
    }

    private void updateSlot(int slot) {
        Optional<Offer> offerOptional = returnIfSlotOccupied(slot);
        if(offerOptional.isPresent()) {
            sendOccupiedSlotData(offerOptional.get(), slot);
            return;
        }
        sendEmptySlotData(slot);
    }

    private void sendEmptySlotData(int slot) {
        player.getPacketSender().sendString(150300+slot, "-")
                .sendString(150310+slot, "")
                .sendString(150320+slot, "Price: -")
                .sendString(150330+slot, "")
                .updateProgressSpriteBar(150280+slot, 0, 100)
                .sendItemOnInterface(150290+slot, -1, 0);
    }

    private void sendOccupiedSlotData(Offer offer, int slot) {
        player.getPacketSender().sendString(150300+slot, ItemDefinition.forId(offer.getItemId()).getName())
                .sendString(150310+slot, (offer.getInitialAmount() - offer.getAmountLeft()) + "/" + offer.getInitialAmount())
                .sendString(150320+slot, "Price: " + Misc.formatNumber(offer.getPrice()))
                .sendString(150330+slot, "="+ Misc.formatNumber(offer.getTotal())+" total")
                .updateProgressSpriteBar(150280+slot, (offer.getInitialAmount() - offer.getAmountLeft()),  offer.getInitialAmount())
                .sendItemOnInterface(150290+slot, offer.getItemId(), 1);
    }

    private void selectSlot(int slot) {
        Optional<Offer> offerOptional = returnIfSlotOccupied(slot);
        if(offerOptional.isPresent()) {
            showCancelOptions(slot);
            return;
        }
        slotSelected = slot;
        allowItemAccept();
    }

    private void showCancelOptions(int slot) {
        Optional<Offer> optionalOffer = getMyOffers()
                .stream()
                        .filter(it -> it.getSlot() == slot)
                                .findFirst();
        optionalOffer.ifPresent(offer -> DialogueManager.sendDialogue(player, new CancelTPOptions(player, ItemDefinition.forId(offer.getItemId()).getName(), slot), -1));
    }

    private void allowItemAccept() {
        selectedItemToAdd = null;
        player.getPacketSender().sendItemContainer(player.getInventory(), 151071);
        player.getPacketSender().sendInterfaceSet(MAIN_INTERFACE_ID, 151070);
    }

    public void cancelSlot(int slot) {
        Optional<Offer> optionalSlot = LIVE_OFFERS
                .stream()
                .filter(it -> slot == it.getSlot() && it.getSeller().equals(player.getUsername()))
                .findFirst();
        if(optionalSlot.isPresent()) {
            Offer offer = optionalSlot.get();
            removeFromLiveOffers(offer);
            player.addItemUnderAnyCircumstances(new Item(offer.getItemId(), offer.getAmountLeft()));
        } else {
            player.getPacketSender().sendMessage("@red@This item does not exist");
        }
        openMainInterface();
    }

    public void selectItemToAdd(Item item) {
        selectedItemToAdd = item;
        int amount;
        if((amount = player.getInventory().getAmount(item.getId())) < item.getAmount()) {
            selectedItemToAdd.setAmount(amount);
        }
        allowInputPrice();
    }

    private void offerItem(int price) {
        if(price * selectedItemToAdd.getAmount() <= 0) {
            player.getPacketSender().sendMessage("@red@Invalid price entered.");
            return;
        }
        Offer offer = new Offer(selectedItemToAdd.getId(), selectedItemToAdd.getAmount(), price, player.getUsername(), slotSelected, System.currentTimeMillis());
        player.getInventory().delete(selectedItemToAdd);
        addToLiveOffers(offer);
        openMainInterface();
    }

    private void allowInputPrice() {
         player.getPacketSender().sendEnterAmountPrompt("How much would you like to sell " + ItemDefinition.forId(selectedItemToAdd.getId()).getName() + " for?");
         player.setInputHandling(new EnterAmount() {
             @Override
             public void handleAmount(Player player, int amount) {
                 offerItem(amount);
             }
         });
    }

    private void viewBuyingPage() {
        if(LIVE_OFFERS.isEmpty()) {
            openMainInterface();
            player.getPacketSender().sendMessage("@red@The trading post contains no items.");
            return;
        }
        if(searchedItem == -1) {
            if(checkNextPage(LIVE_OFFERS)) {
                player.getPacketSender().sendMessage("@red@There are no more listings on the next page.");
                page--;
                return;
            }
            viewingOffers = LIVE_OFFERS.subList(Math.min(LIVE_OFFERS.size(), page * 50), Math.min(LIVE_OFFERS.size(), page * 50 + 50))
                    .stream().filter(offer -> !offer.getSeller().equalsIgnoreCase(player.getUsername())).toList();
        } else {
            viewingOffers = LIVE_OFFERS.
                    stream()
                    .filter(it -> it.getItemId() == searchedItem)
                    .toList();
            if(viewingOffers.isEmpty()) {
                player.getPacketSender().sendMessage("@red@There are no offers for " + ItemDefinition.forId(searchedItem).getName()+".");
                searchedItem = -1;
                page = 0;
                viewBuyingPage();
            } else {
                if(checkNextPage(viewingOffers)) {
                    player.getPacketSender().sendMessage("@red@There are no more listings on the next page.");
                    page--;
                    return;
                }
                viewingOffers = viewingOffers.subList(Math.min(viewingOffers.size(), page * 50), Math.min(viewingOffers.size(), page * 50 + 50))
                        .stream().filter(offer -> !offer.getSeller().equalsIgnoreCase(player.getUsername())).toList();
            }
        }
        sendBuyingPageData();
        player.getPacketSender().sendInterface(BUYING_INTERFACE_ID);
    }

    public boolean checkNextPage(List<Offer> offers) {
        return offers.size() < page * 50;
    }

    private void sendBuyingPageData() {
        Deque<Offer> deque = new ArrayDeque<>(viewingOffers);
        final int size = deque.size();
        for(int i = 0; i < 50; i++) {
            if(deque.size() > 0) {
                Offer offer = deque.pop();
                Timestamp timestamp = new Timestamp(offer.getTimestamp());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                int month = localDateTime.getMonthValue();
                int day = localDateTime.getDayOfMonth();
                int year = localDateTime.getYear();
                player.getPacketSender().sendItemOnInterface(150647 + i, offer.getItemId(), offer.getAmountLeft())
                        .sendString(150697 + i, ItemDefinition.forId(offer.getItemId()).getName())
                        .sendString(150747 + i, Misc.formatNumber(offer.getPrice()))
                        .sendString(150797 + i, month + "/" + day + "/"+ year);
            } else {
                player.getPacketSender().sendItemOnInterface(150647 + i, -1, 0)
                        .sendString(150697 + i, "")
                        .sendString(150747 + i, "")
                        .sendString(150797 + i, "");
            }
        }
        player.getPacketSender().sendString(150851, String.valueOf((page+1)));
        player.getPacketSender().sendMessage(":maxitems:"+size);
        player.getPacketSender().setScrollBar(150446, Math.max(221, size * 41));
    }

    private void selectPurchase(int index) {
        if(viewingOffers == null || viewingOffers.isEmpty()) {
            return;
        }
        Offer offer = viewingOffers.get(index);
        int amountLeft = offer.getAmountLeft();
        if(amountLeft == 0) return;
        if(amountLeft > 1) {
            player.getPacketSender().sendEnterAmountPrompt("How many of " + ItemDefinition.forId(offer.getItemId()).getName() + " would you like to buy?");
            player.setInputHandling(new EnterAmount() {
                @Override
                public void handleAmount(Player player, int amount) {
                    if(amount > offer.getAmountLeft()) {
                        amount = offer.getAmountLeft();
                    }
                    if((long)amount * offer.getPrice() > Integer.MAX_VALUE) {
                        player.getPacketSender().sendMessage("@red@Invalid amount entered.");
                        return;
                    }
                    DialogueManager.sendDialogue(player, new PurchaseTPStatement(player, offer, amount), -1);
                }
            });
        } else {
            DialogueManager.sendDialogue(player, new PurchaseTPStatement(player, offer, 1), -1);
        }
    }

    public void searchItem(int itemId) {
        page = 0;
        searchedItem = itemId;
        viewBuyingPage();
    }

    //@todo noted, stackable items
    public void purchase(Offer offer, int amount, int option) {
        if(option == 2) {
            viewBuyingPage();
            return;
        }
        if(COFFERS.get(offer.getSeller()) == null) {
            createCoffer(offer.getSeller());
        }
        int cofferAmount = getCofferAmount(offer.getSeller());
        int total = amount * offer.getPrice();
        if((long)cofferAmount+total > Integer.MAX_VALUE) {
            player.getPacketSender().sendMessage("@red@This seller cannot accept more " + ItemDefinition.forId(CURRENCY_ID).getName() + "s into their coffer.");
            viewBuyingPage();
            return;
        }
        if(player.getInventory().getAmount(CURRENCY_ID) < total) {
            player.getPacketSender().sendMessage("@red@You do not have enough coins to complete this transaction.");
            viewBuyingPage();
            return;
        }
        Optional<Offer> optionalOffer = containsOptional(offer);
        if(optionalOffer.isPresent()) {
            Offer toPurchase = optionalOffer.get();
            if(amount == toPurchase.getAmountLeft()) {
                removeFromLiveOffers(toPurchase);
            } else {
                toPurchase.incrementAmountSold(amount);
                updateOffer(toPurchase);
            }
            Coffer coffer = COFFERS.get(offer.getSeller());
            coffer.incrementAmount(total);
            updateCoffer(coffer);
            player.getInventory().delete(CURRENCY_ID, total);
            addToItemHistory(new History(toPurchase.getItemId(), amount, toPurchase.getPrice(), toPurchase.getSeller(), player.getUsername(), System.currentTimeMillis(), new Date(System.currentTimeMillis())));
            player.addItemUnderAnyCircumstances(new Item(toPurchase.getItemId(), amount));
            viewBuyingPage();
            return;
        }
        player.getPacketSender().sendMessage("@red@This item does not exist in the trading post anymore.");
        viewBuyingPage();
    }

    private void viewHistory(int index) {
        if(viewingOffers == null || viewingOffers.isEmpty() || index >= viewingOffers.size()) return;
        int itemId = viewingOffers.get(index).getItemId();
        sendHistoryData(itemId);
    }

    public void sendHistoryData(int itemId) {
        LinkedList<History> histories;
        if((histories = ITEM_HISTORIES.get(itemId)) == null) {
            player.getPacketSender().sendMessage("@red@This item has no history.");
            return;
        }
        Deque<History> deque = new ArrayDeque<>(histories);
        final int size = deque.size();
        for(int i = 0; i < 50; i++) {
            if(deque.size() > 0) {
                History history = deque.pop();
                Timestamp timestamp = new Timestamp(history.timestamp());
                LocalDateTime localDateTime = timestamp.toLocalDateTime();
                int month = localDateTime.getMonthValue();
                int day = localDateTime.getDayOfMonth();
                int year = localDateTime.getYear();
                player.getPacketSender().sendItemOnInterface(150917 + i, history.itemId(), history.amount())
                        .sendString(150967 + i, history.price() + " (ea)")
                        .sendString(151017 + i, month + "/" + day + "/"+ year);
            } else {
                player.getPacketSender().sendItemOnInterface(150917 + i, -1, 0);
            }
        }
        player.getPacketSender().sendInterfaceOverlay(BUYING_INTERFACE_ID, 150857);
        player.getPacketSender().sendMessage(":maxitems:"+size);
        player.getPacketSender().setScrollBar(150863, Math.max(247, size * 35));
    }

    private int getCofferAmount(String username) {
        return COFFERS.get(username) == null ? 0 : COFFERS.get(username).getAmount();
    }

    private void collectCoffer() {
        Coffer coffer;
        if((coffer = COFFERS.get(player.getUsername())) == null) {
            return;
        }
        int cofferAmount = getCofferAmount(player.getUsername());
        if(cofferAmount == 0) {
            return;
        }
        int inventoryAmount = player.getInventory().getAmount(CURRENCY_ID);
        if((long)inventoryAmount + cofferAmount > Integer.MAX_VALUE) {
            player.getPacketSender().sendMessage("@red@Please deposit some " + ItemDefinition.forId(CURRENCY_ID).getName() + " from your inventory and reclaim again.");
            return;
        }
        player.getInventory().add(CURRENCY_ID, cofferAmount);
        coffer.setAmount(0);
        updateCoffer(coffer);
        openMainInterface();
    }

    public boolean handleButtonClick(int id) {
        if(id >= 150260 && id <= 150269) {
            selectSlot(id - 150260);
            return true;
        } else if(id >= 150497 && id <= 150545) {
            selectPurchase(id - 150497);
        } else if(id >= 150547 && id <= 150595) {
            viewHistory( id - 150547);
        }
        switch (id) {
            case 150270, 150847 -> viewBuyingPage();
            case 150856 -> openMainInterface();
            case 150274, 150861, 150848 -> player.getPacketSender().sendMessage(":tsearch:");
            case 150279,150859 -> player.getPacketSender().removeOverlay();
            case 150434 -> collectCoffer();
            case 150432 -> player.getPacketSender().sendInterfaceOverlay(MAIN_INTERFACE_ID, 150857);
            case 150849 -> {
                page++;
                viewBuyingPage();
            }
            case 150850 -> {
                if(page != 0) {
                    page--;
                    viewBuyingPage();
                }
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    public static String getAverageValue(Item item) {
        LinkedList<History> historyLinkedList;
        if((historyLinkedList = ITEM_HISTORIES.get(item.getId())) == null || historyLinkedList.size() < 3) {
            return "@red@Not enough of this item has sold to calculate an average";
        }
        OptionalDouble average = historyLinkedList
                .stream()
                .mapToDouble(History::price)
                .average();
        if(average.isPresent()) {
            return "Average: @red@ " + Misc.formatNumber((long) average.getAsDouble());
        }
        return "This item has no current value";
    }

    public static void addToLiveOffers(Offer offer) {
        LIVE_OFFERS.offer(offer);
        DATABASE.createOffer(offer);
    }

    public static void removeFromLiveOffers(Offer offer) {
        LIVE_OFFERS.remove(offer);
        DATABASE.deleteOffer(offer);
    }

    public static void createCoffer(String username) {
        Coffer coffer = new Coffer(username, 0);
        COFFERS.put(username, coffer);
        DATABASE.createCoffer(coffer);
    }

    public static void updateCoffer(Coffer coffer) {
        DATABASE.updateCoffer(coffer);
    }

    public static void updateOffer(Offer offer) {
        DATABASE.updateOffer(offer);
    }

    public static void addToItemHistory(History history) {
        DATABASE.createHistory(history);
        LinkedList<History> histories = ITEM_HISTORIES.computeIfAbsent(history.itemId(), x -> new LinkedList<>());
        histories.add(history);
    }

    public static void loadOffers() {
        DATABASE.loadOffers(LIVE_OFFERS);
    }

    private static void loadCoffers() {
        DATABASE.loadCoffers(COFFERS);
    }

    private static void loadHistories() {
        DATABASE.loadHistory(ITEM_HISTORIES);
    }


    public static void load() {
        loadOffers();
        loadCoffers();
        loadHistories();
    }

    public List<Offer> getMyOffers() {
        return LIVE_OFFERS
                .stream()
                .filter(it -> it.getSeller().equals(player.getUsername()))
                .toList();
    }

    public Optional<Offer> returnIfSlotOccupied(int slot) {
        return offerList
                .stream()
                .filter(it -> it.getSlot() == slot)
                .findFirst();
    }

    public Optional<Offer> containsOptional(Offer offer) {
        return LIVE_OFFERS
                .stream()
                .filter(it -> it.getSlot() == offer.getSlot() && it.getItemId() == offer.getItemId() && Objects.equals(it.getSeller(), offer.getSeller()))
                .findFirst();
    }
}
