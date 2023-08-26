package com.ruse.world.packages.skills.slayer;

import lombok.Getter;

@Getter
public enum SlayerMasters {

    EASY(1597, 0, 1),
    MEDIUM(8275, 1, 40),
    HARD(9085, 2, 75),
    ELITE(925, 3, 92),
    SPECIAL(9000, 4, 99)

    ;

    private final int category, id, level;

    SlayerMasters(int id, int category, int level){
        this.id = id;
        this.category = category;
        this.level = level;
    }

    public static SlayerMasters forId(int id) {
        for (SlayerMasters master : SlayerMasters.values()) {
            if (master.id == id) {
                return master;
            }
        }
        return EASY;
    }

    public static SlayerMasters forCategory(int category) {
        for (SlayerMasters master : SlayerMasters.values()) {
            if (master.category == category) {
                return master;
            }
        }
        return EASY;
    }


}
