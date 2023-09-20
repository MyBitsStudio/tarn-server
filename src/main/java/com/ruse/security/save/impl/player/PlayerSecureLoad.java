package com.ruse.security.save.impl.player;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ruse.model.*;
import com.ruse.model.container.impl.Bank;
import com.ruse.model.container.impl.UimBank;
import com.ruse.security.save.SecureLoad;
import com.ruse.world.content.DropLog;
import com.ruse.world.content.KillsTracker;
import com.ruse.world.content.LoyaltyProgramme;
import com.ruse.world.packages.attendance.AttendanceProgress;
import com.ruse.world.packages.attendance.AttendanceTab;
import com.ruse.world.packages.collectionlog.CollectionEntry;
import com.ruse.world.packages.collectionlog.CollectionLogInterface;
import com.ruse.world.content.combat.magic.CombatSpells;
import com.ruse.world.content.combat.weapon.FightType;
import com.ruse.world.content.dailytasks_new.DailyTask;
import com.ruse.world.content.dailytasks_new.TaskChallenge;
import com.ruse.world.content.groupironman.GroupManager;
import com.ruse.world.content.groupironman.IronmanGroup;
import com.ruse.world.content.skill.SkillManager;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementProgress;
import com.ruse.world.packages.johnachievementsystem.Perk;
import com.ruse.world.packages.johnachievementsystem.PerkType;
import com.ruse.world.packages.mode.GameModeConstants;
import com.ruse.world.packages.ranks.DonatorRank;
import com.ruse.world.packages.ranks.StaffRank;
import com.ruse.world.packages.ranks.VIPRank;
import com.ruse.world.packages.skills.slayer.SlayerMasters;
import com.ruse.world.packages.skills.slayer.SlayerTask;
import com.ruse.world.packages.slot.SlotBonus;
import com.ruse.world.packages.starter.StartShopItems;
import com.ruse.world.packages.tracks.ProgressReward;
import com.ruse.world.packages.tracks.impl.starter.StarterTasks;
import com.ruse.world.packages.tracks.impl.tarn.normal.TarnNormalTasks;
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

        if (object.has("last-donation")) {
            player.lastDonation = object.get("last-donation").getAsLong();
        }

        if (object.has("last-time-reset")) {
            player.lastDonation = object.get("last-time-reset").getAsLong();
        }

        if (object.has("email")) {
            player.setEmailAddress(object.get("email").getAsString());
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

        if (object.has("total-gained-exp")) {
            player.getSkillManager().setTotalGainedExp(object.get("total-gained-exp").getAsInt());
        }


        if (object.has("Skilling-points")) {
            player.getPointsHandler().setSkillingPoints(object.get("Skilling-points").getAsInt(), false);
        }

        if (object.has("commendations")) {
            player.getPointsHandler().setCommendations(object.get("commendations").getAsInt(), false);
        }


        if (object.has("voting-points")) {
            player.getPointsHandler().setVotingPoints(object.get("voting-points").getAsInt(), false);
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

        if (object.has("accept-aid")) {
            player.setAcceptAid(object.get("accept-aid").getAsBoolean());
        }
        if (object.has("poison-damage")) {
            player.setPoisonDamage(object.get("poison-damage").getAsInt());
        }
        if (object.has("poison-immunity")) {
            player.setPoisonImmunity(object.get("poison-immunity").getAsInt());
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


        if (object.has("summon-npc")) {
            int npc = object.get("summon-npc").getAsInt();
            //System.out.println("npc: " + npc);
            if (npc > 0)
                player.getVariables().setSetting("summon-npc", npc);
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

        if (object.has("slayer-master")) {
            player.getSlayer().setMaster(SlayerMasters.valueOf(object.get("slayer-master").getAsString()));
        }

        if (object.has("slayer-task")) {
            player.getSlayer().setTask(builder.fromJson(object.get("slayer-task"),  new TypeToken<SlayerTask>() {
            }.getType()));
        }

        if (object.has("slayer-last-task")) {
            player.getSlayer().setLastTask(builder.fromJson(object.get("slayer-last-task"),  new TypeToken<SlayerTask>() {
            }.getType()));
        }

        if (object.has("slayer-streak")) {
            player.getSlayer().setStreak(object.get("slayer-streak").getAsInt());
        }

        if (object.has("lastlogin"))
            player.lastLogin = (object.get("lastlogin").getAsLong());
        if (object.has("lastdailyclaim"))
            player.lastDailyClaim = (object.get("lastdailyclaim").getAsLong());


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

        if (object.has("yellhexcolor")) {
            player.setYellHex(object.get("yellhexcolor").getAsString());
        }

        if (object.has("yell-tit")) {
            player.setYellTitle(object.get("yell-tit").getAsString());
        }


        if (object.has("reffered")) {
            player.setReffered(object.get("reffered").getAsBoolean());
        }

        if (object.has("is-secondary-equipment")) {
            player.setIsSecondaryEquipment(object.get("is-secondary-equipment").getAsBoolean());
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

        if (object.has("secondary-equipment-slots-unlocked")) {
            player.setSecondaryEquipmentUnlocks(builder.fromJson(object.get("secondary-equipment-slots-unlocked").getAsJsonArray(), boolean[].class));
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

        if(object.has("vipPoints")) {
            player.getPlayerVIP().setPoints(object.get("vipPoints").getAsInt());
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

        if(object.has("starter-shop")) {
            player.getStarterShop().load(builder.fromJson(object.get("starter-shop"),
                    new TypeToken<Map<StartShopItems, Integer>>() {
                    }.getType()));
        }

        player.setIsSecondaryEquipment(false);
        if (object.has("equip-slot")) {
            player.getEquipment()
                    .setSlots(builder.fromJson(object.get("equip-slot").getAsJsonArray(), SlotBonus[].class));
        }

        player.setIsSecondaryEquipment(true);
        if (object.has("second-slot")) {
            player.getEquipment()
                    .setSlots(builder.fromJson(object.get("second-slot").getAsJsonArray(), SlotBonus[].class));
        }
        player.setIsSecondaryEquipment(false);

        if (object.has("charges")) {
            player.getItems()
                    .setCharges(builder.fromJson(object.get("charges"),  new TypeToken<Map<String, Integer>>() {
                    }.getType()));
        }

        if (object.has("points")) {
            player.getPoints().load(builder.fromJson(object.get("points"),  new TypeToken<Map<String, Integer>>() {
                    }.getType()));
        }

        if (object.has("towerTier")) {
           player.getTower().setTier(object.get("towerTier").getAsInt());
        }

        if (object.has("towerLevel")) {
            player.getTower().setLevel(object.get("towerLevel").getAsInt());
        }

        if (object.has("towerRewards")) {
            player.getTower().getRewards().setItems(builder.fromJson(object.get("towerRewards").getAsJsonArray(), Item[].class));
        }

        if (object.has("timers")) {
            player.getTimers().load(builder.fromJson(object.get("timers"),  new TypeToken<Map<String, Long>>() {
            }.getType()), player);
        }

        if(object.has("achievements-map")){
            player.getAchievementsMap().putAll(builder.fromJson(object.get("achievements-map"),
                    new TypeToken<Map<Integer, AchievementProgress>>() {
                    }.getType()));
        }

        if(object.has("achievement-points")){
            player.setAchievementPoints(object.get("achievement-points").getAsInt());
        }

        if(object.has("ach-perks")){
            player.setPerks(builder.fromJson(object.get("ach-perks"),
                    new TypeToken<Map<PerkType, Perk>>() {
                    }.getType()));
        }

        if(object.has("tarn-normal")) {
            player.getTarnNormal().setTasks(builder.fromJson(object.get("tarn-normal"),
                    new TypeToken<Map<TarnNormalTasks, Boolean>>() {
                    }.getType()));
        }

        if(object.has("normal-level")) {
            player.getTarnNormal().setPosition(object.get("normal-level").getAsInt());
        }

        if(object.has("normal-xp")) {
            player.getStarter().setXp(object.get("normal-xp").getAsInt());
        }

        if(object.has("normal-rewards")) {
            player.getTarnNormal().getRewards().setRewards(builder.fromJson(object.get("normal-rewards"),
                    new TypeToken<List<ProgressReward>>() {
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
    }
}
