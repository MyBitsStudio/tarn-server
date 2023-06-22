package com.ruse.world.content.tradingpost;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TradingPostHandler {

    private static final List<Offer> LIVE_OFFERS = new ArrayList<>();
    private static final List<PastOffer> PAST_OFFERS = new CopyOnWriteArrayList<>();

    public static void addToLiveOffers(Offer offer) {
        LIVE_OFFERS.add(offer);
    }

    public static List<Offer> getLiveOffers() {
        return LIVE_OFFERS;
    }

    public static List<PastOffer> getPastOffers() {
        return PAST_OFFERS;
    }

    public static boolean addToPast(Offer offer){
        return PAST_OFFERS.add(new PastOffer(offer.getItemId(), offer.getItemEffect(), offer.getItemBonus(), offer.getAmountSold(), offer.getPrice(), offer.getSeller()));
    }

    public static long getAveragePrice(int itemId, String effect, int bonus){
        return (long) PAST_OFFERS.stream()
                .filter(offer -> offer.itemId() == itemId && offer.itemEffect().equals(effect) && offer.bonus() == bonus).mapToLong(PastOffer::price).average().orElse(1);
    }



}
