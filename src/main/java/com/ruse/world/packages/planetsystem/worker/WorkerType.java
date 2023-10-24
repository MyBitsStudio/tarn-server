package com.ruse.world.packages.planetsystem.worker;

import com.google.common.collect.ImmutableList;

public enum WorkerType {

    GOBLIN("Goblin", 25, 1, 4151, 650),
    FARMER("Farmer",30,1, 6585, 1000),
    KNIGHT("Knight", 50, 1, 11732, 1250);

    private transient final String cleanName;
    private transient final int maxHungerLevel;
    private transient final int baseSpeed;
    private transient final int icon;
    private transient final int appetite;

    WorkerType(String name, int maxHungerLevel, int baseSpeed, int icon, int appetite) {
        this.cleanName = name;
        this.maxHungerLevel = maxHungerLevel;
        this.baseSpeed = baseSpeed;
        this.icon = icon;
        this.appetite = appetite;
    }

    public String getCleanName() {
        return cleanName;
    }

    public int getMaxHungerLevel() {
        return maxHungerLevel;
    }

    public int getBaseSpeed() {
        return baseSpeed;
    }

    public int getIcon() {
        return icon;
    }

    public static final ImmutableList<WorkerType> VALUES = ImmutableList.copyOf(values());

    public int getAppetite() {
        return appetite;
    }
}
