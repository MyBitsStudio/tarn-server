package com.ruse.world.packages.items.monic;

import lombok.Getter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@Getter
public enum Monic {

    ANCIENT(14505, "ancient-monic", new MonicPerk[]{
            new MonicPerk("2x XP 1HR", 3, "ancient-xp"),
            new MonicPerk("2x DR 1HR", 3, "ancient-dr"),
            new MonicPerk("2x DDR 1HR", 3, "ancient-ddr"),
            new MonicPerk("Perk Token", 3, "ancient-perk"),
            new MonicPerk("Enhance Pack I", 5, "ancient-enhance"),
            new MonicPerk("Ticket Pack I", 5, "ancient-tickets"),
            new MonicPerk("Blue Sweets x5", 3, "ancient-blue"),
            new MonicPerk("Deep Blue Sweets x5", 5, "ancient-deep-blue"),
            new MonicPerk("25k Coins", 1, "ancient-coins"),
    }),

    CRYSTAL(19640, "crystal-monic", new MonicPerk[]{
            new MonicPerk("Enhance Pack III", 5, "crystal-enhance"),
            new MonicPerk("Ticket Pack III", 5, "crystal-tickets"),
            new MonicPerk("Pet Enhancement Pack", 2, "crystal-pack"),
            new MonicPerk("Rare Perk Token", 3, "crystal-rare-perk"),
            new MonicPerk("White Sweets x5", 3, "crystal-white"),
            new MonicPerk("Pink Sweets x5", 5, "crystal-pinks"),
            new MonicPerk("5k Tokens", 1, "crystal-coins"),
            new MonicPerk("Prayer 1HR", 2, "crystal-prayer"),
            new MonicPerk("2x DMG 1HR", 2, "crystal-damage"),
    }),

    ;

    private final int itemId;
    private final MonicPerk[] perk;
    private final String name;
    Monic(int id, String name, MonicPerk[] perk){
        this.itemId = id;
        this.name = name;
        this.perk = perk;
    }

    @Contract(pure = true)
    public static @Nullable Monic getMonic(int id){
        for(Monic monic : values()){
            if(monic.itemId == id){
                return monic;
            }
        }
        return null;
    }

}
