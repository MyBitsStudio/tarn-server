package com.ruse.engine.task.impl;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.engine.task.impl.globalevents.GlobalEventBossTask;
import com.ruse.model.Animation;
import com.ruse.model.DamageDealer;
import com.ruse.model.Locations.Location;
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
import com.ruse.world.content.tbdminigame.Game;
import com.ruse.world.content.tbdminigame.Lobby;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.content.eventboss.EventBossDropHandler;
import com.ruse.world.content.globalBoss.GlobalBoss;
import com.ruse.world.content.globalBoss.GlobalBossHandler;
import com.ruse.world.content.globalBoss.TheGeneral;
import com.ruse.world.content.progressionzone.ProgressionZone;
import com.ruse.world.packages.seasonpass.SeasonPassManager;
import com.ruse.world.content.skeletalhorror.SkeletalHorror;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
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
    private int ticks;

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

    @Override
    public void execute() {
        try {
            npc.setEntityInteraction(null);
            switch (ticks) {
                case 2 -> {
                    npc.getMovementQueue().setLockMovement(true).reset();
                    DamageDealer damageDealer = npc.getCombatBuilder().getTopDamageDealer(false, null);
                    killer = damageDealer == null ? null : damageDealer.getPlayer();
                    if (killer != null) {
                        if (killer.getInstance() != null) {
                            killer.getInstance().setCanLeave((System.currentTimeMillis() + (1000 * 2)));
                        }
                    }
                    if ((killer instanceof MiniPlayer) && killer.getMiniPlayerOwner() != null) {
                        killer = killer.getMiniPlayerOwner();
                    }
                    if (killer != null) {
                        killer.getControllerManager().processNPCDeath(npc);
                    }
                    if (!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
                        npc.performAnimation(new Animation(npc.getDefinition().getDeathAnim()));
                }
                case 0 -> {
                    if (killer != null) {
                        DryStreak dryStreak = killer.getDryStreak();
                        killer.getDryStreak().dryStreakMap.put(npc.getId(), dryStreak.getDryStreak(npc.getId()) + 1);
                        dryStreak.sendAlert(npc.getId());

                        SeasonPassManager.addNpcKillExp(killer.getSeasonPass(), npc.getDefinition().getName());

                        Game game;
                        if((game = Lobby.getInstance().getGame()) != null) {
                            game.npcDeath(npc, killer);
                        }

                        achieve(killer, npc.getId());
                        killTracker(killer, npc.getId());

                        Achievements.doProgress(killer, Achievements.Achievement.KILL_5000_NPCS);
                        Achievements.doProgress(killer, Achievements.Achievement.KILL_10000_NPCS);
                        Achievements.doProgress(killer, Achievements.Achievement.KILL_25000_NPCS);

                        for (Achievements.Achievement ach : Achievements.Achievement.values()) {
                            if (ach.getNpcId() != -1 && ach.getNpcId() == npc.getId())
                                Achievements.doProgress(killer, ach);
                        }

                        otherCheck(killer, npc.getId());

                        if (Misc.getRandom(500) == 0) {
                            killer.sendMessage("@red@You received a PVM Casket");
                            killer.getInventory().add(2736, 1);
                        }

                        /** LOCATION KILLS **/
                        if (npc.getLocation().handleKilledNPC(killer, npc)) {
                            stop();
                            return;
                        }


                        if (!npc.isEventBoss()) {
                            DropManager.getManager().sendDrop(npc, killer);
                        }

                        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null
                                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
                            killer.getPointsHandler().incrementNPCKILLCount(1);
                        }

                        int killCount = 1;

                        if (killer.getEquipment().hasQuadKills()) {
                            killCount = 4;
                        } else if (killer.getEquipment().hasTripleKills()) {
                            killCount = 3;
                        } else if (killer.getEquipment().hasDoubleKills()) {
                            killCount = 2;
                        }

                        killer.getPointsHandler().incrementNPCKILLCount(killCount);

                        KillsTracker.submitById(killer, npc.getId(), false, npc.getDefinition().isBoss());

                        killer.getStarter().handleKillCount(npc.getId());

                        if (npc instanceof GlobalBoss) {
                            GlobalBossHandler.onDeath((GlobalBoss) npc);
                        }

                        if (npc.stopTask()) {
                            setEventRunning(false);
                            npc.setDying(false);
                            killer.getSlayer().handleSlayerTask(killer, npc.getId());
                            npc.getCombatBuilder().getDamageMap().clear();
                            stop();
                            return;
                        } else {
                            new BossEventHandler().death(killer, npc, npc.getDefinition().getName());

                            /** SLAYER **/
                            killer.getSlayer().handleSlayerTask(killer, npc.getId());
                            npc.getCombatBuilder().getDamageMap().clear();

                            TaskManager.submit(new Task(0, killer, true) {
                                @Override
                                protected void execute() {
                                    setEventRunning(false);

                                    npc.setDying(false);

                                    if (killer != null) {

                                        if (npc.isEventBoss()) {
                                            EventBossDropHandler.death(killer, npc);
                                        }

                                        summoning(killer, npc.getId());

                                    }

                                    if (killer == null) {
                                        World.deregister(npc);
                                    } else if (killer.getInstance() == null) {
                                        World.deregister(npc);
                                    } else {
                                        killer.getInstance().remove(npc);
                                    }

                                    if (npc.getDefinition().getRespawn() > 0 && !npc.isEventBoss()) {
                                        if (npc.respawn)
                                            TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawn(), killer));
                                    }


                                    super.stop();
                                }
                            });
                        }

                        super.stop();

                    }
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

        boolean hasPet = killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302;
        int assign = 0;

        if(npcId == 1614) {
            if(killer.getEquipment().hasQuadKills()){
                assign += 3;
            } else if(killer.getEquipment().hasTripleKills()){
                assign += 2;
            } else if(killer.getEquipment().hasDoubleKills()){
                assign += 1;
            }
            assign++;
            killer.getPointsHandler().incrementNPCKILLCount(assign);
        }

        if(npcId == 603) {
            if(killer.getEquipment().hasQuadKills()){
                assign += 3;
            } else if(killer.getEquipment().hasTripleKills()){
                assign += 2;
            } else if(killer.getEquipment().hasDoubleKills()){
                assign += 1;
            }
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementNPCKILLCount(assign);
        }

        if(npcId == 12843) {
            if(killer.getEquipment().hasQuadKills()){
                assign += 3;
            } else if(killer.getEquipment().hasTripleKills()){
                assign += 2;
            } else if(killer.getEquipment().hasDoubleKills()){
                assign += 1;
            }
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementDEMONKILLCount(assign);
        }
        if(npcId == 8014) {
            if(killer.getEquipment().hasQuadKills()){
                assign += 3;
            } else if(killer.getEquipment().hasTripleKills()){
                assign += 2;
            } else if(killer.getEquipment().hasDoubleKills()){
                assign += 1;
            }
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
//        if (npcId == 6203 || npcId == 6260 || npcId == 6247 || npcId == 6222) { // done
//            StarterTasks.doProgress(killer, StarterTaskData.KILL_20_GWD_BOSSES);
//        }
//        if (npcId == 1023) { // done
//            StarterTasks.doProgress(killer, StarterTaskData.KILL_100_STARTER);
//        }
//        if (npcId == 4972) { // done
//            StarterTasks.doProgress(killer, StarterTaskData.KILL_ETERNAL);
//        }
//
//        if (!(npcId == 1)) {
//            StarterTasks.doProgress(killer, StarterTaskData.REACH_1000_TOTAL);
//        }
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
//        if (npc.getId() == 9904) {
//            WorldBosses3.handleDrop(npc);
//        }
//        if (npc.getId() == 9906) {
//            WorldBosses2.handleDrop(npc);
//        }
//        if (npc.getId() == 9907) {
//            WorldBosses4.handleDrop(npc);
//        }
    }

}
