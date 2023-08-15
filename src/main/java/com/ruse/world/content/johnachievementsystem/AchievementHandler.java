package com.ruse.world.content.johnachievementsystem;

import com.ruse.model.Item;
import com.ruse.world.entity.impl.player.Player;

public class AchievementHandler {

    public static void progress(Player player, int amount, Achievement achievement) {
        AchievementProgress ap = player.getAchievementsMap().computeIfAbsent(achievement, x -> new AchievementProgress());
        if(ap.isComplete()) return;
        ap.addProgressAmount(amount);
        if(ap.getProgress() >= achievement.getMaxProgress()) {
            ap.setComplete(true);
            ap.setProgress(achievement.getMaxProgress());
            player.addAchievementPoints(achievement.getPoints());
            player.sendMessage("@red@You have been awarded some items and " + achievement.getPoints() + " for completing an achievement!");
            for(Item item : achievement.getRewards())
                player.addItemUnderAnyCircumstances(item);
        }
    }
    
}
