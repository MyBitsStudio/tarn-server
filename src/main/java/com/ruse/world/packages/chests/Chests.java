package com.ruse.world.packages.chests;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum Chests {

    SLAYER_CHEST(63001, 63001),
    VOTE_CHEST(63000, 63000),
    PVM_CHEST(63002, 63002)
    ;

    private final int id, openId;

    Chests(int id, int openId) {
        this.id = id;
        this.openId = openId;
    }

    public static @Nullable Chests get(int id) {
        for (Chests chest : Chests.values()) {
            if (chest.getId() == id) {
                return chest;
            }
        }
        return null;
    }

    public static @Nullable Chests getOpen(int id) {
        for (Chests chest : Chests.values()) {
            if (chest.getOpenId() == id) {
                return chest;
            }
        }
        return null;
    }
}
