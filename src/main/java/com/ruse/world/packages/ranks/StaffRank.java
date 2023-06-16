package com.ruse.world.packages.ranks;

import lombok.Getter;

@Getter
public enum StaffRank {

    PLAYER(0, 0, ""),
    YOUTUBER(1, 837, "<col=CD201F><shad=ffffff"),
    TRAIL_STAFF(2, 832,"<col=0><shad=FFFFFF>"),
    HELPER(3, 832,"<col=0><shad=FFFFFF>"),
    MODERATOR(4, 828, "<col=20B2AA><shad=0>"),
    ADMINISTRATOR(5, 829, "@yel@<shad=0>"),
    MANAGER(6, 829, "col=0><shad=B40404>"),
    DEVELOPER(7, 831, "@red@<shad=B40404>"),
    OWNER(8, 3385, "@red@<shad=FFFFFF>"),

    ;

    final int rank, img;
    final String yellPrefix;
    StaffRank(int rank, int image, String prefex){
        this.rank = rank;
        this.yellPrefix = prefex;
        this.img = image;
    }

    public static StaffRank forId(int id) {
        for (StaffRank rank : StaffRank.values()) {
            if (rank.rank == id) {
                return rank;
            }
        }
        return null;
    }

    public boolean isStaff() {
        return this != PLAYER;
    }

    public boolean isAdmin(){
        return this == ADMINISTRATOR || this == OWNER || this == DEVELOPER || this == MANAGER;
    }

    public boolean isDeveloper(){
        return this == DEVELOPER || this == OWNER;
    }

    public boolean isModPlus(){
        return this == MODERATOR || this == ADMINISTRATOR || this == OWNER || this == DEVELOPER || this == MANAGER;
    }

    public boolean isHelperPlus(){
        return this == HELPER || this == MODERATOR || this == ADMINISTRATOR || this == OWNER || this == DEVELOPER || this == MANAGER;
    }

    public boolean isYoutube(){
        return this == YOUTUBER;
    }



}
