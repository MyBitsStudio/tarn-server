package com.ruse.world.content.tradingpost.persistance;

import com.ruse.world.content.tradingpost.models.Coffer;
import com.ruse.world.content.tradingpost.models.Offer;

import java.util.List;

public interface Database {
    void createOffer(Offer offer);
    void createCoffer(Coffer coffer);
    void loadOffers(List<Offer> offerList);
}
