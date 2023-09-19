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
import com.ruse.world.content.bossEvents.BossEventHandler;
import com.ruse.world.content.tbdminigame.Game;
import com.ruse.world.content.tbdminigame.Lobby;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.content.eventboss.EventBossDropHandler;
import com.ruse.world.content.globalBoss.GlobalBoss;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.seasonpass.SeasonPassManager;
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

                        SeasonPassManager.addNpcKillExp(killer.getSeasonPass(), npc.getDefinition().getId());

                        Game game;
                        if((game = Lobby.getInstance().getGame()) != null) {
                            game.npcDeath(npc, killer);
                        }

                        achieve(killer, npc.getId());
                        killTracker(killer, npc.getId());

                        AchievementHandler.progress(killer, 1, 0);
                        AchievementHandler.progress(killer, 1, 1);
                        AchievementHandler.progress(killer, 1, 15);
                        AchievementHandler.progress(killer, 1, 16);
                        AchievementHandler.progress(killer, 1, 28);
                        AchievementHandler.progress(killer, 1, 29);
                        AchievementHandler.progress(killer, 1, 52);
                        AchievementHandler.progress(killer, 1, 53);
                        AchievementHandler.progress(killer, 1, 74);
                        AchievementHandler.progress(killer, 1, 75);


                        otherCheck(killer, npc.getId());

                        if (Misc.getRandom(1000) == 3) {
                            killer.sendMessage("@red@You received a PVM Casket");
                            killer.getInventory().add(2736, 1);
                        }

                        if(Misc.random(9862) == 13){
                            killer.getInventory().add(25103, 1);
                            killer.sendMessage("@red@You received a Locked PvM Chest!");
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
                        killer.getTarnNormal().handleKillCount(npc.getId());

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

                                    npc.onDeath(killer);

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
            //System.out.println("killer is null");
            return;
        }

        boolean hasPet = killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302;
        int assign = 0;

    }

    private void achieve(Player killer, int npcId){

    }

    private void killTracker(Player killer, int npcId){

    }

    private void otherCheck(Player killer, int npcId){

    }

}
