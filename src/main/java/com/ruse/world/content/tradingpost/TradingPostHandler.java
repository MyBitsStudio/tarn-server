package com.ruse.world.content.tradingpost;

import java.util.ArrayList;
import java.util.List;

public class TradingPostHandler {

    private static final List<Offer> LIVE_OFFERS = new ArrayList<>();

    public static void addToLiveOffers(Offer offer) {
        LIVE_OFFERS.add(offer);
    }
}
