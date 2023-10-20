package com.ruse.world.packages.ranks;

import lombok.Getter;

@Getter
public enum VIPRank {

    NONE(0, 1, 1, 0, ""),
    BRONZE(1, 50, 1.1, 10, "@blu@<shad=0>"),
    SILVER(2, 100, 1.2, 15, "@gre@<shad=0>"),
    GOLD(3, 150, 1.3, 20, "@red@<shad=0>"),
    PLATINUM(4, 200, 1.5, 25, "@whi@<shad=0>"),
    DIAMOND(5, 250,  1.75, 30, "@bla@<shad=0>"),
    ONYX(6, 400,  2, 40, "@bla@<shad=0>"),
    ZENYTE(7, 600,  2.2, 50, "@bla@<shad=0>"),
    SUPPORTER(8, 900,  2.5, 60, "@bla@<shad=0>"),
    SPONSOR(9, 1200,  3, 75, "@bla@<shad=0>"),
    LEGENDARY(10, 1500,  5, 90, "@bla@<shad=0>")

    ;

    final int rank, dr, bonusCap;
    final double damage;
    final String prefix;
    VIPRank(int rank, int dr, double damage, int bonusCap, String prefix){
        this.rank = rank;
        this.dr = dr;
        this.damage = damage;
        this.bonusCap = bonusCap;
        this.prefix = prefix;
    }

    public static VIPRank forRank(int rank){
        for(VIPRank r : values()){
            if(r.getRank() == rank){
                return r;
            }
        }
        return null;
    }

}
