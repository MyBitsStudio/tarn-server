package com.ruse.world.content.johnachievementsystem;

import com.google.common.collect.ImmutableList;
import com.ruse.model.Item;

public enum Achievement {
    BEG_1(165029,  1, AchievementDifficulty.BEGINNER, new Item(4151, 1), new Item(4151, 1)),
    EASY_1(165030,  1, AchievementDifficulty.EASY, new Item(4151, 1), new Item(4151, 1)),
    MED_1(165031,  1, AchievementDifficulty.MEDIUM, new Item(4151, 1), new Item(4151, 1)),
    HARD_1(165032,  1, AchievementDifficulty.HARD, new Item(4151, 1), new Item(4151, 1)),
    ELITE_1(165033, 1, AchievementDifficulty.ELITE, new Item(4151, 1), new Item(4151, 1));

    private final transient int componentId;
    private final transient int maxProgress;
    private final transient AchievementDifficulty achievementDifficulty;
    private final transient Item[] rewards;

    Achievement(int componentId, int maxProgress, AchievementDifficulty achievementDifficulty, Item... rewards) {
        this.componentId = componentId;
        this.maxProgress = maxProgress;
        this.achievementDifficulty = achievementDifficulty;
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

    public AchievementDifficulty getAchievementDifficulty() {
        return achievementDifficulty;
    }

    public static ImmutableList<Achievement> VALUES = ImmutableList.copyOf(values());
}
