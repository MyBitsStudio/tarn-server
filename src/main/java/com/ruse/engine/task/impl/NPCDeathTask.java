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
import com.ruse.world.content.eventboss.EventBossDropHandler;
import com.ruse.world.content.globalBoss.GlobalBoss;
import com.ruse.world.content.globalBoss.GlobalBossHandler;
import com.ruse.world.content.globalBoss.TheGeneral;
import com.ruse.world.content.instanceMananger.InstanceManager;
import com.ruse.world.content.progressionzone.ProgressionZone;
import com.ruse.world.content.skeletalhorror.SkeletalHorror;
import com.ruse.world.content.skill.impl.old_dungeoneering.Dungeoneering;
import com.ruse.world.content.skill.impl.slayer.SlayerMaster;
import com.ruse.world.content.skill.impl.slayer.SlayerTasks;
import com.ruse.world.entity.impl.mini.MiniPlayer;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.world.content.globalBoss.merk.MerkSpawn;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

//import com.ruse.tools.discord.DiscordConstant;
//import com.ruse.tools.discord.DiscordManager;
//import com.ruse.world.content.dbz.Frieza;

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
    private Player killer = null;

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
                    if (killer instanceof Player) {
                        killer.getControllerManager().processNPCDeath(npc);
                    }

                    if (!(npc.getId() >= 6142 && npc.getId() <= 6145) && !(npc.getId() > 5070 && npc.getId() < 5081))
                        npc.performAnimation(new Animation(npc.getDefinition().getDeathAnimation()));

                    /** CUSTOM NPC DEATHS **/
                    if (npc.getId() == 13447) {
                        Nex.handleDeath();
                    }

                    break;
                case 0:
                    if (killer != null) {
                        DryStreak dryStreak = killer.getDryStreak();
                        killer.getDryStreak().dryStreakMap.put(npc.getId(), dryStreak.getDryStreak(npc.getId())+1);
                        //System.out.println("Streak: " + dryStreak.getDryStreak(npc.getId()));
                        dryStreak.sendAlert(npc.getId());

                        boolean boss = (npc.getDefaultConstitution() > 2000);
                        if (!Nex.nexMinion(npc.getId()) && npc.getId() != 1158
                                && !(npc.getId() >= 3493 && npc.getId() <= 3497)) {
                            KillsTracker.submitById(killer, npc.getId(), true, npc.getDefinition().boss);
                            KillsTracker.submitById(killer, npc.getId(), false, npc.getDefinition().boss);
                            if (boss) {
                            }
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


                        if (npc.getId() == 9028) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_MYSTIC);
                        }
                        if (npc.getId() == 9029) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_NIGHTMARE);
                        }
                        if (npc.getId() == 9030) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_PATIENCE);
                        }
                        if (npc.getId() == 8014) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZINQRUX);
                        }
                        if (npc.getId() == 8003) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_ABERRANT);
                        }
                        if (npc.getId() == 202) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_INFERNO);
                        }
                        if (npc.getId() == 811) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_NAGENDRA);
                        }
                        if (npc.getId() == 9815) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_KOLGAL);
                        }
                        if (npc.getId() == 9817) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_YISDAR);
                        }
                        if (npc.getId() == 9920) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_IGTHAUR);
                        }
                        if (npc.getId() == 3831) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZERNATH);
                        }
                        if (npc.getId() == 9025) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_AVALON);
                        }
                        if (npc.getId() == 9836) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_DOOMWATCHER);
                        }
                        if (npc.getId() == 92) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_MAZE_GUARDIAN);
                        }
                        if (npc.getId() == 3313) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_MISCREATION);
                        }
                        if (npc.getId() == 8008) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_AVATAR_TITAN);
                        }
                        if (npc.getId() == 1906) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZORBAK);
                        }
                        if (npc.getId() == 9915) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_DEATH_GOD);
                        }
                        if (npc.getId() == 2342) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_EMERALD_SLAYER);
                        }
                        if (npc.getId() == 9024) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_GOLDEN_GOLEM);
                        }
                        if (npc.getId() == 9916) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_LUFFY);
                        }
                        if (npc.getId() == 9918) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_BROLY);
                        }
                        if (npc.getId() == 9919) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_BOWSER);
                        }
                        if (npc.getId() == 9914) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_SASUKE);
                        }
                        //BOSSES
                        if (npc.getId() == 9017) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_SANCTUM_GOLEM);
                        }
                        if (npc.getId() == 9839) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_MUTANT_HYDRA);
                        }
                        if (npc.getId() == 9806) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_GORVEK);
                        }
                        if (npc.getId() == 9816) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_DRAGONITE);
                        }
                        if (npc.getId() == 9903) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_ASMODEUS);
                        }
                        if (npc.getId() == 8002) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_MALVEK);
                        }
                        if (npc.getId() == 1746) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_ONYX_GRIFFIN);
                        }
                        if (npc.getId() == 3010) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_ZEIDAN_GRIMM);
                        }
                        if (npc.getId() == 3013) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_AGTHOMOTH);
                        }
                        if (npc.getId() == 3014) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_LILINRYSS);
                        }
                        if (npc.getId() == 8010) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_GROUDON);
                        }
                        if (npc.getId() == 3016) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_VARTHRAMOTH);
                        }
                        if (npc.getId() == 4972) {
                            Achievements.doProgress(killer, Achievements.Achievement.KILL_TYRANT_LORD);
                        }


                        if (npc.getId() == 1703) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 50) {
                                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Jellyfish.");
                            }
                        }
                        if (npc.getId() == 1721) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 50) {
                                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Jello.");
                            }
                        }
                        if (npc.getId() == 1729) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 50) {
                                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Bunyip.");
                            }
                        }
                        if (npc.getId() == 1705) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 50) {
                                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Gargoyle.");
                            }
                        }
                        if (npc.getId() == 1712) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 50) {
                                killer.sendMessage("<img=5>@blu@You have reached 50 kills and completed the requirement for Flaming Butterfly.");
                            }
                        }
                        if (npc.getId() == 1711) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 100) {
                                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Blast Cloud.");
                            }
                        }
                        if (npc.getId() == 1739) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 100) {
                                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Dark Bloodveld.");
                            }
                        }
                        if (npc.getId() == 1710) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 100) {
                                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Lavannoth.");
                            }
                        }
                        if (npc.getId() == 1702) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 100) {
                                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Granite Crab.");
                            }
                        }
                        if (npc.getId() == 1700) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 100) {
                                killer.sendMessage("<img=5>@blu@You have reached 100 kills and completed the requirement for Ant Worker.");
                            }
                        }
                        if (npc.getId() == 1724) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 200) {
                                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Mosquito.");
                            }
                        }
                        if (npc.getId() == 1713) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 200) {
                                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for War Plant.");
                            }
                        }
                        if (npc.getId() == 1737) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 200) {
                                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Tycoons Bird");
                            }
                        }
                        if (npc.getId() == 1730) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 200) {
                                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Nature Unicorn");
                            }
                        }
                        if (npc.getId() == 1742) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 200) {
                                killer.sendMessage("<img=5>@blu@You have reached 200 kills and completed the requirement for Bronze Dragon");
                            }
                        }
                        if (npc.getId() == 1706) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 350) {
                                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Zamorak Bird");
                            }
                        }
                        if (npc.getId() == 1725) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 350) {
                                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Symbiote");
                            }
                        }
                        if (npc.getId() == 1727) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 350) {
                                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Ghoulord");
                            }
                        }
                        if (npc.getId() == 1708) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 350) {
                                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Grooter");
                            }
                        }
                        if (npc.getId() == 1744) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 350) {
                                killer.sendMessage("<img=5>@blu@You have reached 350 kills and completed the requirement for Elemental Moss");
                            }
                        }
                        if (npc.getId() == 1740) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Fire Moss");
                            }
                        }
                        if (npc.getId() == 1741) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Pelican Bird");
                            }
                        }
                        if (npc.getId() == 1709) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Runite Turtle");
                            }
                        }
                        if (npc.getId() == 1745) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Sabertooth");
                            }
                        }
                        if (npc.getId() == 1731) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Armoured Minotaur");
                            }
                        }
                        if (npc.getId() == 1719) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Native Demon");
                            }
                        }
                        if (npc.getId() == 1715) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Wild Graahk");
                            }
                        }
                        if (npc.getId() == 1734) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Leopard");
                            }
                        }
                        if (npc.getId() == 1733) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 500) {
                                killer.sendMessage("<img=5>@blu@You have reached 500 kills and completed the requirement for Kree Devil");
                            }
                        }
                        if (npc.getId() == 1735) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 750) {
                                killer.sendMessage("<img=5>@blu@You have reached 750 kills and completed the requirement for Hyndra");
                            }
                        }
                        if (npc.getId() == 1743) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 750) {
                                killer.sendMessage("<img=5>@blu@You have reached 750 kills and completed the requirement for Evil Chinchompa");
                            }
                        }
                        if (npc.getId() == 1723) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 750) {
                                killer.sendMessage("<img=5>@blu@You have reached 750 kills and completed the requirement for Chinese Dragon");
                            }
                        }
                        if (npc.getId() == 1716) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1000) {
                                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Elite Dragon");
                            }
                        }
                        if (npc.getId() == 8015) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1000) {
                                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Eternal Dragon");
                            }
                        }
                        if (npc.getId() == 4972) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1000) {
                                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Scarlet Falcon");
                            }
                        }
                        if (npc.getId() == 2949) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1000) {
                                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Crystal Queen");
                            }
                        }
                        if (npc.getId() == 6430) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1000) {
                                killer.sendMessage("<img=5>@blu@You have reached 1000 kills and completed the requirement for Lucifer");
                            }
                        }
                        if (npc.getId() == 9012) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1250) {
                                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Mega Avatar");
                            }
                        }
                        if (npc.getId() == 4540) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1250) {
                                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Crazy Witch");
                            }
                        }
                        if (npc.getId() == 1234) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1250) {
                                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Light Supreme");
                            }
                        }
                        if (npc.getId() == 440) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1250) {
                                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Dark Supreme");
                            }
                        }
                        if (npc.getId() == 438) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 1250) {
                                killer.sendMessage("<img=5>@blu@You have reached 1250 kills and completed the requirement for Fractite Demon");
                            }
                        }
                        if (npc.getId() == 12843) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 5000) {
                                killer.sendMessage("<img=5>@blu@You have reached 5000 kills and completed the requirement for Perfect Cell");
                            }
                        }
                        if (npc.getId() == 449) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 2000) {
                                killer.sendMessage("<img=5>@blu@You have reached 2000 kills and completed the requirement for Super Buu");
                            }
                        }
                        if (npc.getId() == 452) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 2500) {
                                killer.sendMessage("<img=5>@blu@You have reached 2500 kills and completed the requirement for Frieza");
                            }
                        }
                        if (npc.getId() == 252) {
                            int total = KillsTracker.getTotalKillsForNpc(npc.getId(), killer);
                            if (total == 10000) {
                                killer.sendMessage("<img=5>@blu@You have reached 10000 kills and completed the requirement for Goku");
                            }
                        }

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
                        if (npc.getId() == Wildywyrm.NPC_ID) {
                            Wildywyrm.wyrmAlive = false;
                            World.getPlayers().forEach(p -> PlayerPanel.refreshPanel(p));
                        }

                        if (npc.getId() == SkeletalHorror.NPC_ID) {
                            SkeletalHorror.wyrmAlive = false;
                        }
                        if (npc.getId() == MerkSpawn.NPC_ID) {
                            MerkSpawn.wyrmAlive = false;
                        }
                        if (npc.getId() == 6203 || npc.getId() == 6260 || npc.getId() == 6247 || npc.getId() == 6222) { // done
                            StarterTasks.doProgress(killer, StarterTaskData.KILL_20_GWD_BOSSES);
                        }
                        if (npc.getId() == 1023) { // done
                            StarterTasks.doProgress(killer, StarterTaskData.KILL_100_STARTER);
                        }
                        if (npc.getId() == 4972) { // done
                            StarterTasks.doProgress(killer, StarterTaskData.KILL_ETERNAL);
                        }

                        if (!(npc.getId() == 1)) {
                            StarterTasks.doProgress(killer, StarterTaskData.REACH_1000_TOTAL);
                        }
                        /** PARSE DROPS **/
                        if (npc.getId() == 3830) {
                            SkeletalHorror.handleDrop(npc);
                        }


                        /** PARSE DROPS **/

                        if (npc.getId() == 8013) {// resets the vote count to 0 on votizo
                            VoteBossDrop.handleDrop(npc);
                            World.sendMessage("<shad=f9f6f6>Vote boss has been slain...");

                        }

                        if (npc.getId() == 9908) {
                            WorldBosses.handleDrop(npc);
                        }
                        if (npc.getId() == 9904) {
                            WorldBosses3.handleDrop(npc);
                        }
                        if (npc.getId() == 9906) {
                            WorldBosses2.handleDrop(npc);
                        }
                        if (npc.getId() == 9907) {
                            WorldBosses4.handleDrop(npc);
                        }
                        if (npc.getId() == 8010) {
                            Groudon.handleDrop(npc);
                        }
                        if (npc.getId() == 3112) {
                            Ezkel.handleDrop(npc);
                        }
                        if (npc.getId() == 12239) {
                            SupremeNex.handleDrop(npc);
                        }

                        if (npc.getId() == MerkSpawn.NPC_ID) {
                            MerkSpawn.handleDrop(npc);
                        }
                        if (npc.getId() == 7553) {
                            TheGeneral.giveLoot(killer, npc);
                        }
                        if (npc.getId() == Exoden.MINION_NPCID) {
                            Exoden.minions_dead = true;
                            Exoden.minions_spawned = false;
                        }
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

                        if(ItemEffect.hasTripleKills(killer)) {
                            killCount *= 3;
                        }

                        if(ItemEffect.hasDoubleKills(killer)) {
                            killCount*=2;
                        }

                        killer.getPointsHandler().incrementNPCKILLCount(killCount);

                        if (npc instanceof GlobalBoss) {
                            GlobalBossHandler.onDeath((GlobalBoss) npc);
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

                        /** BOSS EVENT **/
                        new BossEventHandler().death(killer, npc, npc.getDefinition().getName());
                        new InstanceManager(killer).death(killer, npc, npc.getDefinition().getName());
                        new DailyTaskHandler(killer).death(npc.getDefinition().getName());

                        /** SLAYER **/
                        killer.getSlayer().killedNpc(npc);
                        npc.getCombatBuilder().getDamageMap().clear();
                    }
                    stop();
                    break;
            }
            ticks--;
        } catch (Exception e) {
            e.printStackTrace();
            stop();
        }
    }

    @Override
    public void stop() {

        setEventRunning(false);

        npc.setDying(false);

        // respawn
        if (npc.getDefinition().getRespawnTime() > 0 && npc.getLocation() != Location.GRAVEYARD && npc.getLocation() != Location.KEEPERS_OF_LIGHT_GAME
                && npc.getLocation() != Location.DUNGEONEERING && npc.getLocation() != Location.CUSTOM_RAIDS && !npc.isEventBoss()) {
            if (npc.respawn)
                TaskManager.submit(new NPCRespawnTask(npc, npc.getDefinition().getRespawnTime(), killer));
        }

        if (npc.isEventBoss()) {
            EventBossDropHandler.death(killer, npc);
        }

        World.deregister(npc);

        if (npc.getId() == 1158 || npc.getId() == 1160) {
            KalphiteQueen.death(npc.getId(), npc.getPosition());
        }

        boolean tripleKills = ItemEffect.hasTripleKills(killer);
        boolean doubleKills = ItemEffect.hasDoubleKills(killer);
        boolean hasPet = killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302;
        int assign = 0;

        if(npc.getId() == 1614) {
            assign = 0;
            if(tripleKills)
                assign += 2;
            else if(doubleKills)
                assign += 1;
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementNPCKILLCount(assign);
        }

        if(npc.getId() == 603) {
            assign = 0;
            if(tripleKills)
                assign += 2;
            else if(doubleKills)
                assign += 1;
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementNPCKILLCount(assign);
        }

        if(npc.getId() == 12843) {
            assign = 0;
            if(tripleKills)
                assign += 2;
            else if(doubleKills)
                assign += 1;
            if(hasPet)
                assign += 1;
            assign++;
            killer.getPointsHandler().incrementDEMONKILLCount(assign);
        }
        if(npc.getId() == 8014) {
            assign = 0;
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
        } else if (npc.getId() == 8008) {// avatar
            killer.getPointsHandler().incrementAVATARKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 3308
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementANGELKILLCount(2);
        } else if (npc.getId() == 3308) {// angel
            killer.getPointsHandler().incrementANGELKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 3117
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementLUCIENKILLCount(2);
        } else if (npc.getId() == 3117) {// lucien
            killer.getPointsHandler().incrementLUCIENKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 13635
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementKINGKILLCount(2);
        } else if (npc.getId() == 13635) {// king
            killer.getPointsHandler().incrementKINGKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 201
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementHERCULESKILLCount(2);
        } else if (npc.getId() == 201) {// hercules
            killer.getPointsHandler().incrementHERCULESKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 202
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementSATANKILLCount(2);
        } else if (npc.getId() == 202) {// satan
            killer.getPointsHandler().incrementSATANKILLCount(1);

        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 203
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementZEUSKILLCount(2);
        } else if (npc.getId() == 203) {// zeus
            killer.getPointsHandler().incrementZEUSKILLCount(1);
        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 53
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementDRAGONKILLCount(2);
        } else if (npc.getId() == 53) {// dragon
            killer.getPointsHandler().incrementDRAGONKILLCount(1);

        }
        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 8018
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            // well yeah i was just making an example, but im just saying, ur gona have to
            // add so much stuff for each npc if u dont create a system for it
            killer.getPointsHandler().incrementBEASTKILLCount(2);
        } else if (npc.getId() == 8018) {// beast
            killer.getPointsHandler().incrementBEASTKILLCount(1);
        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 9011
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            killer.getPointsHandler().incrementMiniLuciferKillCount(2);
        } else if (npc.getId() == 9011) {// zeus
            killer.getPointsHandler().incrementMiniLuciferKillCount(1);
        }

        if (killer.getSummoning() != null && killer.getSummoning().getFamiliar() != null && npc.getId() == 9012
                && killer.getSummoning().getFamiliar().getSummonNpc().getId() == 302) {
            killer.getPointsHandler().incrementLuciferKillCount(2);
        } else if (npc.getId() == 9012) {// zeus
            killer.getPointsHandler().incrementLuciferKillCount(1);
        }

        //killer.incrementNPCKILLCount(ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.NPC_KILLS ? 2 : 1);


       /* if (npc.getId() == 8011) {
            int[] RewardId1 = new int[]{5022};
            int pickedFood1 = RewardId1[RandomUtility.exclusiveRandom(0, RewardId1.length)];
            killer.getInventory().add(pickedFood1, 5);
            killer.getInventory().add(ItemDefinition.MILL_ID, 1);
            killer.getPointsHandler().incrementEventPoints(2);
        }*/

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

        if (Nex.nexMob(npc.getId())) {
            Nex.death(npc.getId());
        }

        //PlayerPanel.refreshPanel(killer);
    }
}
