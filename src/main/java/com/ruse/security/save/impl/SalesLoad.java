package com.ruse.security.save.impl;

import com.google.gson.reflect.TypeToken;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.packages.donation.DonateSales;
import com.ruse.world.packages.donation.FlashDeals;

import java.util.List;
import java.util.Map;

public class SalesLoad extends SecureLoad {

    private DonateSales deals;
    public SalesLoad(DonateSales deals){
        this.deals = deals;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public SalesLoad run() {
        Map<Integer, String> labels = builder.fromJson(object.get("deals"),
                new TypeToken<Map<Integer, String>>() {
                }.getType());

        deals.setDeals(labels);

        deals.setIsActive(object.get("isActive").getAsBoolean());
        return this;
    }
}
