package com.ruse.world.packages.referral;

import com.ruse.model.Item;
import lombok.Getter;

@Getter
public enum PlayerRefers {

    LEVEL_1(1, new Item(995, 50_000),
            new Item(20501, 1), new Item(23254, 1)),
    LEVEL_2(2, new Item(995, 100_000),
            new Item(20501, 2), new Item(23254, 2)),
    LEVEL_3(3, new Item(995, 150_000),
            new Item(20501, 3), new Item(23254, 3)),
    LEVEL_4(4, new Item(995, 200_000),
            new Item(20501, 4), new Item(23254, 4)),
    LEVEL_5(5, new Item(995, 250_000), new Item(23256, 2),
            new Item(20502, 3), new Item(23255, 3)),

    LEVEL_6(6, new Item(995, 300_000), new Item(23149, 3),
            new Item(23148, 1)),
    LEVEL_7(7, new Item(995, 350_000), new Item(23149, 4),
            new Item(23148, 2)),
    LEVEL_8(8, new Item(995, 400_000), new Item(23149, 5),
            new Item(23148, 3)),
    LEVEL_9(9, new Item(995, 450_000), new Item(23148, 4),
            new Item(23147, 2)),
    LEVEL_10(10, new Item(995, 500_000), new Item(23148, 5),
            new Item(23147, 3), new Item(23257, 2),
            new Item(23057, 2)),
    ;

    private final int level;
    private final Item[] items;

    PlayerRefers(int level, Item... items){
        this.level = level;
        this.items = items;
    }

    public static PlayerRefers forLevel(int level){
        for(PlayerRefers refer : values()){
            if(refer.level == level){
                return refer;
            }
        }
        return null;
    }
}
