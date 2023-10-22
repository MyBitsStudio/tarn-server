package com.ruse.world.content.taskscrolls;

import java.util.Arrays;

public class PlayerTask {
    private final int[] restrictedWears;
    private final int taskKeyType;
    private final int npcTaskId;
    private final int completionAmount;
    private int progress;

    public PlayerTask(int[] restrictedWears, int taskKeyType, int npcTaskId, int completionAmount) {
        this.restrictedWears = restrictedWears;
        this.taskKeyType = taskKeyType;
        this.npcTaskId = npcTaskId;
        this.completionAmount = completionAmount;
    }

    public int[] getRestrictedWears() {
        return restrictedWears;
    }

    public int getTaskKeyType() {
        return taskKeyType;
    }

    public int getCompletionAmount() {
        return completionAmount;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getNpcTaskId() {
        return npcTaskId;
    }

    public void incrementProgress(int amount) {
        progress += amount;
    }

    public boolean isComplete() {
        return progress == completionAmount;
    }

    @Override
    public String toString() {
        return "PlayerTask{" +
                "restrictedWears=" + Arrays.toString(restrictedWears) +
                ", taskKeyType=" + taskKeyType +
                ", npcTaskId=" + npcTaskId +
                ", completionAmount=" + completionAmount +
                ", progress=" + progress +
                '}';
    }
}
