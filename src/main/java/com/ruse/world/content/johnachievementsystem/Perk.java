package com.ruse.world.content.johnachievementsystem;

public class Perk {
    private int level;
    private int exp;

    public boolean hasUnlocked() {
        return level > 0;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
