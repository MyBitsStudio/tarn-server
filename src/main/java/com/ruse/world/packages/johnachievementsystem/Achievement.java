package com.ruse.world.packages.johnachievementsystem;

import lombok.Getter;

@Getter
public class Achievement {

    private int primaryKey;
    private String title;
    private AchievementDifficulty difficulty;
    private String description;
    private int maxProgress;
    private Reward[] rewards;
    private int componentId;

    public AchievementDifficulty getAchievementDifficulty() {
        return difficulty;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setAchievementDifficulty(AchievementDifficulty achievementDifficulty) {
        this.difficulty = achievementDifficulty;
    }

    public void setRewards(Reward[] rewards) {
        this.rewards = rewards;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(AchievementDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }
}
