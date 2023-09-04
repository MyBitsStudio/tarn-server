package com.ruse.world.packages.ranks;

import lombok.Getter;

@Getter
public enum VIPRank {

    NONE(0, 1, 1, 0),
    BRONZE(1, 10, 1.1, 10),
    SILVER(2, 25, 1.2, 15),
    GOLD(3, 50, 1.3, 20),
    PLATINUM(4, 75, 1.5, 25),
    DIAMOND(5, 100,  1.75, 30),
    ONYX(6, 150,  2, 40),
    ZENYTE(7, 200,  2.2, 50),
    SUPPORTER(8, 250,  2.5, 60),
    SPONSOR(9, 300,  3, 75),
    LEGENDARY(10, 500,  5, 90)

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
