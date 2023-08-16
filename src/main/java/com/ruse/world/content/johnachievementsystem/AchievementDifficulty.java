package com.ruse.world.content.johnachievementsystem;

public enum AchievementDifficulty {
    BEGINNER(0, 1),
    EASY(1, 2),
    MEDIUM(2, 3),
    HARD(3, 4),
    ELITE(4, 5);

    private final int key;
    private final int points;

    AchievementDifficulty(int key, int points) {
        this.key = key;
        this.points = points;
    }

    public int getKey() {
        return key;
    }

    public int getPoints() {
        return points;
    }
}
