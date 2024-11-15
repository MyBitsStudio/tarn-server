package com.ruse.world.packages.johnachievementsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ruse.model.Item;
import com.ruse.world.World;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AchievementHandler {

    public static final HashMap<Integer, Achievement> achievements = new HashMap<>();


    public static void sendInterface(Player player){
        if(!World.attributes.getSetting("achievements")){
            player.sendMessage("Achievements are currently disabled.");
            return;
        }
        player.getPacketSender().sendInterface(165001);
        player.getPacketSender().sendString(165024, String.valueOf(player.getAchievementPoints()));
        player.getPacketSender().sendString(165339, "You can spend your achievement points in the store to unlock exclusive perks with amazing benefits!");
    }

    public static void progress(Player player, int amount, int... primaryKeys) {
        if(!World.attributes.getSetting("achievements")){
            return;
        }
        for (int key : primaryKeys) {
            Achievement achievement = achievements.get(key);
            if (achievement == null) {
                player.getPacketSender().sendMessage("@red@This achievement does not exist");
                continue;
            }
            AchievementProgress ap = player.getAchievementsMap().get(key);
            if (ap.isComplete()) continue;
            ap.addProgressAmount(amount);
            if (ap.getProgress() >= achievement.getMaxProgress()) {
                ap.setComplete(true);
                increaseCompleteAmount(player, achievement.getAchievementDifficulty(), 1);
                ap.setProgress(achievement.getMaxProgress());
                int pointsAwarded = achievement.getAchievementDifficulty().getPoints();
                player.addAchievementPoints(pointsAwarded);
                player.sendMessage("@red@You have been awarded some items and " + pointsAwarded + " for completing an achievement!");
                for (Reward item : achievement.getRewards())
                    player.addItemUnderAnyCircumstances(new Item(item.getItemId(), item.getAmount()));
            }
            sendProgressAmount(player, ap, achievement);
        }
    }

    private static void sendProgressAmount(Player player, AchievementProgress achievementProgress, Achievement achievement) {
        player.getPacketSender().sendMessage("achP#"+achievement.getComponentId()+","+achievementProgress.getProgress());
    }

    private static void increaseCompleteAmount(Player player, AchievementDifficulty difficulty, int amount) {
        player.getPacketSender().sendMessage("achC#"+difficulty.getKey() + "," + amount);
    }

    public static void sendPerkUnlock(Player player, PerkType perkType) {
        player.getPacketSender().sendMessage("achPP#"+player.getPerks().computeIfAbsent(perkType, x -> new Perk()).hasUnlocked()+","+perkType.getKey());
    }

    public static void sendPerkLevel(Player player, PerkType perkType) {
        player.getPacketSender().sendMessage("achPL#"+player.getPerks().computeIfAbsent(perkType, x -> new Perk()).getLevel()+","+perkType.getKey());
    }

    public static void onPlayerLogin(Player player) {
        achievements.values()
                .forEach(achievement -> {
                    AchievementProgress ap = player.getAchievementsMap().computeIfAbsent(achievement.getPrimaryKey(), x -> new AchievementProgress());
                    sendProgressAmount(player, ap, achievement);
                });
        AchievementDifficulty.VALUES.forEach(difficulty -> increaseCompleteAmount(player, difficulty, getCompletedAchievements(player, difficulty)));
        for(PerkType pt : PerkType.VALUES) {
            sendPerkUnlock(player, pt);
            sendPerkLevel(player, pt);
        }
    }

    private static int getCompletedAchievements(@NotNull Player player, AchievementDifficulty difficulty) {
        return (int) player.getAchievementsMap().entrySet()
                .stream()
                .filter(entry -> achievements.get(entry.getKey()).getAchievementDifficulty().equals(difficulty)
                        && entry.getValue().isComplete())
                .count();
    }

    public static boolean onButtonClick(Player player, int id) {
        if(id >= 165354 && id <= 165362) {
            player.setSelectedPerk(id - 165354);
            return true;
        }
        switch (id) {
            case 165340 -> {
                player.setSelectedPerk(0);
                return true;
            }
            case 165344 -> {
                selectUpgrade(player);
                return true;
            }
            case 165345 -> {
                selectBuy(player);
                return true;
            }
        }
        return false;
    }

    public static boolean hasUnlocked(@NotNull Player player, PerkType perkType) {
        // No need to check if null because hashmap gets populated on player login
        return player.getPerks().get(perkType) != null && player.getPerks().get(perkType).hasUnlocked();
    }

    public static int getPerkLevel(@NotNull Player player, PerkType perkType) {
        return player.getPerks().get(perkType).getLevel();
    }

    private static PerkType getSelectedPerkType(Player player) {
        return PerkType.VALUES.stream().filter(perkType_ ->  perkType_.getKey() == player.getSelectedPerk()).findFirst().orElse(null);
    }

    private static void selectUpgrade(Player player) {
        PerkType perkType = getSelectedPerkType(player);
        if(perkType == null) return;
        Perk perk = player.getPerks().get(perkType);
        if(!perk.hasUnlocked()) return;
        int currentLevel = perk.getLevel();
        if(currentLevel == 5) return;
        // now handle upgrading
        if(player.getAchievementPoints() <= costForLevel(currentLevel)) return;
        player.setAchievementPoints(player.getAchievementPoints() - costForLevel(currentLevel));

        // increasing level on upgrade
        perk.setLevel(currentLevel+1);

        // send this method on perk upgrade
        sendPerkLevel(player, perkType);
    }

    private static int costForLevel(int level) {
        return switch(level){
            case 1 -> 75;
            case 2 -> 100;
            case 3 -> 150;
            case 4 -> 200;
            default -> -1;
        };
    }

    private static void selectBuy(Player player) { // 50
        PerkType perkType = getSelectedPerkType(player);
        if(perkType == null) return;
        Perk perk = player.getPerks().get(perkType);
        if(perk.hasUnlocked()) return;
        // now handle buying
        if(player.getAchievementPoints() <= 50) return;
        player.setAchievementPoints(player.getAchievementPoints() - 50);
        // setting to level 1 automatically unlocks perk
        perk.setLevel(1);

        // send these two methods successful unlock
        sendPerkUnlock(player, perkType);
        sendPerkLevel(player, perkType);
    }

    public static void load() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            List<Achievement> achievementList = mapper.readValue(
                    new File("./.core/server/defs/achievements/achievements.yaml"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Achievement.class)
            );
            for (int i = 0; i < achievementList.size(); i++) {
                achievementList.get(i).setComponentId(165029 + i);
            }
            achievements.putAll(achievementList.stream().collect(Collectors.toMap(Achievement::getPrimaryKey, Function.identity())));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
