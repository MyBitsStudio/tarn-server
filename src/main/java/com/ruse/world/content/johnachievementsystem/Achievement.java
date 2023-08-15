package com.ruse.world.content.johnachievementsystem;

import com.ruse.model.Item;

public enum Achievement {
    KILL_10_AVALON(165029, 10, 1, new Item(4151, 1), new Item(4151, 1));

    private final transient int componentId;
    private final transient int maxProgress;
    private final transient int points;
    private final transient Item[] rewards;

    Achievement(int componentId, int maxProgress, int points, Item... rewards) {
        this.componentId = componentId;
        this.points = points;
        this.maxProgress = maxProgress;
        this.rewards = rewards;
    }

    public int getComponentId() {
        return componentId;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public Item[] getRewards() {
        return rewards;
    }

    public int getPoints() {
        return points;
    }
}
