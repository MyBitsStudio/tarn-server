package com.ruse.world.content.johnachievementsystem;

public class Achievement {

    private int primaryKey;
    private String title;
    private AchievementDifficulty difficulty;
    private String description;
    private int maxProgress;
    private Reward[] rewards;
    private int componentId;

    public int getComponentId() {
        return componentId;
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public Reward[] getRewards() {
        return rewards;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AchievementDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(AchievementDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }
}
