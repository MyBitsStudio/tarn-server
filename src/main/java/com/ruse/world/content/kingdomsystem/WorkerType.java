package com.ruse.world.content.kingdomsystem;

import com.google.common.collect.ImmutableList;

public enum WorkerType {

    GOBLIN("Goblin", 25, 1);

    private transient final String name;
    private transient final int maxHungerLevel;
    private transient final int speed;

    WorkerType(String name, int maxHungerLevel, int speed) {
        this.name = name;
        this.maxHungerLevel = maxHungerLevel;
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public int getMaxHungerLevel() {
        return maxHungerLevel;
    }

    public int getSpeed() {
        return speed;
    }

    public static final ImmutableList<WorkerType> VALUES = ImmutableList.copyOf(values());
}
