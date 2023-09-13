package com.ruse.world.packages.johnachievementsystem;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

@Getter
public enum AchievementDifficulty {
    BEGINNER(0, 1),
    EASY(1, 2),
    MEDIUM(2, 3),
    HARD(3, 5),
    ELITE(4, 8);

    private final int key;
    private final int points;

    AchievementDifficulty(int key, int points) {
        this.key = key;
        this.points = points;
    }

    public static ImmutableList<AchievementDifficulty> VALUES = ImmutableList.copyOf(values());
}
