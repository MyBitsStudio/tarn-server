package com.ruse.world.packages.starter;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum StartShopItems {

    // packs
    STARTER_PACK_I(15004, 5, 0, 10),
    STARTER_PACK_II(23300, 3, 0, 50),
    STARTER_PACK_III(23301, 1, 0, 100),
    RARE_CARD_I(22121, 10, 0, 25),
    ENHANCE_PACK_I(20500, 5, 0, 100),
    TICKET_PACK_I(23253, 3, 0, 100),
    DONATOR_PACK_I(23256, 2, 0, 500),
    DONATOR_PACK_II(23257, 1, 0, 1000),

        // items
    STARTER_BONES(2025, 100, 1, 5),
    PERK_TOKEN(23149, 3, 1, 100),
    COMMON_TOKEN(23148, 2, 1, 250),
    RARE_TOKEN(23147, 1, 1, 500),
    TRANSFER_TOKEN(8788, 5, 1, 25),
    STARDUST(13727, 250, 1, 5),

    //special

    LOOT_DEVICE(21819, 1, 2, 1000),

    //misc
    ;

    private final int id, cap, category, price;

    StartShopItems(int id, int cap, int category, int price){
        this.id = id;
        this.cap = cap;
        this.category = category;
        this.price = price;
    }

    public static @Nullable StartShopItems forId(int id){
        for(StartShopItems item : values()){
            if(item.getId() == id)
                return item;
        }
        return null;
    }

    public static List<StartShopItems> forCat(int cat){
        return Arrays.stream(values()).filter(item -> item.getCategory() == cat).collect(Collectors.toList());
    }
}
