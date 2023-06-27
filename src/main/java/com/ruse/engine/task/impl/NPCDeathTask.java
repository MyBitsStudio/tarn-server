package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.globalevents.GlobalEventBossTask;
import com.ruse.model.Animation;
import com.ruse.model.DamageDealer;
import com.ruse.model.Locations.Location;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.util.Misc;
import com.ruse.util.RandomUtility;
import com.ruse.world.World;
import com.ruse.world.content.*;
import com.ruse.world.content.StarterTasks.StarterTaskData;
import com.ruse.world.content.achievement.Achievements;
import com.ruse.world.content.battlepass.BattlePassData;
import com.ruse.world.content.battlepass.BattlePassHandler;
import com.ruse.world.content.bossEvents.BossEventHandler;
import com.ruse.world.content.combat.strategy.impl.Exoden;
import com.ruse.world.content.combat.strategy.impl.KalphiteQueen;
import com.ruse.world.content.combat.strategy.impl.Nex;
import com.ruse.world.content.dailyTask.DailyTaskHandler;
import com.ruse.world.content.dailytasks_new.DailyTask;
import com.ruse.world.event.youtube.YoutubeBoss;
import com.ruse.world.packages.donation.boss.DonationBoss;
import com.ruse.world.content.eventboss.EventBossDropHandler;
import com.ruse.world.content.globalBoss.GlobalBoss;
import com.ruse.world.content.globalBoss.GlobalBossHandler;
import com.ruse.world.content.globalBoss.TheGeneral;
import com.ruse.world.content.progressionzone.ProgressionZone;
import com.ruse.world.packages.seasonpass.SeasonPassManager;
import com.ruse.world.content.skeletalhorror.SkeletalHorror;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.packages.voting.VoteBossDrop;
import com.ruse.world.entity.impl.mini.MiniPlayer;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an npc's death task, which handles everything an npc does before
 * and after their death animation (including it), such as dropping their drop
 * table items.
 *
 * @author relex lawl
 */

public class NPCDeathTask extends Task {
    public static NPC FRIEZA;
    // this
    // array
    // of
    // npcs
    // to
    // change
    // the
    // npcs
    // you
    // want
    // to
    // give
    // boss
    // points
    /**
     * The npc setting off the death task.
     */
    private final NPC npc;
    /*
     * The array which handles what bosses will give a player points after death
     */
    private Set<Integer> BOSSES = new HashSet<>(Arrays.asList(1999, 440, 2882, 2881, 2883, 7134, 5666, 7286, 4540, 6222, 252,
            449, 452, 6260, 6247, 6203, 8349, 50, 2001, 1158, 8133, 3200, 13447, 8549, 1382, 2000, 2009, 2006, 8000,
            8002, 6430, 185, 3831, 2342, 2949, 1120, 8015)); // use
    /**
     * The amount of ticks on the task.
     */
    private int ticks = 2;

    /**
     * The player who killed the NPC
     */
    private Player killer;

