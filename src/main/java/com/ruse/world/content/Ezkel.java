package com.ruse.world.content;

import com.ruse.model.Position;
import com.ruse.model.definitions.NPCDrops;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.content.achievement.Achievements;
import com.ruse.world.content.combat.CombatBuilder;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.content.dailytasks_new.DailyTask;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Ezkel {

    public static NPC currentBoss = null;

    // Negative because if you make it zero it will increase
    // before the world is created and it wont spawn on server boot for 3 hrs
    public static int tick = -20;

    public static Position SPAWN_POINT = new Position(2143, 5016,56);

    public static int[] BOSS_IDS = {
            3112
    };

    public static void handleDrop(NPC npc) {
        currentBoss = null;
        if (npc.getCombatBuilder().getDamageMap().size() == 0) {
            return;
        }
        Map<Player, Long> killers = new HashMap<>();

        for (Map.Entry<Player, CombatBuilder.CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {
            if (entry == null) {
                continue;
            }

            long timeout = entry.getValue().getStopwatch().elapsed();
            if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
                continue;
            }

            Player player = entry.getKey();
            if (player.getConstitution() <= 0 || !player.isRegistered()) {
                continue;
            }

            killers.put(player, entry.getValue().getDamage());
        }

        npc.getCombatBuilder().getDamageMap().clear();

        List<Map.Entry<Player, Long>> result = sortEntries(killers);
        for (Iterator<Map.Entry<Player, Long>> iterator = result.iterator(); iterator.hasNext(); ) {
            Map.Entry<Player, Long> entry = iterator.next();
            Player killer = entry.getKey();

            Achievements.doProgress(killer, Achievements.Achievement.KILL_45_GLOBAL_BOSSES);
            DailyTask.GLOBAL_BOSSES.tryProgress(killer);
            KillsTracker.submitById(killer, npc.getId(), true, npc.getDefinition().isBoss());
            KillsTracker.submitById(killer, npc.getId(), false, npc.getDefinition().isBoss());
            NPCDrops.handleDrops(killer, npc);
            iterator.remove();
        }
    }
    static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntries(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return sortedEntries;

    }
    public static String timeLeft() {
        int ticks = 20 - (tick % 20);
        ticks /= 100;
        ticks *= 60;

        long ms = ticks ;
       String  m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
                TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
                TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));

        if (tick < 0) {
            m = "Soon";
        }
        return m;
    }

    public static void sequence() {
        tick++;

        // Spawn every 3 hours
        if (tick % 20 == 0) {

            // Only if its dead
            if(currentBoss == null || currentBoss.isDying() || !currentBoss.isRegistered()) {

                // Make extra sure the boss isnt spawned
                // not really needed
                for (int i = 0; i < BOSS_IDS.length; i++) {
                    if (World.npcIsRegistered(BOSS_IDS[i])) {
                        return;
                    }
                }
                int boss = BOSS_IDS[Misc.random(BOSS_IDS.length - 1)];
                NPC npc = new NPC(boss, SPAWN_POINT);
                currentBoss = npc;
                World.register(npc);

                String message = "Whoever dares to challenge Nine Tails, face him now at ::ninetails";

                if (boss == 3112)
                message = "The Final Boss Veigar has spawned at ::veigar";
                //JavaCord.sendMessage("\uD83E\uDD16│activity", "**[World Boss] " + message + " **");
                for (Player players : World.getPlayers()) {
                    if (players == null) {
                        continue;
                    }
                    //players.getPacketSender().sendBroadCastMessage(message, 100);
                }
               // World.sendBroadcastMessage(message);
               // GameSettings.broadcastMessage = message;
               // GameSettings.broadcastTime = 100;
               // World.sendMessage(message);

            }
        }
    }
}