package com.ruse.world.packages.items.monic;

import lombok.Getter;

@Getter
public enum Monic {

    ANCIENT(14505, "ancient-monic", new MonicPerk[]{
            new MonicPerk("Donator Pack I", 3, "ancient-donator"),
            new MonicPerk("2x XP", 2, "ancient-xp"),
            new MonicPerk("2x DR", 2, "ancient-dr"),
            new MonicPerk("2x DDR", 2, "ancient-ddr"),
    }),
    CRYSTAL(19640, "crystal-monic", new MonicPerk[]{
            new MonicPerk("Donator Pack III", 3, "crystal-donator"),
            new MonicPerk("Limited Pack", 5, "crystal-limited"),
            new MonicPerk("Enhance Pack III", 4, "crystal-enhance"),
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

    public static Monic getMonic(int id){
        for(Monic monic : values()){
            if(monic.itemId == id){
                return monic;
            }
        }
        return null;
    }

}
