package com.ruse.world.packages.donation;

import com.ruse.security.save.impl.FlashDealLoad;
import com.ruse.security.save.impl.SalesLoad;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DonateSales {

    private static DonateSales instance = new DonateSales();

    public static DonateSales getInstance() {
        if(instance == null) {
            instance = new DonateSales();
        }
        return instance;
    }

    private final AtomicBoolean isActive = new AtomicBoolean(false);
    private final Map<Integer, String> deals = new ConcurrentHashMap<>();

    public Map<Integer, String> getDeals() {
    	return deals;
    }

    public DonateSales() {
        load();
    }

    public void setIsActive(boolean active){
        isActive.set(active);
    }

    public boolean isActive(){
        return isActive.get();
    }

    public void setDeals(Map<Integer, String> deals){
        this.deals.clear();
        this.deals.putAll(deals);
    }

    private void load(){
        new SalesLoad(this).loadJSON("./.core/sales.json").run();
    }

    public void reload(){
        new SalesLoad(this).loadJSON("./.core/sales.json").run();
        System.out.println("Active Sales \n"+deals+"\nIs Active \n"+isActive());
    }
}
