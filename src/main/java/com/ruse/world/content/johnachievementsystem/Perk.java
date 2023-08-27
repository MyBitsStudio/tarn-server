package com.ruse.world.content.johnachievementsystem;

public class Perk {
    private int level;

    public boolean hasUnlocked() {
        return level > 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
