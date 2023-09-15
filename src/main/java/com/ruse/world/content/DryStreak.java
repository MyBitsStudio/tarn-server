package com.ruse.world.content;

import com.ruse.model.definitions.NpcDefinition;
import com.ruse.world.entity.impl.player.Player;

import java.util.HashMap;

public class DryStreak {

    private final Player player;
    /**
     * holds the players dry streaks
     *
     * Key = npc id
     * value = dry streak
     */
    public HashMap<Integer, Integer> dryStreakMap = new HashMap<>();

    /**
     * key = npc id
     * value = dry streak needed for drop
     */
    public static final HashMap<Integer, Integer> FIXED_DRY_STREAK_MILESTONE = new HashMap<>();

    static {
        FIXED_DRY_STREAK_MILESTONE.put(9837, 30);
        FIXED_DRY_STREAK_MILESTONE.put(9027, 30);
        FIXED_DRY_STREAK_MILESTONE.put(9835, 30);
        FIXED_DRY_STREAK_MILESTONE.put(9911, 30);
        FIXED_DRY_STREAK_MILESTONE.put(9922, 30);
        FIXED_DRY_STREAK_MILESTONE.put(8014, 30);
        FIXED_DRY_STREAK_MILESTONE.put(8003, 30);
        FIXED_DRY_STREAK_MILESTONE.put(811, 30);
        FIXED_DRY_STREAK_MILESTONE.put(9817, 30);
        FIXED_DRY_STREAK_MILESTONE.put(9836, 30);
        FIXED_DRY_STREAK_MILESTONE.put(92, 30);
        FIXED_DRY_STREAK_MILESTONE.put(3313, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1906, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1742, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1743, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1744, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1745, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1738, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1739, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1740, 30);
        FIXED_DRY_STREAK_MILESTONE.put(1741, 30);

        FIXED_DRY_STREAK_MILESTONE.put(9025, 50);
        FIXED_DRY_STREAK_MILESTONE.put(9026, 50);
        FIXED_DRY_STREAK_MILESTONE.put(8008, 50);
        FIXED_DRY_STREAK_MILESTONE.put(2342, 50);
        FIXED_DRY_STREAK_MILESTONE.put(9839, 50);
        FIXED_DRY_STREAK_MILESTONE.put(9806, 50);
        FIXED_DRY_STREAK_MILESTONE.put(1746, 50);
        FIXED_DRY_STREAK_MILESTONE.put(4972, 50);
        FIXED_DRY_STREAK_MILESTONE.put(3021, 50);
        FIXED_DRY_STREAK_MILESTONE.put(3305, 50);
        FIXED_DRY_STREAK_MILESTONE.put(125, 50);

        FIXED_DRY_STREAK_MILESTONE.put(587, 100);
        FIXED_DRY_STREAK_MILESTONE.put(8013, 100);
        FIXED_DRY_STREAK_MILESTONE.put(9904, 100);
        FIXED_DRY_STREAK_MILESTONE.put(8010, 100);
        FIXED_DRY_STREAK_MILESTONE.put(3308, 100);
        FIXED_DRY_STREAK_MILESTONE.put(9005, 100);

        FIXED_DRY_STREAK_MILESTONE.put(9915, 250);
        FIXED_DRY_STREAK_MILESTONE.put(9024, 350);
        FIXED_DRY_STREAK_MILESTONE.put(9919, 450);
        FIXED_DRY_STREAK_MILESTONE.put(8002, 550);
        FIXED_DRY_STREAK_MILESTONE.put(8000, 650);
        FIXED_DRY_STREAK_MILESTONE.put(3020, 750);
        FIXED_DRY_STREAK_MILESTONE.put(9913, 850);
        FIXED_DRY_STREAK_MILESTONE.put(1311, 950);
        FIXED_DRY_STREAK_MILESTONE.put(1313, 950);
        FIXED_DRY_STREAK_MILESTONE.put(9914, 1250);
        FIXED_DRY_STREAK_MILESTONE.put(185, 1250);
        FIXED_DRY_STREAK_MILESTONE.put(188, 1250);
        FIXED_DRY_STREAK_MILESTONE.put(3117, 1250);

        FIXED_DRY_STREAK_MILESTONE.put(3013, 100);
        FIXED_DRY_STREAK_MILESTONE.put(3831, 150);
        FIXED_DRY_STREAK_MILESTONE.put(9017, 200);
        FIXED_DRY_STREAK_MILESTONE.put(3016, 250);
        FIXED_DRY_STREAK_MILESTONE.put(12239, 300);
    }

    public DryStreak(Player player) {
        this.player = player;
    }

    public Integer getDryStreak(int npcId) {
        return dryStreakMap.computeIfAbsent(npcId, x -> 0);
    }

    public boolean hasHitDryStreak(int npcId) {
        int dryStreak = getDryStreak(npcId);
        Integer mustHitStreak = getDryStreakMilestoneByNpc(npcId);
        if(mustHitStreak != null) {
            return dryStreak >= mustHitStreak;
        }
        return false;
    }

    public void sendAlert(int npcId) {
        if(FIXED_DRY_STREAK_MILESTONE.containsKey(npcId)) {
            int milestone = FIXED_DRY_STREAK_MILESTONE.get(npcId);
            int myDryStreak = getDryStreak(npcId);
            int difference = milestone - myDryStreak;
            if(player.getWalkableInterfaceId() != 60_000) {
                player.getPacketSender().sendWalkableInterface(60_000, true);
            }
            player.getPacketSender().sendString(60_004, String.valueOf(difference));

            if(milestone > myDryStreak) {
                if((difference % 20 == 0 && player.getPSettings().getBooleanValue("drop-message-personal"))) {
                    player.getPacketSender().sendMessage("@red@You will receive a drop from " + NpcDefinition.forId(npcId).getName() + " in " + difference + " kills");
                }
            }
        }
    }

    public Integer getDryStreakMilestoneByNpc(int npcId) {
        return FIXED_DRY_STREAK_MILESTONE.get(npcId);
    }

    public HashMap<Integer, Integer> getDryStreakMap() {
        return dryStreakMap;
    }

    public void setDryStreakMap(HashMap<Integer, Integer> dryStreakMap) {
        this.dryStreakMap = dryStreakMap;
    }
}
