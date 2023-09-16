package com.ruse.world.packages.johnachievementsystem;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

@Getter
public enum AchievementDifficulty {
    BEGINNER(0, 2),
    EASY(1, 4),
    MEDIUM(2, 6),
    HARD(3, 8),
    ELITE(4, 10);

    private final int key;
    private final int points;

    AchievementDifficulty(int key, int points) {
        this.key = key;
        this.points = points;
    }

    public static ImmutableList<AchievementDifficulty> VALUES = ImmutableList.copyOf(values());
}
