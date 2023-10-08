package com.ruse.world.packages.chests;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
public enum Chests {

    SLAYER_CHEST(35470, 35471)
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
