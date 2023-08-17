package com.ruse.world.content.johnachievementsystem;

public class Achievement {

    private String title;
    private String description;
    private transient int maxProgress;
    private transient AchievementDifficulty difficulty;
    private transient Reward[] rewards;
    private transient int componentId;

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
}
