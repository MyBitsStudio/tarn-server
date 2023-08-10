package com.ruse.world.packages.shops;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum Currency {

    TOKENS("coins", 995),
    DONATION("donation", -1),
    SLAYER("slayer", -1),
    VOTE("vote", -1),

    ;

    private final String name;
    private final int itemId;

    Currency(String name, int itemId) {
        this.name = name;
        this.itemId = itemId;
    }

    public static @Nullable Currency getCurrency(int itemId){
        for(Currency currency : Currency.values()){
            if(currency.getItemId() == itemId){
                return currency;
            }
        }
        return null;
    }

    public static @Nullable Currency getCurrency(String name){
        for(Currency currency : Currency.values()){
            if(currency.getName().equalsIgnoreCase(name)){
                return currency;
            }
        }
        return null;
    }

}
