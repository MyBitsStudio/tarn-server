package com.ruse.security.save.impl;

import com.google.gson.reflect.TypeToken;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.donation.FlashDeals;

import java.util.List;
import java.util.Map;

public class FlashDealLoad extends SecureLoad {

    private FlashDeals deals;
    public FlashDealLoad(FlashDeals deals){
        this.deals = deals;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public FlashDealLoad run() {
        Map<Integer, Map<Integer, Integer>> labels = builder.fromJson(object.get("deals"),
                new TypeToken<Map<Integer, Map<Integer, Integer>>>() {
                }.getType());

        deals.setDeals(labels);

        Map<Integer, Map<Integer, String>> label = builder.fromJson(object.get("specialDeals"),
                new TypeToken<Map<Integer, Map<Integer, String>>>() {
                }.getType());

        deals.setSpecialDeals(label);

        List<Integer> labela = builder.fromJson(object.get("doubledItems"),
                new TypeToken<List<Integer>>() {
                }.getType());

        deals.setDoubledItems(labela);

        deals.setIsActive(object.get("isActive").getAsBoolean());
        return this;
    }
}
