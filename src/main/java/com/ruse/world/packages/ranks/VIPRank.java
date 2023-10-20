package com.ruse.world.packages.ranks;

import lombok.Getter;

@Getter
public enum VIPRank {

    NONE(0, 1, 1, 0, ""),
    BRONZE(1, 10, 1.1, 10, "@blu@<shad=0>"),
    SILVER(2, 25, 1.2, 15, "@gre@<shad=0>"),
    GOLD(3, 50, 1.3, 20, "@red@<shad=0>"),
    PLATINUM(4, 75, 1.5, 25, "@whi@<shad=0>"),
    DIAMOND(5, 100,  1.75, 30, "@bla@<shad=0>"),
    ONYX(6, 150,  2, 40, "@bla@<shad=0>"),
    ZENYTE(7, 200,  2.2, 50, "@bla@<shad=0>"),
    SUPPORTER(8, 250,  2.5, 60, "@bla@<shad=0>"),
    SPONSOR(9, 300,  3, 75, "@bla@<shad=0>"),
    LEGENDARY(10, 500,  5, 90, "@bla@<shad=0>")

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
