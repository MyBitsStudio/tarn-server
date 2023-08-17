package com.ruse.world.content.johnachievementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AchievementHandler {

    private static final int INTERFACE_ID = 165001;
    private static final int OVERLAY_ID = 165329;

    public static final List<Achievement> achievements = new ArrayList<>();

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
            for(Reward item : achievement.getRewards())
                player.addItemUnderAnyCircumstances(new Item(item.getItemId(), item.getAmount()));
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
        achievements
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

    public static boolean onButtonClick(Player player, int id) {
        return false;
    }

    public static void load() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            achievements.addAll(mapper.readValue(
                    new File("./.core/server/defs/achievements.yaml"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Achievement.class)
            ));
            for (int i = 0; i < achievements.size(); i++) {
                achievements.get(i).setComponentId(165029 + i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
