package com.ruse.security.save.impl.player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruse.engine.task.impl.FamiliarSpawnTask;
import com.ruse.model.*;
import com.ruse.model.container.impl.Bank;
import com.ruse.model.container.impl.UimBank;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.content.CurrencyPouch;
import com.ruse.world.content.DropLog;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.LoyaltyProgramme;
import com.ruse.world.packages.attendance.AttendanceProgress;
import com.ruse.world.packages.attendance.AttendanceTab;
import com.ruse.world.content.collectionlog.CollectionEntry;
import com.ruse.world.content.collectionlog.CollectionLogInterface;
import com.ruse.world.content.combat.magic.CombatSpells;
import com.ruse.world.content.combat.weapon.FightType;
import com.ruse.world.content.dailytasks_new.DailyTask;
import com.ruse.world.content.dailytasks_new.TaskChallenge;
import com.ruse.world.content.grandexchange.GrandExchangeSlot;
import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.content.groupironman.IronmanGroup;
import com.ruse.world.content.skill.SkillManager;
import com.ruse.world.content.skill.impl.slayer.SlayerMaster;
import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.donation.DonationManager;
import com.ruse.world.packages.mode.GameModeConstants;
import com.ruse.world.packages.mode.impl.*;
import com.ruse.world.packages.ranks.DonatorRank;
import com.ruse.world.packages.ranks.StaffRank;
import com.ruse.world.packages.ranks.VIPRank;
import com.ruse.world.packages.slot.SlotBonus;
import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.impl.starter.StarterTasks;
import com.ruse.world.packages.vip.Donation;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class PlayerSecureLoad extends SecureLoad {

    private final Player player;
    public PlayerSecureLoad(Player player){
        this.player = player;
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public PlayerSecureLoad run() {

        if (object.has("total-play-time")) {
            player.setTotalPlayTime(object.get("total-play-time").getAsLong());
        }

        if (object.has("username")) {
            player.setUsername(object.get("username").getAsString());
        }

        if (object.has("uim-bank")) {
            Map<Integer, Integer> items = builder.fromJson(object.get("uim-bank"),
                    new TypeToken<Map<Integer, Integer>>() {
                    }.getType());
            player.setUimBankItems(items);
        }

        if(object.has("dg-floor")) {
            player.setDungeoneeringFloor(object.get("dg-floor").getAsInt());
        }

        if(object.has("dg-prestige")) {
            player.setDungeoneeringPrestige(object.get("dg-prestige").getAsInt());
        }
        if (object.has("has-pin2")) {
            player.setHasPin(object.get("has-pin2").getAsBoolean());
            if(player.getHasPin()){
                if (object.has("saved-pin2")) {
                    player.setSavedPin(object.get("saved-pin2").getAsString());
                }
            }
        }

        if (object.has("saved-ip")) {
            player.setSavedIp(object.get("saved-ip").getAsString());
        }

        if (object.has("amount-donated-today")) {
            player.setAmountDonatedToday(object.get("amount-donated-today").getAsInt());
        }

        if (object.has("global-rate")) {
            player.getPointsHandler().setGlobalRate(object.get("global-rate").getAsInt());
        }
        if (object.has("peng-rate")) {
            player.getPointsHandler().setPengRate(object.get("peng-rate").getAsInt());
        }

        if (object.has("godmodetime")) {
            player.setGodModeTimer(object.get("godmodetime").getAsInt());
        }

        if (object.has("slayer-rate")) {
            player.getPointsHandler().setSlayerRate(object.get("slayer-rate").getAsInt());
        }
        if (object.has("claimed-first")) {
            player.claimedFirst = object.get("claimed-first").getAsBoolean();
        }

        if (object.has("claimed-second")) {
            player.claimedSecond = object.get("claimed-second").getAsBoolean();
        }

        if (object.has("claimed-third")) {
            player.claimedThird = object.get("claimed-third").getAsBoolean();
        }

        if (object.has("last-donation")) {
            player.lastDonation = object.get("last-donation").getAsLong();
        }

        if (object.has("last-time-reset")) {
            player.lastDonation = object.get("last-time-reset").getAsLong();
        }

        if (object.has("email")) {
            player.setEmailAddress(object.get("email").getAsString());
        }
        if (object.has("lastTGloveIndex")) {
            player.lastTGloveIndex = object.get("lastTGloveIndex").getAsInt();
        }
        if (object.has("tasks-progress")) {
            int[] loadedProgress = builder.fromJson(object.get("tasks-progress").getAsJsonArray(), int[].class);
            int defaultLength = player.getStarterTaskAttributes().getProgress().length;
            if (loadedProgress.length < defaultLength) {

                for (int index = 0; index < defaultLength - 1; index++) {
                    if (index < loadedProgress.length) {
                        player.getStarterTaskAttributes().setProgress(index, loadedProgress[index]);
                    } else {
                        player.getStarterTaskAttributes().setProgress(index, 0);
                    }
                }
            } else {
                player.getStarterTaskAttributes().setProgress(loadedProgress);
            }
        }

        if (object.has("tasks-completion")) {
            boolean[] loadedCompletion = builder.fromJson(object.get("tasks-completion").getAsJsonArray(),
                    boolean[].class);
            int defaultLength = player.getStarterTaskAttributes().getCompletion().length;
            if (loadedCompletion.length < defaultLength) {
                // System.out.println("load length: " + loadedCompletion + " default: " + defaultLength);
                for (int index = 0; index < defaultLength - 1; index++) {
                    if (index < loadedCompletion.length) {
                        // System.out.println("what is this: " + index);
                        player.getStarterTaskAttributes().setCompletion(index, loadedCompletion[index]);
                    } else {
                        player.getStarterTaskAttributes().setCompletion(index, false);
                    }
                }
            } else {
                player.getStarterTaskAttributes().setCompletion(loadedCompletion);
            }
        }

        if (object.has("staff-rights")) {
            String rights = object.get("staff-rights").getAsString();
            setNewRank(player, rights);
        }

        if(object.has("staff")){
            player.setRank(StaffRank.valueOf(object.get("staff").getAsString()));
        }

        if(object.has("donator")){
            player.setDonator(DonatorRank.valueOf(object.get("donator").getAsString()));
        }

        if(object.has("vip")){
            player.setVip(VIPRank.valueOf(object.get("vip").getAsString()));
        }

        if (object.has("game-mode")) {
            GameModeConstants.setMode(player, object.get("game-mode").getAsString());
        }

        if (object.has("collection-data")) {
            Type collectionLogType = new TypeToken<List<CollectionEntry>>() {
            }.getType();
            player.setCollectionLogData(new Gson().fromJson(object.get("collection-data"), collectionLogType));
        }

        if (object.has("holy-prayers-unlocked")) {
            player.setUnlockedHolyPrayers(
                    builder.fromJson(object.get("holy-prayers-unlocked").getAsJsonArray(), boolean[].class));
        }

        if (object.has("current-boss-task")) {
            player.setCurrentBossTask(object.get("current-boss-task").getAsInt());
        }

        if (object.has("current-boss-amount")) {
            player.setCurrentBossTaskAmount(object.get("current-boss-amount").getAsInt());
        }

        if (object.has("has-completed-boss-task")) {
            player.setHasPlayerCompletedBossTask(object.get("has-completed-boss-task").getAsBoolean());
        }

        if (object.has("current-daily-task-id")) {
            player.setCurrentDailyTask(object.get("current-daily-task-id").getAsString().equals("") ? null
                    : object.get("current-daily-task-id").getAsString());
        }

        if (object.has("current-daily-task-amount")) {
            player.setCurrentDailyTaskAmount(object.get("current-daily-task-amount").getAsInt());
        }

        if (object.has("current-daily-task-x-pos")) {
            player.setxPosDailyTask(object.get("current-daily-task-x-pos").getAsInt());
        }

        if (object.has("current-daily-task-y-pos")) {
            player.setyPostDailyTask(object.get("current-daily-task-y-pos").getAsInt());
        }

        if (object.has("current-daily-task-z-pos")) {
            player.setzPosDailyTask(object.get("current-daily-task-z-pos").getAsInt());
        }
        if (object.has("current-daily-task-reward")) {
            player.setRewardDailyTask(object.get("current-daily-task-reward").getAsInt());
        }

        if (object.has("daysVoted")) {
            player.setDaysVoted(object.get("daysVoted").getAsInt());
        }

        if (object.has("totalTimesClaimed")) {
            player.setTotalTimesClaimed(object.get("totalTimesClaimed").getAsInt());
        }

        if (object.has("longestDaysVoted")) {
            player.setLongestDaysVoted(object.get("longestDaysVoted").getAsInt());
        }

        if (object.has("timeUntilNextReward")) {
            player.setTimeUntilNextReward(object.get("timeUntilNextReward").getAsInt());
        }

        if (object.has("lastVoted")) {
            player.setLastVoted(object.get("lastVoted").getAsString().equals("") ? null : object.get("lastVoted").getAsString());
        }

        if (object.has("current-voting-streak")) {
            player.setCurrentVotingStreak(object.get("current-voting-streak").getAsInt());
        }

        if (object.has("entriesCurrency")) {
            player.setEntriesCurrency(object.get("entriesCurrency").getAsDouble());
        }

        if (object.has("loyalty-title")) {
            player.setLoyaltyTitle(LoyaltyProgramme.LoyaltyTitles.valueOf(object.get("loyalty-title").getAsString()));
        }

        if (object.has("position")) {
            player.getPosition().setAs(builder.fromJson(object.get("position"), Position.class));
        }

        if (object.has("online-status")) {
            player.getRelations().setStatus(PlayerRelations.PrivateChatStatus.valueOf(object.get("online-status").getAsString()),
                    false);
        }

        if (object.has("given-starter")) {
            player.setReceivedStarter(object.get("given-starter").getAsBoolean());
        }

        if (object.has("donated")) {
            player.incrementAmountDonated(object.get("donated").getAsInt());
        }

        if (object.has("minutes-bonus-exp")) {
            player.setMinutesBonusExp(object.get("minutes-bonus-exp").getAsInt(), false);
        }
        if (object.has("minutes-voting-dr")) {
            player.setMinutesVotingDR(object.get("minutes-voting-dr").getAsInt(), false);
        }
        if (object.has("minutes-voting-dmg")) {
            player.setMinutesVotingDMG(object.get("minutes-voting-dmg").getAsInt(), false);
        }

        if (object.has("total-gained-exp")) {
            player.getSkillManager().setTotalGainedExp(object.get("total-gained-exp").getAsInt());
        }

        if (object.has("dung-tokens")) {
            player.getPointsHandler().setDungeoneeringTokens(object.get("dung-tokens").getAsInt(), false);
        }

        if (object.has("barrows-points")) {
            player.getPointsHandler().setBarrowsPoints(object.get("barrows-points").getAsInt(), false);
        }

        if (object.has("donator-points")) {
            player.getPointsHandler().setDonatorPoints(object.get("donator-points").getAsInt(), false);
        }

        if (object.has("prestige-points")) {
            player.getPointsHandler().setPrestigePoints(object.get("prestige-points").getAsInt(), false);
        }
        if (object.has("Skilling-points")) {
            player.getPointsHandler().setSkillingPoints(object.get("Skilling-points").getAsInt(), false);
        }

        if (object.has("achievement-points")) {
            player.getPointsHandler().setAchievementPoints(object.get("achievement-points").getAsInt(), false);
        }

        if (object.has("mini-me-equipment")) {
            player.setMinimeEquipment(builder.fromJson(object.get("mini-me-equipment"), Item[].class));
        }

        if (object.has("commendations")) {
            player.getPointsHandler().setCommendations(object.get("commendations").getAsInt(), false);
        }

        if (object.has("loyalty-points")) {
            player.getPointsHandler().setLoyaltyPoints(object.get("loyalty-points").getAsInt(), false);
        }

        if (object.has("total-loyalty-points")) {
            player.getAchievementAttributes()
                    .incrementTotalLoyaltyPointsEarned(object.get("total-loyalty-points").getAsDouble());
        }

        if (object.has("voting-points")) {
            player.getPointsHandler().setVotingPoints(object.get("voting-points").getAsInt(), false);
        }
        if (object.has("spawn-killcount")) {
            player.getPointsHandler().setSPAWNKILLCount(object.get("spawn-killcount").getAsInt(), false);
        }
        if (object.has("lord-killcount")) {
            player.getPointsHandler().setLORDKILLCount(object.get("lord-killcount").getAsInt(), false);
        }
        if (object.has("demon-killcount")) {
            player.getPointsHandler().setDEMONKILLCount(object.get("demon-killcount").getAsInt(), false);
        }
        if (object.has("dragon-killcount")) {
            player.getPointsHandler().setDRAGONKILLCount(object.get("dragon-killcount").getAsInt(), false);
        }
        if (object.has("beast-killcount")) {
            player.getPointsHandler().setBEASTKILLCount(object.get("beast-killcount").getAsInt(), false);
        }
        if (object.has("king-killcount")) {
            player.getPointsHandler().setKINGKILLCount(object.get("king-killcount").getAsInt(), false);
        }
        if (object.has("avatar-killcount")) {
            player.getPointsHandler().setAVATARKILLCount(object.get("avatar-killcount").getAsInt(), false);
        }
        if (object.has("angel-killcount")) {
            player.getPointsHandler().setANGELKILLCount(object.get("angel-killcount").getAsInt(), false);
        }
        if (object.has("lucien-killcount")) {
            player.getPointsHandler().setLUCIENKILLCount(object.get("lucien-killcount").getAsInt(), false);
        }
        if (object.has("hercules-killcount")) {
            player.getPointsHandler().setHERCULESKILLCount(object.get("hercules-killcount").getAsInt(), false);
        }
        if (object.has("satan-killcount")) {
            player.getPointsHandler().setSATANKILLCount(object.get("satan-killcount").getAsInt(), false);
        }
        if (object.has("zeus-killcount")) {
            player.getPointsHandler().setZEUSKILLCount(object.get("zeus-killcount").getAsInt(), false);
        }
        if (object.has("mini-lucifer-killcount")) {
            player.getPointsHandler().setMiniLuciferkillcount(object.get("mini-lucifer-killcount").getAsInt());
        }
        if (object.has("lucifer-killcount")) {
            player.getPointsHandler().setLuciferkillcount(object.get("lucifer-killcount").getAsInt());
        }
        if (object.has("npc-killcount")) {
            player.getPointsHandler().setNPCKILLCount(object.get("npc-killcount").getAsInt(), false);
        }
        if (object.has("total-prestiges")) {
            player.getPointsHandler().setTotalPrestiges(object.get("total-prestiges").getAsInt(), false);
        }
        if (object.has("slayer-spree")) {
            player.getPointsHandler().setSlayerSpree(object.get("slayer-spree").getAsInt(), false);
        }
        if (object.has("event-points")) {
            player.getPointsHandler().setEventPoints(object.get("event-points").getAsInt(), false);
        }
        if (object.has("boss-points")) {
            player.getPointsHandler().setBossPoints(object.get("boss-points").getAsInt(), false);
        }
        if (object.has("shilling-rate")) {
            player.getPointsHandler().setSHILLINGRate(object.get("shilling-rate").getAsInt(), false);
        }

        if (object.has("slayer-points")) {
            player.getPointsHandler().setSlayerPoints(object.get("slayer-points").getAsInt(), false);
        }

        if (object.has("pk-points")) {
            player.getPointsHandler().setPkPoints(object.get("pk-points").getAsInt(), false);
        }

        if (object.has("player-kills")) {
            player.getPlayerKillingAttributes().setPlayerKills(object.get("player-kills").getAsInt());
        }

        if (object.has("player-killstreak")) {
            player.getPlayerKillingAttributes().setPlayerKillStreak(object.get("player-killstreak").getAsInt());
        }

        if (object.has("player-deaths")) {
            player.getPlayerKillingAttributes().setPlayerDeaths(object.get("player-deaths").getAsInt());
        }

        if (object.has("target-percentage")) {
            player.getPlayerKillingAttributes().setTargetPercentage(object.get("target-percentage").getAsInt());
        }

        if (object.has("bh-rank")) {
            player.getAppearance().setBountyHunterSkull(object.get("bh-rank").getAsInt());
        }

        if (object.has("gender")) {
            player.getAppearance().setGender(Gender.valueOf(object.get("gender").getAsString()));
        }

        if (object.has("spell-book")) {
            player.setSpellbook(MagicSpellbook.valueOf(object.get("spell-book").getAsString()));
        }

        if (object.has("prayer-book")) {
            player.setPrayerbook(Prayerbook.valueOf(object.get("prayer-book").getAsString()));
        }
        if (object.has("running")) {
            player.setRunning(object.get("running").getAsBoolean());
        }
        if (object.has("run-energy")) {
            player.setRunEnergy(object.get("run-energy").getAsInt());
        }
        if (object.has("music")) {
            player.setMusicActive(object.get("music").getAsBoolean());
        }
        if (object.has("sounds")) {
            player.setSoundsActive(object.get("sounds").getAsBoolean());
        }
        if (object.has("auto-retaliate")) {
            player.setAutoRetaliate(object.get("auto-retaliate").getAsBoolean());
        }
        if (object.has("xp-locked")) {
            player.setExperienceLocked(object.get("xp-locked").getAsBoolean());
        }
        if (object.has("veng-cast")) {
            player.setHasVengeance(object.get("veng-cast").getAsBoolean());
        }
        if (object.has("last-veng")) {
            player.getLastVengeance().reset(object.get("last-veng").getAsLong());
        }
        if (object.has("fight-type")) {
            player.setFightType(FightType.valueOf(object.get("fight-type").getAsString()));
        }
        if (object.has("sol-effect")) {
            player.setStaffOfLightEffect(object.get("sol-effect").getAsInt());
        }
        if (object.has("skull-timer")) {
            player.setSkullTimer(object.get("skull-timer").getAsInt());
        }
        if (object.has("fuse-combination-timer")) {
            player.setFuseCombinationTimer(object.get("fuse-combination-timer").getAsLong());
        }
        if (object.has("fuse-item-selected")) {
            player.setFuseItemSelected(object.get("fuse-item-selected").getAsInt());
        }
        if (object.has("claimed-fuse-item")) {
            player.setClaimedFuseItem(object.get("claimed-fuse-item").getAsBoolean());
        }
        if (object.has("fuse-in-progress")) {
            player.setFuseInProgress(object.get("fuse-in-progress").getAsBoolean());
        }
        if (object.has("accept-aid")) {
            player.setAcceptAid(object.get("accept-aid").getAsBoolean());
        }
        if (object.has("poison-damage")) {
            player.setPoisonDamage(object.get("poison-damage").getAsInt());
        }
        if (object.has("poison-immunity")) {
            player.setPoisonImmunity(object.get("poison-immunity").getAsInt());
        }
        if (object.has("double-dr-timer")) {
            player.setDoubleDRTimer(object.get("double-dr-timer").getAsInt());
        }
        if (object.has("double-ddr-timer")) {
            player.setDoubleDDRTimer(object.get("double-ddr-timer").getAsInt());
        }
        if (object.has("double-dmg-timer")) {
            player.setDoubleDMGTimer(object.get("double-dmg-timer").getAsInt());
        }
        if (object.has("fire-immunity")) {
            player.setFireImmunity(object.get("fire-immunity").getAsInt());
        }
        if (object.has("fire-damage-mod")) {
            player.setFireDamageModifier(object.get("fire-damage-mod").getAsInt());
        }
        if (object.has("prayer-renewal-timer")) {
            player.setPrayerRenewalPotionTimer(object.get("prayer-renewal-timer").getAsInt());
        }
        if (object.has("teleblock-timer")) {
            player.setTeleblockTimer(object.get("teleblock-timer").getAsInt());
        }
        if (object.has("special-amount")) {
            player.setSpecialPercentage(object.get("special-amount").getAsInt());
        }

        if (object.has("entered-gwd-room")) {
            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                    .setHasEnteredRoom(object.get("entered-gwd-room").getAsBoolean());
        }

        if (object.has("gwd-altar-delay")) {
            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                    .setAltarDelay(object.get("gwd-altar-delay").getAsLong());
        }

        if (object.has("gwd-killcount")) {
            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                    .setKillcount(builder.fromJson(object.get("gwd-killcount"), int[].class));
        }

        if (object.has("effigy")) {
            player.setEffigy(object.get("effigy").getAsInt());
        }

        if (object.has("summon-npc")) {
            int npc = object.get("summon-npc").getAsInt();
            if (npc > 0)
                player.getSummoning().setFamiliarSpawnTask(new FamiliarSpawnTask(player)).setFamiliarId(npc);
        }
        if (object.has("summon-death")) {
            int death = object.get("summon-death").getAsInt();
            if (death > 0 && player.getSummoning().getSpawnTask() != null)
                player.getSummoning().getSpawnTask().setDeathTimer(death);
        }
        if (object.has("process-farming")) {
            player.setProcessFarming(object.get("process-farming").getAsBoolean());
        }

        if (object.has("clanchat")) {
            String clan = object.get("clanchat").getAsString();
            if (!clan.equals("null"))
                player.setClanChatName(clan);
        }
        if (object.has("autocast")) {
            player.setAutocast(object.get("autocast").getAsBoolean());
        }
        if (object.has("autocast-spell")) {
            int spell = object.get("autocast-spell").getAsInt();
            if (spell != -1)
                player.setAutocastSpell(CombatSpells.getSpell(spell));
        }

        if (object.has("kills")) {
            KillsTracker.submit(player, builder.fromJson(object.get("kills").getAsJsonArray(), KillsTracker.KillsEntry[].class));
        }

        if (object.has("drops")) {
            DropLog.submit(player, builder.fromJson(object.get("drops").getAsJsonArray(), DropLog.DropLogEntry[].class));
        }

        if (object.has("coins-gambled")) {
            player.getAchievementAttributes().setCoinsGambled(object.get("coins-gambled").getAsInt());
        }

        if (object.has("slayer-master")) {
            player.getSlayer().setSlayerMaster(SlayerMaster.valueOf(object.get("slayer-master").getAsString()));
        }

        if (object.has("slayer-task")) {
            player.getSlayer().setSlayerTask(SlayerTasks.valueOf(object.get("slayer-task").getAsString()));
        }

        if (object.has("lastlogin"))
            player.lastLogin = (object.get("lastlogin").getAsLong());
        if (object.has("lastdailyclaim"))
            player.lastDailyClaim = (object.get("lastdailyclaim").getAsLong());

        if (object.has("day1claimed"))
            player.day1Claimed = (object.get("day1claimed").getAsBoolean());

        if (object.has("day2claimed"))
            player.day2Claimed = (object.get("day2claimed").getAsBoolean());

        if (object.has("day3claimed"))
            player.day3Claimed = (object.get("day3claimed").getAsBoolean());

        if (object.has("day4claimed"))
            player.day4Claimed = (object.get("day4claimed").getAsBoolean());

        if (object.has("day5claimed"))
            player.day5Claimed = (object.get("day5claimed").getAsBoolean());

        if (object.has("day6claimed"))
            player.day6Claimed = (object.get("day6claimed").getAsBoolean());

        if (object.has("day7claimed"))
            player.day7Claimed = (object.get("day7claimed").getAsBoolean());

        if (object.has("lastvotetime"))
            player.lastVoteTime = (object.get("lastvotetime").getAsLong());

        if (object.has("hasvotedtoday"))
            player.hasVotedToday = (object.get("hasvotedtoday").getAsBoolean());

        if (object.has("prev-slayer-task")) {
            player.getSlayer().setLastTask(SlayerTasks.valueOf(object.get("prev-slayer-task").getAsString()));
        }

        if (object.has("task-amount")) {
            player.getSlayer().setAmountToSlay(object.get("task-amount").getAsInt());
        }

        if (object.has("task-streak")) {
            player.getSlayer().setTaskStreak(object.get("task-streak").getAsInt());
        }

        if (object.has("duo-partner")) {
            String partner = object.get("duo-partner").getAsString();
            player.getSlayer().setDuoPartner(partner.equals("null") ? null : partner);
        }

        if (object.has("double-slay-xp")) {
            player.getSlayer().doubleSlayerXP = object.get("double-slay-xp").getAsBoolean();
        }

        if (object.has("killed-players")) {
            List<String> list = new ArrayList<>();
            String[] killed_players = builder.fromJson(object.get("killed-players").getAsJsonArray(),
                    String[].class);
            Collections.addAll(list, killed_players);
            player.getPlayerKillingAttributes().setKilledPlayers(list);
        }


        if (object.has("random-coffin")) {
            player.getMinigameAttributes().getBarrowsMinigameAttributes()
                    .setRandomCoffin((object.get("random-coffin").getAsInt()));
        }

        if (object.has("barrows-killcount")) {
            player.getMinigameAttributes().getBarrowsMinigameAttributes()
                    .setKillcount((object.get("barrows-killcount").getAsInt()));
        }

        if (object.has("nomad")) {
            player.getMinigameAttributes().getNomadAttributes()
                    .setQuestParts(builder.fromJson(object.get("nomad").getAsJsonArray(), boolean[].class));
        }

        if (object.has("recipe-for-disaster")) {
            player.getMinigameAttributes().getRecipeForDisasterAttributes().setQuestParts(
                    builder.fromJson(object.get("recipe-for-disaster").getAsJsonArray(), boolean[].class));
        }

        if (object.has("recipe-for-disaster-wave")) {
            player.getMinigameAttributes().getRecipeForDisasterAttributes()
                    .setWavesCompleted((object.get("recipe-for-disaster-wave").getAsInt()));
        }

        if (object.has("clue-progress")) {
            player.setClueProgress((object.get("clue-progress").getAsInt()));
        }

        if (object.has("dung-items-bound")) {
            player.getMinigameAttributes().getDungeoneeringAttributes()
                    .setBoundItems(builder.fromJson(object.get("dung-items-bound").getAsJsonArray(), int[].class));
        }

        if (object.has("rune-ess")) {
            player.setStoredRuneEssence((object.get("rune-ess").getAsInt()));
        }

        if (object.has("pure-ess")) {
            player.setStoredPureEssence((object.get("pure-ess").getAsInt()));
        }

        if (object.has("bank-pin")) {
            player.getBankPinAttributes()
                    .setBankPin(builder.fromJson(object.get("bank-pin").getAsJsonArray(), int[].class));
        }

        if (object.has("has-bank-pin")) {
            player.getBankPinAttributes().setHasBankPin(object.get("has-bank-pin").getAsBoolean());
        }
        if (object.has("last-pin-attempt")) {
            player.getBankPinAttributes().setLastAttempt(object.get("last-pin-attempt").getAsLong());
        }
        if (object.has("invalid-pin-attempts")) {
            player.getBankPinAttributes().setInvalidAttempts(object.get("invalid-pin-attempts").getAsInt());
        }

        if (object.has("bank-pin")) {
            player.getBankPinAttributes()
                    .setBankPin(builder.fromJson(object.get("bank-pin").getAsJsonArray(), int[].class));
        }

        if (object.has("appearance")) {
            player.getAppearance().set(builder.fromJson(object.get("appearance").getAsJsonArray(), int[].class));
        }

        if (object.has("agility-obj")) {
            player.setCrossedObstacles(
                    builder.fromJson(object.get("agility-obj").getAsJsonArray(), boolean[].class));
        }


        if (object.has("skills")) {
            player.getSkillManager().setSkills(builder.fromJson(object.get("skills"), SkillManager.Skills.class));
        }
        if (object.has("inventory")) {
            player.getInventory()
                    .setItems(builder.fromJson(object.get("inventory").getAsJsonArray(), Item[].class));
        }
        if (object.has("equipment")) {
            player.getEquipment()
                    .setItems(builder.fromJson(object.get("equipment").getAsJsonArray(), Item[].class));
        }
        if (object.has("secondary-equipment")) {
            player.getSecondaryEquipment()
                    .setItems(builder.fromJson(object.get("secondary-equipment").getAsJsonArray(), Item[].class));
        }
        if (object.has("preset-equipment")) {
            player.getPreSetEquipment()
                    .setItems(builder.fromJson(object.get("preset-equipment").getAsJsonArray(), Item[].class));
        }

        if (object.has("offences")) {
            ArrayList<String> list = new ArrayList<String>();
            String[] killed_players = builder.fromJson(object.get("offences").getAsJsonArray(), String[].class);
            for (String s : killed_players)
                list.add(s);
            player.setOffences(list);
        }

        /** BANK **/
        for (int i = 0; i < 9; i++) {
            if (object.has("bank-" + i))
                player.setBank(i, new Bank(player)).getBank(i).addItems(
                        builder.fromJson(object.get("bank-" + i).getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-0")) {
            player.setBank(0, new Bank(player)).getBank(0)
                    .addItems(builder.fromJson(object.get("bank-0").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-1")) {
            player.setBank(1, new Bank(player)).getBank(1)
                    .addItems(builder.fromJson(object.get("bank-1").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-2")) {
            player.setBank(2, new Bank(player)).getBank(2)
                    .addItems(builder.fromJson(object.get("bank-2").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-3")) {
            player.setBank(3, new Bank(player)).getBank(3)
                    .addItems(builder.fromJson(object.get("bank-3").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-4")) {
            player.setBank(4, new Bank(player)).getBank(4)
                    .addItems(builder.fromJson(object.get("bank-4").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-5")) {
            player.setBank(5, new Bank(player)).getBank(5)
                    .addItems(builder.fromJson(object.get("bank-5").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-6")) {
            player.setBank(6, new Bank(player)).getBank(6)
                    .addItems(builder.fromJson(object.get("bank-6").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-7")) {
            player.setBank(7, new Bank(player)).getBank(7)
                    .addItems(builder.fromJson(object.get("bank-7").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("bank-8")) {
            player.setBank(8, new Bank(player)).getBank(8)
                    .addItems(builder.fromJson(object.get("bank-8").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("new-uim-bank")) {
            player.setUIMBank(
                            new UimBank(player)).getUIMBank()
                    .addItems(builder.fromJson(object.get("new-uim-bank").getAsJsonArray(), Item[].class), false);
        }

        if (object.has("ge-slots")) {
            GrandExchangeSlot[] slots = builder.fromJson(object.get("ge-slots").getAsJsonArray(),
                    GrandExchangeSlot[].class);
            player.setGrandExchangeSlots(slots);
        }

        if (object.has("store")) {
            Item[] validStoredItems = builder.fromJson(object.get("store").getAsJsonArray(), Item[].class);
            if (player.getSummoning().getSpawnTask() != null) {
                player.getSummoning().getSpawnTask().setValidItems(validStoredItems);
            }
        }

        if (object.has("charm-imp")) {
            int[] charmImpConfig = builder.fromJson(object.get("charm-imp").getAsJsonArray(), int[].class);
            player.getSummoning().setCharmimpConfig(charmImpConfig);
        }

        if (object.has("friends")) {
            long[] friends = builder.fromJson(object.get("friends").getAsJsonArray(), long[].class);

            for (long l : friends) {
                player.getRelations().getFriendList().add(l);
            }
        }
        if (object.has("ignores")) {
            long[] ignores = builder.fromJson(object.get("ignores").getAsJsonArray(), long[].class);

            for (long l : ignores) {
                player.getRelations().getIgnoreList().add(l);
            }
        }

        if (object.has("loyalty-titles")) {
            player.setUnlockedLoyaltyTitles(
                    builder.fromJson(object.get("loyalty-titles").getAsJsonArray(), boolean[].class));
        }

        if (object.has("achievements-completion")) {
            player.getAchievementAttributes().setCompletion(
                    builder.fromJson(object.get("achievements-completion").getAsJsonArray(), boolean[].class));
        }

        if (object.has("achievements-progress")) {
            player.getAchievementAttributes().setProgress(
                    builder.fromJson(object.get("achievements-progress").getAsJsonArray(), int[].class));
        }

        if (object.has("yellhexcolor")) {
            player.setYellHex(object.get("yellhexcolor").getAsString());
        }

        if (object.has("yell-tit")) {
            player.setYellTitle(object.get("yell-tit").getAsString());
        }

        if (object.has("fri13may16")) {
            player.setFriday13May2016(object.get("fri13may16").getAsBoolean());
        }

        if (object.has("spiritdebug")) {
            player.setSpiritDebug(object.get("spiritdebug").getAsBoolean());
        }

        if (object.has("reffered")) {
            player.setReffered(object.get("reffered").getAsBoolean());
        }

        if (object.has("is-secondary-equipment")) {
            player.setIsSecondaryEquipment(object.get("is-secondary-equipment").getAsBoolean());
        }

        if (object.has("indung")) {
            player.setInDung(object.get("indung").getAsBoolean());
        }

        if (object.has("toggledglobalmessages")) {
            player.setToggledGlobalMessages(object.get("toggledglobalmessages").getAsBoolean());
        }

        if (object.has("flying")) {
            player.setFlying(object.get("flying").getAsBoolean());
        }

        if (object.has("canfly")) {
            player.setCanFly(object.get("canfly").getAsBoolean());
        }

        if (object.has("canghostwalk")) {
            player.setCanGhostWalk(object.get("canghostwalk").getAsBoolean());
        }

        if (object.has("ghostwalking")) {
            player.setGhostWalking(object.get("ghostwalking").getAsBoolean());
        }

        if (object.has("barrowschests")) {
            player.getPointsHandler().setBarrowsChests(object.get("barrowschests").getAsInt(), false);
        }

        if (object.has("cluesteps")) {
            player.getPointsHandler().setClueSteps(object.get("cluesteps").getAsInt(), false);
        }

        if (object.has("currency-pouch")) {
            player.setCurrencyPouch(builder.fromJson(object.get("currency-pouch"), CurrencyPouch.class));
        }

        /*
         * RIP difficulty loading. We'll still keep original values though, because fuck
         * it. if (object.has("difficulty")) { Difficulty.set(player,
         * Difficulty.valueOf(object.get("difficulty").getAsString()), true);
         * //player.setGameMode(GameMode.valueOf(object.get("game-mode").getAsString()))
         * ; }
         */

        if (object.has("secondary-equipment-slots-unlocked")) {
            player.setSecondaryEquipmentUnlocks(builder.fromJson(object.get("secondary-equipment-slots-unlocked").getAsJsonArray(), boolean[].class));
        }

        if (object.has("hween2016")) {
            player.setHween2016All(builder.fromJson(object.get("hween2016").getAsJsonArray(), boolean[].class));
        }

        if (object.has("donehween2016")) {
            player.setDoneHween2016(object.get("donehween2016").getAsBoolean());
        }

        if (object.has("christmas2016")) {
            player.setchristmas2016(object.get("christmas2016").getAsInt());
        }

        if (object.has("newYear2017")) {
            player.setNewYear2017(object.get("newYear2017").getAsInt());
        }

        if (object.has("easter2017")) {
            player.setEaster2017(object.get("easter2017").getAsInt());
        }

        if (object.has("hcimdunginventory")) {
            player.getDungeoneeringIronInventory()
                    .setItems(builder.fromJson(object.get("hcimdunginventory").getAsJsonArray(), Item[].class));
        }

        if (object.has("hcimdungequipment")) {
            player.getDungeoneeringIronEquipment()
                    .setItems(builder.fromJson(object.get("hcimdungequipment").getAsJsonArray(), Item[].class));
        }

        if (object.has("bonecrusheffect")) {
            player.setBonecrushEffect(object.get("bonecrusheffect").getAsBoolean());
        }

        if (object.has("p-tps")) {
            player.setPreviousTeleports(builder.fromJson(object.get("p-tps").getAsJsonArray(), int[].class));
        }

        if (object.has("afkstall1"))
            player.setAfkStallCount1(object.get("afkstall1").getAsInt());
        if (object.has("afkstall2"))
            player.setAfkStallCount2(object.get("afkstall2").getAsInt());
        if (object.has("afkstall3"))
            player.setAfkStallCount3(object.get("afkstall3").getAsInt());


        if (object.has("achievements-points")) {
            int points = object.get("achievements-points").getAsInt();
            player.getAchievements().setPoints(points);
        }
        if (object.has("achievements-amount")) {
            int[][] amountRemaining = builder.fromJson(object.get("achievements-amount").getAsJsonArray(),
                    int[][].class);
            player.getAchievements().setAmountRemaining(amountRemaining);
        }
        if (object.has("achievements-completed")) {
            boolean[][] completed = builder.fromJson(object.get("achievements-completed").getAsJsonArray(),
                    boolean[][].class);
            player.getAchievements().setCompleted(completed);
        }
        if (object.has("achievements-daily")) {
            player.getAchievements().setDailyTaskDate(object.get("achievements-daily").getAsLong());
        }


        if (object.has("gwd-killcount")) {
            player.getMinigameAttributes().getGodwarsDungeonAttributes()
                    .setKillcount(builder.fromJson(object.get("gwd-killcount"), int[].class));
        }

        if (object.has("progression-zones")) {
            player.setProgressionZones(builder.fromJson(object.get("progression-zones"), int[].class));
        }
        if (object.has("zones-complete")) {
            player.setZonesComplete(builder.fromJson(object.get("zones-complete"), boolean[].class));
        }

        if (object.has("gamble-banned")) {
            player.setGambleBanned(object.get("gamble-banned").getAsBoolean());
        }

        if (object.has("lucifers-unlocked")) {
            player.setUnlockedLucifers(object.get("lucifers-unlocked").getAsBoolean());
        }

        if (object.has("dark-supremes-unlocked")) {
            player.setUnlockedDarkSupreme(object.get("dark-supremes-unlocked").getAsBoolean());
        }

        if (object.has("group-ironman-id")) {
            IronmanGroup group = GroupManager.getGroup((object.get("group-ironman-id").getAsInt()));
            //  System.out.println("ID : " + object.get("group-ironman-id").getAsInt());
            if (group != null) {
                //  System.out.println("ID1 : " + object.get("group-ironman-id").getAsInt());
                //  System.out.println("GROUP: " + group);
                //  System.out.println("GROUP name: " + group.getName());
                //  System.out.println("agroup.getMembers(): " +group.getMembers().size());
                //  System.out.println("player.getUsername(): " +player.getUsername());
                if (group.getMembers().contains(player.getUsername())) {
                    // System.out.println("add name: " + player.getUsername());
                    player.setIronmanGroup(group);
                }
            }
        }

        if (object.has("group-ironman-locked")) {
            player.setGroupIronmanLocked((object.get("group-ironman-locked").getAsBoolean()));
        }

        if (object.has("lastInstanceNpc")) {
            player.lastInstanceNpc = object.get("lastInstanceNpc").getAsInt();
        }

        if (object.has("daily-task")) {
            HashMap<DailyTask, TaskChallenge> dailyTasks = builder.fromJson(object.get("daily-task"),
                    new TypeToken<HashMap<DailyTask, TaskChallenge>>() {
                    }.getType());
            player.setDailies(dailyTasks);
        }

        if (object.has("collectionlog-rewards")) {
            HashMap<Integer, CollectionLogInterface.RewardClaim> rewardClaimHashMap = builder.fromJson(object.get("collectionlog-rewards"),
                    new TypeToken<HashMap<Integer, CollectionLogInterface.RewardClaim>>() {
                    }.getType());
            player.getCollectionLog().setRewardsClaims(rewardClaimHashMap);
        }

        if (object.has("daily-skips")) {
            player.dailySkips = object.get("daily-skips").getAsInt();
        }

        if (object.has("dailies-done")) {
            player.dailiesCompleted = object.get("dailies-done").getAsInt();
        }

        if (object.has("daily-task-info")) {
            player.taskInfo = object.get("daily-task-info").getAsString().equals("") ? null : object.get("daily-task-info").getAsString();
        }

        if (object.has("level-notifications")) {
            player.levelNotifications = object.get("level-notifications").getAsBoolean();
        }

        if (object.has("dailies-received-times")) {
            player.setTaskReceivedTimes(builder.fromJson(object.get("dailies-received-times").getAsJsonArray(), long[].class));
        }
            /*if (object.has("favorite-teleports")) {
                TeleportInterface.Teleport[] data = builder.fromJson(object.get("favorite-teleports").getAsJsonArray(), TeleportInterface.Teleport[].class);
                for (TeleportInterface.Teleport l : data) {
                    player.getFavoriteTeleports().add(l);
                }
            }*/

        if (object.has("obtained-pets")) {
            BossPets.BossPet[] data = builder.fromJson(object.get("obtained-pets").getAsJsonArray(), BossPets.BossPet[].class);
            for (BossPets.BossPet l : data) {
                player.getObtainedPets().add(l);
            }
        }
        if(object.has("battlepass-xp")) {
            player.getBattlePass().setXp(object.get("battlepass-xp").getAsInt());
        }
        if (object.has("battlepass-premium")) {
            player.getBattlePass().setPremium(object.get("battlepass-premium").getAsBoolean());
        }

        if (object.has("drystreak")) {
            HashMap<Integer, Integer> dryStreakMap = builder.fromJson(object.get("drystreak"),
                    new TypeToken<HashMap<Integer, Integer>>() {
                    }.getType());
            player.getDryStreak().setDryStreakMap(dryStreakMap);
        }

        if(object.has("boost")) {
            player.getEquipmentEnhancement().setBoost(object.get("boost").getAsLong());
        }
        if(object.has("slot-level")) {
            player.getEquipmentEnhancement().setSlotLevel(object.get("slot-level").getAsLong());
        }

        if(object.has("sp-season")) {
            player.getSeasonPass().setSeason(object.get("sp-season").getAsInt());
        }
        if(object.has("sp-premium")) {
            player.getSeasonPass().setPremium(object.get("sp-premium").getAsBoolean());
        }
        if(object.has("sp-level")) {
            player.getSeasonPass().setLevel(object.get("sp-level").getAsInt());
        }
        if(object.has("sp-exp")) {
            player.getSeasonPass().setExp(object.get("sp-exp").getAsInt());
        }
        if(object.has("sp-rewards")) {
            player.getSeasonPass().setRewardsClaimed(builder.fromJson(object.get("sp-rewards").getAsJsonArray(), boolean[].class));
        }

        if(object.has("p-settings")) {
            ConcurrentMap<String, Object> playerSettings = builder.fromJson(object.get("p-settings"),
                    new TypeToken<ConcurrentMap<String, Object>>() {
                    }.getType());
            player.getPSettings().setSettings(playerSettings);
        }

        if(object.has("forge-tier")) {
            player.getForge().setTier(object.get("forge-tier").getAsInt());
        }

        if(object.has("forge-progress")) {
            player.getForge().setProgress(object.get("forge-progress").getAsInt());
        }

        if(object.has("lastloggedinday")) {
            player.getAttendenceManager().setLastLoggedInDate(LocalDate.parse(object.get("lastloggedinday").getAsString()));
        }

        if(object.has("attendanceprogress")) {
            HashMap<AttendanceTab, AttendanceProgress> temp = builder.fromJson(object.get("attendanceprogress"),
                    new TypeToken<HashMap<AttendanceTab, AttendanceProgress>>() {
                    }.getType());
            player.getAttendenceManager().getPlayerAttendanceProgress().putAll(temp);
        }

        if(object.has("vipEXP")) {
            player.getPlayerVIP().setExp(object.get("vipEXP").getAsInt());
        }

        if(object.has("cTicket")) {
            player.getPlayerVIP().setClaimedTicket(object.get("cTicket").getAsInt());
        }

        if(object.has("vipRPack")) {
            player.getPlayerVIP().setClaimedPack(object.get("vipRPack").getAsInt());
        }

        if(object.has("vipTotal")) {
            player.getPlayerVIP().setTotal(object.get("vipTotal").getAsInt());
        }

        if(object.has("packXP")) {
            player.getPlayerVIP().setPackXp(object.get("packXP").getAsInt());
        }

        if(object.has("vipDonate")){
            player.getPlayerVIP().setDonations(builder.fromJson(object.get("vipDonate"),
                    new TypeToken<List<Donation>>() {
                    }.getType()));
        }

        if(object.has("starterLevel")) {
            player.getStarter().setPosition(object.get("starterLevel").getAsInt());
        }

        if(object.has("starterXp")) {
            player.getStarter().setXp(object.get("starterXp").getAsInt());
        }

        if(object.has("starterTasks")) {
            player.getStarter().setTasks(builder.fromJson(object.get("starterTasks"),
                    new TypeToken<Map<StarterTasks, Boolean>>() {
                    }.getType()));
        }

        if(object.has("starterRewards")) {
            player.getStarter().getRewards().setRewards(builder.fromJson(object.get("starterRewards"),
                    new TypeToken<List<ProgressReward>>() {
                    }.getType()));
        }

        if (object.has("equip-slot")) {
            player.getEquipment()
                    .setSlots(builder.fromJson(object.get("equip-slot").getAsJsonArray(), SlotBonus[].class));
        }

        if (object.has("second-slot")) {
            player.getSecondaryEquipment()
                    .setSlots(builder.fromJson(object.get("second-slot").getAsJsonArray(), SlotBonus[].class));
        }

        if (object.has("charges")) {
            player.getItems()
                    .setCharges(builder.fromJson(object.get("charges"),  new TypeToken<Map<String, Integer>>() {
                    }.getType()));
        }


        return this;
    }

    private static void setNewRank(Player player, String rank){
        switch (rank) {
            case "GRACEFUL_DONATOR" -> player.setDonator(DonatorRank.GRACEFUL);
            case "CLERIC_DONATOR" -> player.setDonator(DonatorRank.CLERIC);
            case "TORMENTED_DONATOR" -> player.setDonator(DonatorRank.TORMENTED);
            case "MYSTICAL_DONATOR" -> player.setDonator(DonatorRank.MYSTICAL);
            case "OBSIDIAN_DONATOR" -> player.setDonator(DonatorRank.OBSIDIAN);
            case "MODERATOR" -> player.setRank(StaffRank.MODERATOR);
            case "ADMINISTRATOR" -> player.setRank(StaffRank.ADMINISTRATOR);
            case "DEVELOPER" -> player.setRank(StaffRank.DEVELOPER);
            case "HELPER" -> player.setRank(StaffRank.HELPER);
        }
        DonationManager.getInstance().handleDonor(player);
    }
}
