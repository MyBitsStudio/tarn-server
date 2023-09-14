//package com.ruse.world.content;
//
//import com.ruse.GameSettings;
//import com.ruse.model.Position;
//import com.ruse.util.Misc;
//import com.ruse.world.World;
//import com.ruse.world.content.achievement.Achievements;
//import com.ruse.world.content.combat.CombatBuilder;
//import com.ruse.world.content.combat.CombatFactory;
//import com.ruse.world.content.dailytasks_new.DailyTask;
//import com.ruse.world.entity.impl.npc.NPC;
//import com.ruse.world.entity.impl.player.Player;
//
//import java.util.*;
//import java.util.concurrent.TimeUnit;
//
//public class WorldBosses2 {
//
//    public static NPC currentBoss = null;
//
//    // Negative because if you make it zero it will increase
//    // before the world is created and it wont spawn on server boot for 3 hrs
//    public static int tick = -20;
//
//    public static Position SPAWN_POINT = new Position(2143, 5016,12);
//
//    public static int[] BOSS_IDS = {
//            9904
//    };
//
//    public static void handleDrop(NPC npc) {
//        currentBoss = null;
//        if (npc.getCombatBuilder().getDamageMap().size() == 0) {
//            return;
//        }
//        Map<Player, Long> killers = new HashMap<>();
//
//        for (Map.Entry<Player, CombatBuilder.CombatDamageCache> entry : npc.getCombatBuilder().getDamageMap().entrySet()) {
//            if (entry == null) {
//                continue;
//            }
//
//            long timeout = entry.getValue().getStopwatch().elapsed();
//            if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
//                continue;
//            }
//
//            Player player = entry.getKey();
//            if (player.getConstitution() <= 0 || !player.isRegistered()) {
//                continue;
//            }
//
//            if(!killers.containsKey(player)) {
//                killers.put(player, entry.getValue().getDamage());
//            }
//        }
//
//        npc.getCombatBuilder().getDamageMap().clear();
//
//        List<Map.Entry<Player, Long>> result = sortEntries(killers);
//        for (Iterator<Map.Entry<Player, Long>> iterator = result.iterator(); iterator.hasNext(); ) {
//            Map.Entry<Player, Long> entry = iterator.next();
//            Player killer = entry.getKey();
//
//            Achievements.doProgress(killer, Achievements.Achievement.KILL_45_GLOBAL_BOSSES);
//            DailyTask.GLOBAL_BOSSES.tryProgress(killer);
//
//            //NPCDrops.handleDrops(killer, npc);
//            iterator.remove();
//        }
//    }
//    static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntries(Map<K, V> map) {
//
//        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());
//
//        Collections.sort(sortedEntries, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));
//
//        return sortedEntries;
//
//    }
//
//    public static String timeLeft() {
//        int ticks = 6000 - (tick % 6000);
//        ticks /= 100;
//        ticks *= 60;
//
//        long ms = ticks ;
//       String  m = String.format("%dh %dm", TimeUnit.SECONDS.toHours(ms),
//                TimeUnit.SECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(ms)),
//                TimeUnit.SECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(ms)));
//
//        if (tick < 0) {
//            m = "Soon";
//        }
//        return m;
//    }
//
//    public static void sequence() {
//        tick++;
//
//        // Spawn every 3 hours
//        if (tick % 6000 == 0) {
//
//            // Only if its dead
//            if(currentBoss == null || currentBoss.isDying() || !currentBoss.isRegistered()) {
//
//                // Make extra sure the boss isnt spawned
//                // not really needed
//                for (int i = 0; i < BOSS_IDS.length; i++) {
//                    if (World.npcIsRegistered(BOSS_IDS[i])) {
//                        return;
//                    }
//                }
//                int boss = BOSS_IDS[Misc.random(BOSS_IDS.length - 1)];
//                NPC npc = new NPC(boss, SPAWN_POINT);
//                currentBoss = npc;
//                World.register(npc);
//
//                String message = "Whoever dares to challenge Nine Tails, face him now at ::ninetails";
//
//                 if (boss == 9904)
//                     message = "The Fearless Nine Tails has just spawned, he can be found at ::ninetails";
//                if (GameSettings.LOCALHOST)
//                    return;
//                 //JavaCord.sendMessage("\uD83E\uDD16│\uD835\uDDEE\uD835\uDDF0\uD835\uDE01\uD835\uDDF6\uD835\uDE03\uD835\uDDF6\uD835\uDE01\uD835\uDE06", "**[World Boss] " + message + " **");
//                for (Player players : World.getPlayers()) {
//                    if (players == null) {
//                        continue;
//                    }
//                    players.getPacketSender().sendBroadCastMessage(message, 100);
//                }
//                World.sendBroadcastMessage(message);
//                GameSettings.broadcastMessage = message;
//                GameSettings.broadcastTime = 100;
//                World.sendMessage(message);
//
//            }
//        }
//    }
//    public static void handleForcedSpawn() {
//
//        //doMotivote.setVoteCount(doMotivote.getVoteCount() - 50);
//        currentBoss = new NPC(9904, new Position(2143, 5016,12));
//
//        World.register(currentBoss);
//        World.sendMessage(
//                "The Fearless Nine Tails has just spawned, he can be found at ::ninetails");
//       // JavaCord.sendMessage("\uD83E\uDD16│\uD835\uDDEE\uD835\uDDF0\uD835\uDE01\uD835\uDDF6\uD835\uDE03\uD835\uDDF6\uD835\uDE01\uD835\uDE06", "**The Fearless Nine Tails has just spawned, he can be found at ::ninetails**");
//    }
//}