package com.ruse.world.content.johnachievementsystem;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

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

    private static void sendProgressAmount(Player player, AchievementProgress achievementProgress, Achievement achievement) {
        player.getPacketSender().sendMessage("achP#"+achievement.getComponentId()+","+achievementProgress.getProgress());
    }

    private static void increaseCompleteAmount(Player player, AchievementDifficulty difficulty, int amount) {
        player.getPacketSender().sendMessage("achC#"+difficulty.getKey() + "," + amount);
    }

    public static void onPlayerLogin(Player player) {
        Achievement.VALUES
                .forEach(achievement -> {
                    AchievementProgress ap = player.getAchievementsMap().computeIfAbsent(achievement, x -> new AchievementProgress());
                    sendProgressAmount(player, ap, achievement);
                });
        AchievementDifficulty.VALUES.forEach(difficulty -> increaseCompleteAmount(player, difficulty, getCompletedAchievements(player, difficulty)));
    }

    private static int getCompletedAchievements(Player player, AchievementDifficulty difficulty) {
        return (int) player.getAchievementsMap().entrySet()
                .stream()
                .filter(achievementAchievementProgressEntry -> achievementAchievementProgressEntry.getKey().getAchievementDifficulty().equals(difficulty)
                        && achievementAchievementProgressEntry.getValue().isComplete())
                .count();
    }
}
