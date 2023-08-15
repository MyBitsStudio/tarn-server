package com.ruse.world.content.johnachievementsystem;

public class AchievementProgress {

    private int progress;
    private boolean complete;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void addProgressAmount(int amount) {
        progress += amount;
    }
}
