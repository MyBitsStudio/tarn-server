package com.ruse.world.packages.johnachievementsystem;

import lombok.Getter;

@Getter
public class Perk {
    private int level;

    public boolean hasUnlocked() {
        return level > 0;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
