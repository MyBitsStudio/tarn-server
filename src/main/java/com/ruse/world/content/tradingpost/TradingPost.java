package com.ruse.world.content.tradingpost;

import com.ruse.model.Item;
import com.ruse.model.definitions.ItemDefinition;
import com.ruse.model.input.EnterAmount;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.util.Misc;
import com.ruse.world.content.dialogue.DialogueManager;
import com.ruse.world.content.tradingpost.dialogues.CancelOptions;
import com.ruse.world.content.tradingpost.models.Offer;
import com.ruse.world.content.tradingpost.persistance.Database;
import com.ruse.world.content.tradingpost.persistance.SQLDatabase;
import com.ruse.world.entity.impl.player.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TradingPost {

    public static final int MAIN_INTERFACE_ID = 150250;
    public static final int ITEM_CONTAINER_ID = 19999;
    private static final int BUYING_INTERFACE_ID = 150440;

    private static final LinkedList<Offer> LIVE_OFFERS = new LinkedList<>();

    private static final Database DATABASE;

    static {
        DATABASE = new SQLDatabase();
    }

    private final Player player;
    private Item selectedItemToAdd;
    private int slotSelected;
    private int page;
    private List<Offer> offerList;
    public TradingPost(Player player) {
        this.player = player;
    }

    public void openMainInterface() {
        slotSelected = 0;
        offerList = getMyOffers();
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
        optionalOffer.ifPresent(offer -> DialogueManager.start(player, new CancelOptions(player, ItemDefinition.forId(offer.getItemId()).getName(), slot)));
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
            DATABASE.deleteOffer(offer);
            LIVE_OFFERS.remove(offer);
            player.addItemUnderAnyCircumstances(new Item(offer.getItemId(), offer.getAmountLeft(), ItemEffect.getEffectForName(offer.getItemEffect()), offer.getItemBonus(), ItemEffect.getRarityForName(offer.getItemRarity())));
        } else {
            player.getPacketSender().sendMessage("@red@This item does not exist");
        }
        openMainInterface();
    }

    public void selectItemToAdd(Item item) {
        selectedItemToAdd = item;
        int amount;
        if((amount = player.getInventory().getAmount(item.getId(), item.getEffect(), item.getRarity(), item.getBonus())) < item.getAmount()) {
            selectedItemToAdd.setAmount(amount);
        }
        allowInputPrice();
    }

    private void offerItem(int price) {
        if(price * selectedItemToAdd.getAmount() <= 0) {
            player.getPacketSender().sendMessage("@red@Invalid price entered.");
            return;
        }
        Offer offer = new Offer(selectedItemToAdd.getId(), selectedItemToAdd.getBonus(), selectedItemToAdd.getEffect().name(), selectedItemToAdd.getRarity().name(), selectedItemToAdd.getAmount(), price, player.getUsername(), slotSelected);
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

    private void viewRecentOffers() {
        if(LIVE_OFFERS.isEmpty()) return;
        for(int i = 50; i > 0; i--) {
            Offer offer;
            if((offer = LIVE_OFFERS.getLast()) == null) break;
            player.getPacketSender().sendItemOnInterface(150647+(50-i), offer.getItemId(), offer.getAmountLeft());
        }
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

    public static String getAverageValue(Item item) {

        return "This item has no current value";
    }

    public static void addToLiveOffers(Offer offer) {
        LIVE_OFFERS.offer(offer);
        DATABASE.createOffer(offer);
    }

    public static void loadOffers() {
        DATABASE.loadOffers(LIVE_OFFERS);
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
}
