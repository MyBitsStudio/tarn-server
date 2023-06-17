package com.ruse.world.packages.ranks;

import lombok.Getter;

@Getter
public enum DonatorRank {

    NONE(0, "", 1, 1, 1, 15, -1, 1, new int[]{0, 1}),
    GRACEFUL(1, "@blu@<shad=0>", 1.1, 1, 3, 10, 833, 2, new int[]{1, 5}),
    CLERIC(2, "@gre@<shad=0>", 1.15, 5, 5, 6, 834, 3, new int[]{3, 7}),
    TORMENTED(3, "@red@<shad=0>", 1.5, 10, 7, 3, 835, 4, new int[]{6, 10}),
    MYSTICAL(4, "@whi@<shad=0>", 1.75, 15, 10, 0, 836, 5, new int[]{9, 13}),
    OBSIDIAN(5, "@bla@<shad=0>", 2, 20, 12, 0, 830, 7.5, new int[]{11, 15}),
    FORSAKEN(6, "@bla@<shad=0>", 2.25, 30, 15, 0, 1508, 10, new int[]{13, 17}),
    ANGELIC(7, "@bla@<shad=0>", 2.5, 40, 20, 0, 0, 12.5, new int[]{16, 20}),
    DEMONIC(8, "@bla@<shad=0>", 3, 50, 25, 0, 0, 15,new int[]{20, 30})

    ;

    final String prefix;
    final int rank, xpgain, dr, yellDelay, img;
    final int[] slayer;
    final double damage, cap;
    DonatorRank(int rank, String prefix, double damage, int dr, int xp, int yell, int img, double cap, int[] slayer){
        this.rank = rank;
        this.prefix = prefix;
        this.damage = damage;
        this.dr = dr;
        this.xpgain = xp;
        this.slayer = slayer;
        this.yellDelay = yell;
        this.img = img;
        this.cap = cap;
    }

    public boolean isMember(){
        return this != NONE;
    }

    public boolean isClericPlus(){
        return this == CLERIC || this == TORMENTED || this == MYSTICAL || this == OBSIDIAN || this == FORSAKEN || this == ANGELIC || this == DEMONIC;
    }

    public boolean isTormentedPlus(){
        return this == TORMENTED || this == MYSTICAL || this == OBSIDIAN || this == FORSAKEN || this == ANGELIC || this == DEMONIC;
    }

    public boolean isMysticalPlus(){
        return this == MYSTICAL || this == OBSIDIAN || this == FORSAKEN || this == ANGELIC || this == DEMONIC;
    }

public boolean isObsidianPlus(){
        return this == OBSIDIAN || this == FORSAKEN || this == ANGELIC || this == DEMONIC;
    }

    public boolean isForsakenPlus(){
        return this == FORSAKEN || this == ANGELIC || this == DEMONIC;
    }
}
