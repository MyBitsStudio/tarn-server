package com.ruse.world.content.tradingpost;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.world.content.tradingpost.models.Offer;
import com.ruse.world.content.tradingpost.persistance.Database;
import com.ruse.world.content.tradingpost.persistance.SQLDatabase;
import com.ruse.world.entity.impl.player.Player;

import java.util.HashMap;
import java.util.LinkedList;

public class TradingPost {

    public static final int MAIN_INTERFACE_ID = 150250;
    private static final int BUYING_INTERFACE_ID = 150440;

    private static final LinkedList<Offer> LIVE_OFFERS = new LinkedList<>();

    private static final Database DATABASE;

    static {
        DATABASE = new SQLDatabase();
    }

    private final Player player;
    private final HashMap<Integer, Offer> myOffers = new HashMap<>();
    private Item selectedItemToAdd;
    private int page;

    public TradingPost(Player player) {
        this.player = player;
    }

    public void openMainInterface() {
        for(int i = 0; i < 10; i++) {
            updateSlot(i);
        }
        player.getPacketSender().sendInterface(MAIN_INTERFACE_ID);
    }

    private void updateSlot(int slot) {
        Offer offer;
        if((offer = myOffers.get(slot)) != null) {
            sendOccupiedSlotData(offer, slot);
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
                .sendString(150310+slot, "")
                .sendString(150320+slot, "Price: " + offer.getPrice())
                .sendString(150330+slot, "")
                .updateProgressSpriteBar(150280+slot, 0, 100)
                .sendItemOnInterface(150290+slot, -1, 0);
    }

    private void selectSlot(int slot) {
        if(myOffers.get(slot) != null) {
            editSlot(slot);
            return;
        }
        allowItemAccept();
    }

    private void editSlot(int slot) {

    }

    private void allowItemAccept() {
        player.getPacketSender().sendMessage(":invglow1:");
        selectedItemToAdd = null;
       // player.getPacketSender().sendEnterAmountPrompt("How much would you like to sell ");
    }

    private void cancelSlot(int slot) {

    }

    public void selectItemToAdd(Item item) {
        selectedItemToAdd = item;
        player.getPacketSender().sendMessage(":invglow0:");

    }

    private void viewRecentOffers() {
        if(LIVE_OFFERS.isEmpty()) return;
        for(int i = 50; i > 0; i--) {
            Offer offer;
            if((offer = LIVE_OFFERS.getLast()) == null) break;
            player.getPacketSender().sendItemOnInterface(150647+(50-i), offer.getItemId(), offer.getAmountLeft());
        }
        player.getPacketSender().sendInterface(BUYING_INTERFACE_ID);
    }

    public boolean handleButtonClick(int id) {
        if(id >= 150260 && id <= 150269) {
            int slot = id - 150260;
            selectSlot(slot);
            return true;
        }
        switch (id) {
            case 150270 -> viewRecentOffers();
            default -> {
                return false;
            }
        }
        return true;
    }

    public static void addToLiveOffers(Offer offer) {
        LIVE_OFFERS.offer(offer);
    }

    public static void loadOffers() {
        DATABASE.loadOffers(LIVE_OFFERS);
    }
}
