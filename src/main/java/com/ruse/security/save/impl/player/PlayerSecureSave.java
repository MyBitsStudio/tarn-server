package com.ruse.security.save.impl.player;

import com.ruse.engine.GameEngine;
import com.ruse.io.ThreadProgressor;
import com.ruse.security.save.SecureSave;
import com.ruse.security.tools.SecurityUtils;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.mode.GameModeConstants;
import com.ruse.world.packages.mode.impl.*;

import java.io.FileWriter;
import java.io.IOException;

public class PlayerSecureSave extends SecureSave {

    private final Player player;

    public PlayerSecureSave(Player player) {
        this.player = player;
    }

    @Override
    public PlayerSecureSave create() {
        object.addProperty("username", player.getUsername());
        object.addProperty("total-play-time", player.getTotalPlayTime());

        object.addProperty("staff", player.getRank().name());
        object.addProperty("donator", player.getDonator().name());
        object.addProperty("vip", player.getVip().name());
        object.addProperty("game-mode", GameModeConstants.getMode(player));

        object.addProperty("yellhexcolor", player.getYellHex() == null ? "ffffff" : player.getYellHex());

        object.add("position", builder.toJsonTree(player.getPosition()));

        object.addProperty("online-status", player.getRelations().getStatus().name());
        object.addProperty("given-starter", player.didReceiveStarter());
        object.addProperty("has-pin2", player.getHasPin());
        object.addProperty("saved-pin2", player.getSavedPin());
        object.addProperty("donated", player.getAmountDonated());
        object.addProperty("current-boss-task", player.getCurrentBossTask());
        object.addProperty("current-boss-amount", player.getCurrentBossTaskAmount());
        object.addProperty("global-rate", player.getPointsHandler().getGlobalRate());
        object.addProperty("peng-rate", player.getPointsHandler().getPengRate());
        object.addProperty("slayer-rate", player.getPointsHandler().getSlayerRate());
        object.addProperty("has-completed-boss-task", player.isHasPlayerCompletedBossTask());
        object.addProperty("current-daily-task-id", player.getCurrentDailyTask() == null ? "" : player.getCurrentDailyTask());
        object.addProperty("current-daily-task-amount", player.getCurrentDailyTaskAmount());
        if (player.getDailyTasks() != null) {
            object.addProperty("current-daily-task-data", player.getDailyTasks().getNpcId());
        }
        object.addProperty("current-daily-task-x-pos", player.getxPosDailyTask());
        object.addProperty("current-daily-task-y-pos", player.getyPostDailyTask());
        object.addProperty("current-daily-task-z-pos", player.getzPosDailyTask());
        object.addProperty("current-daily-task-reward", player.getRewardDailyTask());

        object.add("uim-bank", builder.toJsonTree(player.getUimBankItems()));

        object.addProperty("daysVoted", player.getDaysVoted());
        object.addProperty("totalTimesClaimed", player.getTotalTimesClaimed());
        object.addProperty("longestDaysVoted", player.getLongestDaysVoted());
        object.addProperty("timeUntilNextReward", player.getTimeUntilNextReward());
        object.addProperty("lastVoted", player.getLastVoted() == null ? "" : player.getLastVoted());
        object.addProperty("current-voting-streak", player.getCurrentVotingStreak());
        object.addProperty("entriesCurrency", player.getEntriesCurrency());
        object.addProperty("amount-donated-today", player.getAmountDonatedToday());

        object.addProperty("last-time-reset", player.lastTimeReset);
        object.addProperty("is-secondary-equipment", player.isSecondaryEquipment());
        object.addProperty("lastlogin", player.lastLogin);
        object.addProperty("lastdailyclaim", player.lastDailyClaim);

        object.addProperty("minutes-bonus-exp", player.getMinutesBonusExp());
        object.addProperty("minutes-voting-dr", player.getMinutesVotingDR());
        object.addProperty("minutes-voting-dmg", player.getMinutesVotingDMG());
        object.addProperty("total-gained-exp", player.getSkillManager().getTotalGainedExp());

        object.addProperty("Skilling-points", player.getPointsHandler().getSkillingPoints());
        object.addProperty("prestige-points", player.getPointsHandler().getPrestigePoints());
        object.addProperty("achievement-points", player.getPointsHandler().getAchievementPoints());
        object.addProperty("dung-tokens", player.getPointsHandler().getDungeoneeringTokens());
        object.addProperty("commendations", player.getPointsHandler().getCommendations());

        object.addProperty("godmodetime", player.getGodModeTimer());
        object.addProperty("voting-points", player.getPointsHandler().getVotingPoints());
        object.add("tasks-completion", builder.toJsonTree(player.getStarterTaskAttributes().getCompletion()));
        object.add("tasks-progress", builder.toJsonTree(player.getStarterTaskAttributes().getProgress()));

//        object.addProperty("slayer-spree", player.getPointsHandler().getSlayerSpree());



        object.addProperty("event-points", player.getPointsHandler().getEventPoints());
        object.addProperty("boss-points", player.getPointsHandler().getBossPoints());
        object.addProperty("shilling-rate", player.getPointsHandler().getSHILLINGRate());

        object.addProperty("slayer-points", player.getPointsHandler().getSlayerPoints());
        object.addProperty("pk-points", player.getPointsHandler().getPkPoints());

        object.addProperty("gender", player.getAppearance().getGender().name());
        object.addProperty("spell-book", player.getSpellbook().name());
        object.addProperty("prayer-book", player.getPrayerbook().name());
        object.addProperty("running", player.isRunning());
        object.addProperty("run-energy", player.getRunEnergy());
        object.addProperty("music", player.musicActive());
        object.addProperty("sounds", player.soundsActive());
        object.addProperty("auto-retaliate", player.isAutoRetaliate());
        object.addProperty("xp-locked", player.experienceLocked());
        object.addProperty("veng-cast", player.hasVengeance());
        object.addProperty("last-veng", player.getLastVengeance().elapsed());
        object.addProperty("fight-type", player.getFightType().name());
        object.addProperty("sol-effect", player.getStaffOfLightEffect());
        object.addProperty("skull-timer", player.getSkullTimer());

        object.addProperty("accept-aid", player.isAcceptAid());
        object.addProperty("poison-damage", player.getPoisonDamage());
        object.addProperty("poison-immunity", player.getPoisonImmunity());
        object.addProperty("overload-timer", player.getOverloadPotionTimer());
        object.addProperty("aggro-timer", player.getAggroPotionTimer());
        object.addProperty("exp-timer", player.getExpPotionTimer());
        object.addProperty("dr-timer", player.getDrPotionTimer());
        object.addProperty("ddr-timer", player.getDdrPotionTimer());
        object.addProperty("dmg-timer", player.getDmgPotionTimer());

        object.addProperty("double-dr-timer", player.getDoubleDRTimer());
        object.addProperty("double-ddr-timer", player.getDoubleDDRTimer());
        object.addProperty("double-dmg-timer", player.getDoubleDMGTimer());

        object.addProperty("fire-immunity", player.getFireImmunity());
        object.addProperty("fire-damage-mod", player.getFireDamageModifier());
        object.addProperty("prayer-renewal-timer", player.getPrayerRenewalPotionTimer());
        object.addProperty("teleblock-timer", player.getTeleblockTimer());
        object.addProperty("special-amount", player.getSpecialPercentage());

        object.addProperty("summon-npc",
                player.getSummoning().getFamiliar() != null
                        ? player.getSummoning().getFamiliar().getSummonNpc().getId()
                        : -1);
        object.addProperty("summon-death",
                player.getSummoning().getFamiliar() != null
                        ? player.getSummoning().getFamiliar().getDeathTimer()
                        : -1);
        object.addProperty("process-farming", player.shouldProcessFarming());
        object.addProperty("clanchat", player.getClanChatName() == null ? "null" : player.getClanChatName().trim());
        object.addProperty("autocast", player.isAutocast());
        object.addProperty("autocast-spell",
                player.getAutocastSpell() != null ? player.getAutocastSpell().spellId() : -1);

        object.addProperty("slayer-master", player.getSlayer().getMaster().name());
        object.add("slayer-task", builder.toJsonTree(player.getSlayer().getTask()));
        object.add("slayer-last-task", builder.toJsonTree(player.getSlayer().getLastTask()));
        object.addProperty("slayer-streak", player.getSlayer().getStreak());

        object.add("killed-players", builder.toJsonTree(player.getPlayerKillingAttributes().getKilledPlayers()));
        object.addProperty("random-coffin",
                player.getMinigameAttributes().getBarrowsMinigameAttributes().getRandomCoffin());


        object.add("collection-data", builder.toJsonTree(player.getCollectionLogData()));
        object.add("holy-prayers-unlocked", builder.toJsonTree(player.getUnlockedHolyPrayers()));

        object.addProperty("has-bank-pin", player.getBankPinAttributes().hasBankPin());
        object.addProperty("last-pin-attempt", player.getBankPinAttributes().getLastAttempt());
        object.addProperty("invalid-pin-attempts", player.getBankPinAttributes().getInvalidAttempts());
        object.add("bank-pin", builder.toJsonTree(player.getBankPinAttributes().getBankPin()));
        object.add("appearance", builder.toJsonTree(player.getAppearance().getLook()));

        object.add("skills", builder.toJsonTree(player.getSkillManager().getSkills()));
        object.add("inventory", builder.toJsonTree(player.getInventory().getItems()));
        object.add("equipment", builder.toJsonTree(player.getEquipment().getItems()));
        object.add("secondary-equipment", builder.toJsonTree(player.getSecondaryEquipment().getItems()));
        object.add("preset-equipment", builder.toJsonTree(player.getPreSetEquipment().getItems()));

        object.add("bank-0", builder.toJsonTree(player.getBank(0).getValidItems()));
        object.add("bank-1", builder.toJsonTree(player.getBank(1).getValidItems()));
        object.add("bank-2", builder.toJsonTree(player.getBank(2).getValidItems()));
        object.add("bank-3", builder.toJsonTree(player.getBank(3).getValidItems()));
        object.add("bank-4", builder.toJsonTree(player.getBank(4).getValidItems()));
        object.add("bank-5", builder.toJsonTree(player.getBank(5).getValidItems()));
        object.add("bank-6", builder.toJsonTree(player.getBank(6).getValidItems()));
        object.add("bank-7", builder.toJsonTree(player.getBank(7).getValidItems()));
        object.add("bank-8", builder.toJsonTree(player.getBank(8).getValidItems()));

        object.add("new-uim-bank", builder.toJsonTree(player.getUIMBank().getValidItems()));


        object.add("friends", builder.toJsonTree(player.getRelations().getFriendList().toArray()));
        object.add("ignores", builder.toJsonTree(player.getRelations().getIgnoreList().toArray()));

        object.add("loyalty-titles", builder.toJsonTree(player.getUnlockedLoyaltyTitles()));

        object.add("kills", builder.toJsonTree(player.getKillsTracker().toArray()));
        object.add("drops", builder.toJsonTree(player.getDropLog().toArray()));
        object.add("achievements-completion",
                builder.toJsonTree(player.getAchievementAttributes().getCompletion()));
        object.add("achievements-progress", builder.toJsonTree(player.getAchievementAttributes().getProgress()));

        object.addProperty("reffered", player.gotReffered());
        object.addProperty("toggledglobalmessages", player.toggledGlobalMessages());

        object.addProperty("flying", player.isFlying());
        object.addProperty("canfly", player.canFly());
        object.addProperty("ghostwalking", player.isGhostWalking());
        object.addProperty("canghostwalk", player.canGhostWalk());

        object.add("secondary-equipment-slots-unlocked", builder.toJsonTree(player.getSecondaryEquipmentUnlocks()));

        object.addProperty("yell-tit", player.getYellTitle() == null ? "null" : player.getYellTitle());

        object.add("achievements-points", builder.toJsonTree(player.getAchievements().getPoints()));
        object.add("achievements-amount", builder.toJsonTree(player.getAchievements().getAmountRemaining()));
        object.add("achievements-completed", builder.toJsonTree(player.getAchievements().getCompleted()));
        object.addProperty("achievements-daily", player.getAchievements().getDailyAchievementsDate());

        if (player.getIronmanGroup() != null)
            object.addProperty("group-ironman-id", player.getIronmanGroup().getUniqueId());
        object.addProperty("group-ironman-locked", player.isGroupIronmanLocked());

        object.add("daily-task", builder.toJsonTree(player.dailies));
        object.add("collectionlog-rewards", builder.toJsonTree(player.getCollectionLog().rewardsClaims));
        object.addProperty("daily-skips", player.dailySkips);
        object.addProperty("dailies-done", player.dailiesCompleted);
        object.addProperty("daily-task-info", player.taskInfo == null ? "" : player.taskInfo);
        object.addProperty("level-notifications", player.levelNotifications);
        object.add("dailies-received-times", builder.toJsonTree(player.getTaskReceivedTimes()));

        object.add("obtained-pets", builder.toJsonTree(player.getObtainedPets()));

        object.addProperty("battlepass-xp", player.getBattlePass().getXp());
        object.addProperty("battlepass-premium", player.getBattlePass().isPremium());
        object.add("mini-me-equipment", builder.toJsonTree(player.getMinimeEquipment()));

        object.add("drystreak", builder.toJsonTree(player.getDryStreak().getDryStreakMap()));
        object.addProperty("boost", player.getEquipmentEnhancement().getBoost());
        object.addProperty("slot-level", player.getEquipmentEnhancement().getSlotLevel());

        object.addProperty("sp-season", player.getSeasonPass().getSeason());
        object.addProperty("sp-premium", player.getSeasonPass().isPremium());
        object.addProperty("sp-level", player.getSeasonPass().getLevel());
        object.addProperty("sp-exp", player.getSeasonPass().getExp());
        object.add("sp-rewards", builder.toJsonTree(player.getSeasonPass().getRewardsClaimed()));

        object.add("p-settings", builder.toJsonTree(player.getPSettings().getSettings()));

        object.addProperty("forge-tier", player.getForge().getTier());
        object.addProperty("forge-progress", player.getForge().getProgress());

        object.addProperty("lastloggedinday", player.getAttendenceManager().getLastLoggedInDate().toString());
        object.add("attendanceprogress", builder.toJsonTree(player.getAttendenceManager().getPlayerAttendanceProgress()));

        object.addProperty("vipEXP", player.getPlayerVIP().getExp());
        object.addProperty("cTicket", player.getPlayerVIP().getClaimedTicket());
        object.addProperty("vipRPack", player.getPlayerVIP().getClaimedPack());
        object.addProperty("vipTotal", player.getPlayerVIP().getTotal());
        object.addProperty("packXP", player.getPlayerVIP().getPackXp());
        object.add("vipDonate", builder.toJsonTree(player.getPlayerVIP().getDonations()));

        object.addProperty("starterLevel", player.getStarter().getPosition());
        object.addProperty("starterXp", player.getStarter().getXp());
        object.add("starterTasks", builder.toJsonTree(player.getStarter().getTasks()));
        object.add("starterRewards", builder.toJsonTree(player.getStarter().getRewards().getRewards()));
        object.add("starter-shop", builder.toJsonTree(player.getStarterShop().getItems()));

        object.add("equip-slot", builder.toJsonTree(player.getEquipment().getSlotBonuses()));
        object.add("second-slot", builder.toJsonTree(player.getSecondaryEquipment().getSlotBonuses()));

        object.add("charges", builder.toJsonTree(player.getItems().getCharges()));

        object.add("points", builder.toJsonTree(player.getPoints().getPoints()));

        object.addProperty("towerTier", player.getTower().getTier());
        object.addProperty("towerLevel", player.getTower().getLevel());
        object.add("towerRewards", builder.toJsonTree(player.getTower().getRewards().getItems()));
        object.add("achievements-map", builder.toJsonTree(player.getAchievementsMap()));

        object.add("timers", builder.toJsonTree(player.getTimers().save()));

        return this;
    }

    @Override
    public void save() {
        ThreadProgressor.submit(true, () -> {
            try (FileWriter file = new FileWriter(SecurityUtils.PLAYER_FILE+player.getUsername()+".json")) {
                file.write(builder.toJson(object));
                file.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public String key() {
        return null;
    }
}
