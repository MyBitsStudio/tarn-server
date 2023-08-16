package com.ruse.world.content.johnachievementsystem;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

import java.util.Map;

public class AchievementHandler {

    public static void progress(Player player, int amount, Achievement achievement) {
        AchievementProgress ap = player.getAchievementsMap().get(achievement);
        if(ap.isComplete()) return;
        ap.addProgressAmount(amount);
        if(ap.getProgress() >= achievement.getMaxProgress()) {
            ap.setComplete(true);
            increaseCompleteAmount(player, achievement.getAchievementDifficulty(), 1);
            ap.setProgress(achievement.getMaxProgress());
            int pointsAwarded = achievement.getAchievementDifficulty().getPoints();
            player.addAchievementPoints(pointsAwarded);
            player.sendMessage("@red@You have been awarded some items and " + pointsAwarded + " for completing an achievement!");
            for(Item item : achievement.getRewards())
                player.addItemUnderAnyCircumstances(item);
        }
        sendProgressAmount(player, ap, achievement);
    }

    public static void sendProgressAmount(Player player, AchievementProgress achievementProgress, Achievement achievement) {
        player.getPacketSender().sendMessage("achP#"+achievement.getComponentId()+","+achievementProgress.getProgress()+","+achievement.getMaxProgress());
    }

    public static void increaseCompleteAmount(Player player, AchievementDifficulty difficulty, int amount) {
        player.getPacketSender().sendMessage("achC#"+difficulty.getKey() + "," + amount);
    }

    public static void onPlayerLogin(Player player) {
        Achievement.VALUES
                .forEach(achievement -> {
                    AchievementProgress ap = player.getAchievementsMap().computeIfAbsent(achievement, x -> new AchievementProgress());
                    sendProgressAmount(player, ap, achievement);
                });
        int completedBeginner = 0, completedEasy = 0, completedMedium = 0, completedHard = 0, completedElite = 0;
        for(Map.Entry<Achievement, AchievementProgress> entry : player.getAchievementsMap().entrySet()) {
            if(entry.getValue().isComplete()) {
                switch (entry.getKey().getAchievementDifficulty()) {
                    case BEGINNER -> completedBeginner++;
                    case EASY -> completedEasy++;
                    case MEDIUM -> completedMedium++;
                    case HARD -> completedHard++;
                    case ELITE -> completedElite++;
                }
            }
        }
        increaseCompleteAmount(player, AchievementDifficulty.BEGINNER, completedBeginner);
        increaseCompleteAmount(player, AchievementDifficulty.EASY, completedEasy);
        increaseCompleteAmount(player, AchievementDifficulty.MEDIUM, completedMedium);
        increaseCompleteAmount(player, AchievementDifficulty.HARD, completedHard);
        increaseCompleteAmount(player, AchievementDifficulty.ELITE, completedElite);
    }
}
