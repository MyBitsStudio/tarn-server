package com.ruse.world.content.tradingpost.persistance;

import com.ruse.world.content.tradingpost.models.Coffer;
import com.ruse.world.content.tradingpost.models.History;
import com.ruse.world.content.tradingpost.models.Offer;

import java.util.HashMap;
import java.util.LinkedList;

public interface Database {
    void createOffer(Offer offer);
    void createCoffer(Coffer coffer);
    void updateCoffer(Coffer coffer);
    void loadOffers(LinkedList<Offer> offerList);
    void deleteOffer(Offer offer);
    void updateOffer(Offer offer);
    void createHistory(History history);
    void loadHistory(HashMap<Integer, LinkedList<History>> histories);
    void loadCoffers(HashMap<String, Coffer> cofferHashMap);
}
