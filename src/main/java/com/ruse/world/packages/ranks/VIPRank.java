package com.ruse.world.packages.ranks;

import lombok.Getter;

@Getter
public enum VIPRank {

    NONE(0, 1, 1, 0),
    BRONZE(1, 5, 1.2, 100),
    SILVER(3, 10, 1.4, 200),
    GOLD(3, 20, 1.8, 350),
    PLATINUM(4, 30, 2.1, 500),
    DIAMOND(5, 40,  2.5, 600),
    ONYX(6, 45,  3, 750),
    ZENYTE(7, 50,  4, 1000)

    ;

    final int rank, dr, bonusCap;
    final double damage;
    VIPRank(int rank, int dr, double damage, int bonusCap){
        this.rank = rank;
        this.dr = dr;
        this.damage = damage;
        this.bonusCap = bonusCap;
    }

}