    /**
     * The NPCDeathTask constructor.
     *
     * @param npc The npc being killed.
     */
    public NPCDeathTask(NPC npc) {
        super(2);
        this.npc = npc;
        this.ticks = 2;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void execute() {
        try {
            npc.setEntityInteraction(null);
            switch (ticks) {
                case 2:
                    npc.getMovementQueue().setLockMovement(true).reset();

                    DamageDealer damageDealer = npc.getCombatBuilder().getTopDamageDealer(false, null);
                    killer = damageDealer == null ? null : damageDealer.getPlayer();

                    if(killer != null) {
                        if (killer.getInstance() != null) {
                            killer.getInstance().setCanLeave((System.currentTimeMillis() + (1000 * 2)));
                        }
                    }

                    if(BattlePassData.isBoss(npc.getId())) {
                        BattlePassHandler.GiveExperience(npc);
                    }

                    if (npc.getId() == GlobalEventBossTask.eventBossID) {
                       // World.sendMessage("Event Boss has died.");
                        World.deregister(npc);
                        return;
                    }

                    if ((killer instanceof MiniPlayer) && killer.getMiniPlayerOwner() != null) {
                        killer = killer.getMiniPlayerOwner();
                    }

                    if(killer != null){
                        killer.getControllerManager().processNPCDeath(npc);
                    }

                    if (!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
                        npc.performAnimation(new Animation(npc.getDefinition().getDeathAnim()));

                    /** CUSTOM NPC DEATHS **/
                    if (npc.getId() == 13447) {
                        Nex.handleDeath();
                    }

                    break;
                case 0:
                    if (killer != null) {
                        DryStreak dryStreak = killer.getDryStreak();
                        killer.getDryStreak().dryStreakMap.put(npc.getId(), dryStreak.getDryStreak(npc.getId()) + 1);
                        //System.out.println("Streak: " + dryStreak.getDryStreak(npc.getId()));
                        dryStreak.sendAlert(npc.getId());

                        boolean boss = (npc.getDefaultConstitution() > 2000);
                        if (!Nex.nexMinion(npc.getId()) && npc.getId() != 1158
                                && !(npc.getId() >= 3493 && npc.getId() <= 3497)) {
                            KillsTracker.submitById(killer, npc.getId(), true, npc.getDefinition().isBoss());
                            KillsTracker.submitById(killer, npc.getId(), false, npc.getDefinition().isBoss());
                        }

                        if (npc.getId() == 3) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 10000) {
                                killer.sendMessage("@blu@You have reached 10,000 kills and received a Dan's present.");
                                killer.getInventory().add(6542, 1);
                                //return;
                            }
                        }

                        if (npc.getId() == 9019) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 10000) {
                                killer.sendMessage("@blu@You have reached 10,000 kills and received a St. Patrick's Box.");
                                killer.getInventory().add(13802, 1);
                                //return;
                            }
                        }

                        SeasonPassManager.addNpcKillExp(killer.getSeasonPass(), npc.getDefinition().getName());

                        achieve(killer, npc.getId());
                        killTracker(killer, npc.getId());

                       /* if (BOSSES.contains(npc.getId())) {

                            killer.getPointsHandler().incrementBossPoints(1);
                            killer.sendMessage("<img=99>You now have @red@" + killer.getPointsHandler().getBossPoints()
                                    + " Boss Points!");
                        }*/

                        Achievements.doProgress(killer, Achievements.Achievement.KILL_5000_NPCS);
                        Achievements.doProgress(killer, Achievements.Achievement.KILL_10000_NPCS);
                        Achievements.doProgress(killer, Achievements.Achievement.KILL_25000_NPCS);


                        if (npc.getId() == 6260) {
                            killer.getAchievementAttributes().setGodKilled(0, true);
                        } else if (npc.getId() == 6222) {
                            killer.getAchievementAttributes().setGodKilled(1, true);
                        } else if (npc.getId() == 6247) {
                            killer.getAchievementAttributes().setGodKilled(2, true);
                        } else if (npc.getId() == 6203) {
                            killer.getAchievementAttributes().setGodKilled(3, true);
                        } else if (npc.getId() == 8133) {
                        } else if (npc.getId() == 13447) {
                            killer.getAchievementAttributes().setGodKilled(4, true);
                        }
                        if (killer.getLocation() == Location.CUSTOM_RAIDS) {
                            Dungeoneering.handleNpcDeath(killer, npc);
                        } // fixed, enjoy.

                        if (boss) {
                            DailyTask.BOSSES.tryProgress(killer);
                        }

                        for (Achievements.Achievement ach : Achievements.Achievement.values()) {
                            if (ach.getNpcId() != -1 && ach.getNpcId() == npc.getId())
                                Achievements.doProgress(killer, ach);
                        }

                        otherCheck(killer, npc.getId());

                        if (Misc.getRandom(500) == 0) {
                            killer.sendMessage("@red@You received a PVM Casket");
                            killer.getInventory().add(2736, 1);
                        }
                        ProgressionZone.handleKill(killer, npc.getId());

                        /** LOCATION KILLS **/
                        if (npc.getLocation().handleKilledNPC(killer, npc)) {
                            stop();
                            return;
                        }
                        if (npc.getPosition().getRegionId() == 7758) {
                            killer.vod.killBarrowsNpc(killer, npc, true);
                            stop();
                            return;
                        }

                        if (npc.getPosition().getRegionId() == 8782) {
                            killer.hov.killBarrowsNpc(npc, true);
                            stop();
                            return;
                        }

                        if (NPCDrops.multiKillNpcs.contains(npc.getId())) {
                            NPCDrops.dropItemsMultiKill(npc);
                        } else {
                            killer.getPointsHandler().incrementNPCKILLCount(1);
                            if (!npc.isEventBoss()) {
                                NPCDrops.handleDrops(killer, npc, 1);

                                // NPCDrops.dropItems(killer, npc);
                            }
                        }
                        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
                                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
                            killer.getPointsHandler().incrementNPCKILLCount(1);
                        }

                        int killCount = 1;

                        if (ItemEffect.hasTripleKills(killer)) {
                            killCount *= 3;
                        }

                        if (ItemEffect.hasDoubleKills(killer)) {
                            killCount *= 2;
                        }

                        killer.getPointsHandler().incrementNPCKILLCount(killCount);

                        if (npc instanceof GlobalBoss) {
                            GlobalBossHandler.onDeath((GlobalBoss) npc);
                        }

                        if (npc instanceof DonationBoss) {
                            ((DonationBoss) npc).handleDrops();
                        }

                        if(npc.getId() == 4540){
                            YoutubeBoss.drop(npc);
                        }

                        if (killer.getCurrentClue().getCurrentTask() != SlayerTasks.NO_TASK) {
                            if (killer.getCurrentClue().getAmountToSlay() > 0) {
                                int newV = killer.getCurrentClue().getAmountToSlay() - 1;
                                killer.getCurrentClue().setAmountToSlay(newV);
                                if (newV == 0) {
                                    killer.sendMessage("You have completed your clue task! Read the clue again to claim your prize!");
                                }

                            }
                        }

                        npc.onDeath();

                        if (npc.stopTask()) {
                            setEventRunning(false);
                            npc.setDying(false);
                            killer.getSlayer().killedNpc(npc);
                            npc.getCombatBuilder().getDamageMap().clear();
                            stop();
                            return;
                        } else {
                            new BossEventHandler().death(killer, npc, npc.getDefinition().getName());
                           // new InstanceManager(killer).death(killer, npc, npc.getDefinition().getName());
                            new DailyTaskHandler(killer).death(npc.getDefinition().getName());

                            /** SLAYER **/
                            killer.getSlayer().killedNpc(npc);
                            npc.getCombatBuilder().getDamageMap().clear();

                            TaskManager.submit(new Task(0, killer, true){
                                @Override
                                protected void execute() {
                                    setEventRunning(false);

                                    npc.setDying(false);

                                    if (Nex.nexMob(npc.getId())) {
                                        Nex.death(npc.getId());
                                    }

                                    if(killer != null){

                                        if (npc.isEventBoss()) {
                                            EventBossDropHandler.death(killer, npc);
                                        }

                                        if (npc.getId() == 1158 || npc.getId() == 1160) {
                                            KalphiteQueen.death(npc.getId(), npc.getPosition());
                                        }

                                        summoning(killer, npc.getId());

                                        if (npc.getId() == 186) {
                                            int random = RandomUtility.inclusiveRandom(0, 100);
                                            if (random < killer.getPointsHandler().getGlobalRate()) {// its using shillingrate though gthose go up to
                                                // ininfinty
                                                // well yeah i was just making an example, but im just saying, ur gona have to
                                                // add so much stuff for each npc if u dont create a system for it
                                                killer.getInventory().add(8212, 5);
                                                killer.getInventory().add(8213, 1);
                                                killer.getPointsHandler().incrementEventPoints(2);
                                                killer.sendMessage("Because of your 'Event rate' multiplier you got extra dust");
                                                killer.sendMessage("you also got a free Christmas token.");
                                            } else {
                                                killer.getInventory().add(8212, 2);
                                                killer.getPointsHandler().incrementEventPoints(2);
                                            }
                                        }

                                        if (npc.getId() == 5188) {// penguins
                                            killer.getInventory().add(12657, 50 + killer.getPointsHandler().getSHILLINGRate());

                                        }

                                    }

                                    if (npc.getDefinition().getRespawn() > 0 && npc.getLocation() != Location.GRAVEYARD && npc.getLocation() != Location.KEEPERS_OF_LIGHT_GAME
                                            && npc.getLocation() != Location.DUNGEONEERING && npc.getLocation() != Location.CUSTOM_RAIDS && !npc.isEventBoss()) {
                                        if (npc.respawn)
                                            TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawn(), killer));
                                    }

                                    if(killer == null) {
                                        World.deregister(npc);
                                    } else if (killer.getInstance() == null) {
                                        World.deregister(npc);
                                    } else {
                                        killer.getInstance().remove(npc);
                                    }


                                    super.stop();
                                }
                            });
                        }

                        super.stop();
                        break;

                    }
            }
            ticks--;
        } catch (Exception e) {
            e.printStackTrace();
            super.stop();
        }
    }

    private void summoning(Player killer, int npcId){
        if(killer == null){
            System.out.println("killer is null");
            return;
        }
        boolean tripleKills = ItemEffect.hasTripleKills(killer);
        boolean doubleKills = ItemEffect.hasDoubleKills(killer);
        boolean hasPet = killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302;
        int assign = 0;

        if(npcId == 1614) {
            if(tripleKills)
                assign += 2;
            else if(doubleKills)
                assign += 1;
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementNPCKILLCount(assign);
        }

        if(npcId == 603) {
            if(tripleKills)
                assign += 2;
            else if(doubleKills)
                assign += 1;
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementNPCKILLCount(assign);
        }

        if(npcId == 12843) {
            if(tripleKills)
                assign += 2;
            else if(doubleKills)
                assign += 1;
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementDEMONKILLCount(assign);
        }
        if(npcId == 8014) {
            if(tripleKills)
                assign += 2;
            else if(doubleKills)
                assign += 1;
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementDEMONKILLCount(assign);
        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 8008
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementAVATARKILLCount(2);
        } else if (npcId == 8008) {// avatar
            killer.getPointsHandler().incrementAVATARKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 3308
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementANGELKILLCount(2);
        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 3117
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementLUCIENKILLCount(2);
        } else if (npcId == 3117) {// lucien
            killer.getPointsHandler().incrementLUCIENKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 13635
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementKINGKILLCount(2);
        } else if (npcId == 13635) {// king
            killer.getPointsHandler().incrementKINGKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 201
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementHERCULESKILLCount(2);
        } else if (npcId == 201) {// hercules
            killer.getPointsHandler().incrementHERCULESKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 202
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementSATANKILLCount(2);
        } else if (npcId == 202) {// satan
            killer.getPointsHandler().incrementSATANKILLCount(1);

        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 203
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementZEUSKILLCount(2);
        } else if (npcId == 203) {// zeus
            killer.getPointsHandler().incrementZEUSKILLCount(1);
        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 53
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementDRAGONKILLCount(2);
        } else if (npcId == 53) {// dragon
            killer.getPointsHandler().incrementDRAGONKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 8018
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementBEASTKILLCount(2);
        } else if (npcId == 8018) {// beast
            killer.getPointsHandler().incrementBEASTKILLCount(1);
        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 9011
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            killer.getPointsHandler().incrementMiniLuciferKillCount(2);
        } else if (npcId == 9011) {// zeus
            killer.getPointsHandler().incrementMiniLuciferKillCount(1);
        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 9012
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            killer.getPointsHandler().incrementLuciferKillCount(2);
        } else if (npcId == 9012) {// zeus
            killer.getPointsHandler().incrementLuciferKillCount(1);
        }
    }

    private void achieve(Player killer, int npcId){
        if (npcId == 9028) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_MYSTIC);
        }
        if (npcId == 9029) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_NIGHTMARE);
        }
        if (npcId == 9030) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_PATIENCE);
        }
        if (npcId == 8014) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZINQRUX);
        }
        if (npcId == 8003) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_ABERRANT);
        }
        if (npcId == 202) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_INFERNO);
        }
        if (npcId == 811) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_NAGENDRA);
        }
        if (npcId == 9815) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_KOLGAL);
        }
        if (npcId == 9817) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_YISDAR);
        }
        if (npcId == 9920) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_IGTHAUR);
        }
        if (npcId == 3831) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZERNATH);
        }
        if (npcId == 9025) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_AVALON);
        }
        if (npcId == 9836) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_DOOMWATCHER);
        }
        if (npcId == 92) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_MAZE_GUARDIAN);
        }
        if (npcId == 3313) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_MISCREATION);
        }
        if (npcId == 8008) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_AVATAR_TITAN);
        }
        if (npcId == 1906) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZORBAK);
        }
        if (npcId == 9915) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_DEATH_GOD);
        }
        if (npcId == 2342) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_EMERALD_SLAYER);
        }
        if (npcId == 9024) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_GOLDEN_GOLEM);
        }
        if (npcId == 9916) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_LUFFY);
        }
        if (npcId == 9918) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_BROLY);
        }
        if (npcId == 9919) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_BOWSER);
        }
        if (npcId == 9914) {
            Achievements.doProgress(killer, Achievements.Achievement.KILL_SASUKE);
        }
        //BOSSES
        if (npcId == 9017) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_SANCTUM_GOLEM);
        }
        if (npcId == 9839) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_MUTANT_HYDRA);
        }
        if (npcId == 9806) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_GORVEK);
        }
        if (npcId == 9816) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_DRAGONITE);
        }
        if (npcId == 9903) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_ASMODEUS);
        }
        if (npcId == 8002) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_MALVEK);
        }
        if (npcId == 1746) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_ONYX_GRIFFIN);
        }
        if (npcId == 3010) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZEIDAN_GRIMM);
        }
        if (npcId == 3013) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_AGTHOMOTH);
        }
        if (npcId == 3014) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_LILINRYSS);
        }
        if (npcId == 8010) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_GROUDON);
        }
        if (npcId == 3016) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_VARTHRAMOTH);
        }
        if (npcId == 4972) {
            killer.getSeasonPass().incrementExp(3, false);
            Achievements.doProgress(killer, Achievements.Achievement.KILL_TYRANT_LORD);
        }
        if (npcId == 9012) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3019) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3020) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3021) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3305) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 9818) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 9912) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 9913) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3117) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3115) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 12239) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3112) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 3011) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 252) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 449) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 452) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 187) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 188) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 1311) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 1313) {
            killer.getSeasonPass().incrementExp(2, false);
        }
        if (npcId == 1318) {
            killer.getSeasonPass().incrementExp(2, false);
        }
    }

    private void killTracker(Player killer, int npcId){
        if (npcId == 1703) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 50) {
                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Jellyfish.");
            }
        }
        if (npcId == 1721) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 50) {
                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Jello.");
            }
        }
        if (npcId == 1729) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 50) {
                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Bunyip.");
            }
        }
        if (npcId == 1705) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 50) {
                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Gargoyle.");
            }
        }
        if (npcId == 1712) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 50) {
                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Flaming Butterfly.");
            }
        }
        if (npcId == 1711) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 100) {
                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Blast Cloud.");
            }
        }
        if (npcId == 1739) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 100) {
                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Dark Bloodveld.");
            }
        }
        if (npcId == 1710) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 100) {
                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Lavannoth.");
            }
        }
        if (npcId == 1702) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 100) {
                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Granite Crab.");
            }
        }
        if (npcId == 1700) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 100) {
                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Ant Worker.");
            }
        }
        if (npcId == 1724) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 200) {
                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Mosquito.");
            }
        }
        if (npcId == 1713) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 200) {
                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for War Plant.");
            }
        }
        if (npcId == 1737) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 200) {
                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Tycoons Bird");
            }
        }
        if (npcId == 1730) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 200) {
                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Nature Unicorn");
            }
        }
        if (npcId == 1742) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 200) {
                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Bronze Dragon");
            }
        }
        if (npcId == 1706) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 350) {
                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Zamorak Bird");
            }
        }
        if (npcId == 1725) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 350) {
                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Symbiote");
            }
        }
        if (npcId == 1727) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 350) {
                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Ghoulord");
            }
        }
        if (npcId == 1708) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 350) {
                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Grooter");
            }
        }
        if (npcId == 1744) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 350) {
                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Elemental Moss");
            }
        }
        if (npcId == 1740) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Fire Moss");
            }
        }
        if (npcId == 1741) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Pelican Bird");
            }
        }
        if (npcId == 1709) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Runite Turtle");
            }
        }
        if (npcId == 1745) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Sabertooth");
            }
        }
        if (npcId == 1731) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Armoured Minotaur");
            }
        }
        if (npcId == 1719) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Native Demon");
            }
        }
        if (npcId == 1715) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Wild Graahk");
            }
        }
        if (npcId == 1734) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Leopard");
            }
        }
        if (npcId == 1733) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 500) {
                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Kree Devil");
            }
        }
        if (npcId == 1735) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 750) {
                killer.sendMessage("<img=5>@blu@You have reached 750 kills and completed the requirement for Hyndra");
            }
        }
        if (npcId == 1743) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 750) {
                killer.sendMessage("<img=5>@blu@You have reached 750 kills and completed the requirement for Evil Chinchompa");
            }
        }
        if (npcId == 1723) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 750) {
                killer.sendMessage("<img=5>@blu@You have reached 750 kills and completed the requirement for Chinese Dragon");
            }
        }
        if (npcId == 1716) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1000) {
                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Elite Dragon");
            }
        }
        if (npcId == 8015) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1000) {
                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Eternal Dragon");
            }
        }
        if (npcId == 4972) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1000) {
                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Scarlet Falcon");
            }
        }
        if (npcId == 2949) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1000) {
                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Crystal Queen");
            }
        }
        if (npcId == 6430) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1000) {
                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Lucifer");
            }
        }
        if (npcId == 9012) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1250) {
                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Mega Avatar");
            }
        }
        if (npcId == 1234) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1250) {
                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Light Supreme");
            }
        }
        if (npcId == 440) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1250) {
                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Dark Supreme");
            }
        }
        if (npcId == 438) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 1250) {
                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Fractite Demon");
            }
        }
        if (npcId == 12843) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 5000) {
                killer.sendMessage("<img=5>@blu@You have reached 5000 kills and completed the requirement for Perfect Cell");
            }
        }
        if (npcId == 449) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 2000) {
                killer.sendMessage("<img=5>@blu@You have reached 2000 kills and completed the requirement for Super Buu");
            }
        }
        if (npcId == 452) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 2500) {
                killer.sendMessage("<img=5>@blu@You have reached 2500 kills and completed the requirement for Frieza");
            }
        }
        if (npcId == 252) {
            int total = KillsTracker.getTotalKillsForNpc(npcId, killer);
            if (total == 10000) {
                killer.sendMessage("<img=5>@blu@You have reached 10000 kills and completed the requirement for Goku");
            }
        }
    }

    private void otherCheck(Player killer, int npcId){
        if (npcId == Wildywyrm.NPC_ID) {
            Wildywyrm.wyrmAlive = false;
            World.getPlayers().forEach(PlayerPanel::refreshPanel);
        }

        if (npcId == SkeletalHorror.NPC_ID) {
            SkeletalHorror.wyrmAlive = false;
        }
        if (npcId == 6203 || npcId == 6260 || npcId == 6247 || npcId == 6222) { // done
            StarterTasks.doProgress(killer, StarterTaskData.KILL_20_GWD_BOSSES);
        }
        if (npcId == 1023) { // done
            StarterTasks.doProgress(killer, StarterTaskData.KILL_100_STARTER);
        }
        if (npcId == 4972) { // done
            StarterTasks.doProgress(killer, StarterTaskData.KILL_ETERNAL);
        }

        if (!(npcId == 1)) {
            StarterTasks.doProgress(killer, StarterTaskData.REACH_1000_TOTAL);
        }
        /** PARSE DROPS **/
        if (npcId == 3830) {
            SkeletalHorror.handleDrop(npc);
        }


        /** PARSE DROPS **/

        if (npc.getId() == 8013) {// resets the vote count to 0 on votizo
            VoteBossDrop.handleDrop(npc);
            World.sendMessage("<shad=f9f6f6>Vote boss has been slain...");

        }

//        if (npc.getId() == 9908) {
//            WorldBosses.handleDrop(npc);
//        }
        if (npc.getId() == 9904) {
            WorldBosses3.handleDrop(npc);
        }
//        if (npc.getId() == 9906) {
//            WorldBosses2.handleDrop(npc);
//        }
//        if (npc.getId() == 9907) {
//            WorldBosses4.handleDrop(npc);
//        }
        if (npc.getId() == 8010) {
            Groudon.handleDrop(npc);
        }
        if (npc.getId() == 3112) {
            Ezkel.handleDrop(npc);
        }
        if (npc.getId() == 12239) {
            SupremeNex.handleDrop(npc);
        }

        if (npc.getId() == 7553) {
            TheGeneral.giveLoot(killer, npc);
        }
        if (npc.getId() == Exoden.MINION_NPCID) {
            Exoden.minions_dead = true;
            Exoden.minions_spawned = false;
        }
    }

}
