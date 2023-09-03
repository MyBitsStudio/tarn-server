package com.ruse.world.packages.ranks;

import lombok.Getter;

@Getter
public enum VIPRank {

    NONE(0, 1, 1, 0),
    BRONZE(1, 5, 1.2, 10),
    SILVER(2, 10, 1.4, 20),
    GOLD(3, 20, 1.8, 350),
    PLATINUM(4, 30, 2.1, 500),
    DIAMOND(5, 40,  2.5, 600),
    ONYX(6, 45,  3, 750),
    ZENYTE(7, 50,  4, 1000),
    SUPPORTER(8, 55,  5, 1500),
    SPONSOR(9, 60,  6, 2000),
    LEGENDARY(10, 65,  7, 2500)

    ;

    final int rank, dr, bonusCap;
    final double damage;
    VIPRank(int rank, int dr, double damage, int bonusCap){
        this.rank = rank;
        this.dr = dr;
        this.damage = damage;
        this.bonusCap = bonusCap;
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
