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
        FIXED_DRY_STREAK_MILESTONE.put(9838, 10); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9845, 10); //scorp
        FIXED_DRY_STREAK_MILESTONE.put(9910, 10); //ranger
        FIXED_DRY_STREAK_MILESTONE.put(9807, 10); //paladin
        FIXED_DRY_STREAK_MILESTONE.put(6692, 10); //wyvern
        FIXED_DRY_STREAK_MILESTONE.put(9028, 30); //mystic
        FIXED_DRY_STREAK_MILESTONE.put(9029, 30); //nightmare
        FIXED_DRY_STREAK_MILESTONE.put(9030, 30); //patience
        FIXED_DRY_STREAK_MILESTONE.put(8014, 50); //zinrux
        FIXED_DRY_STREAK_MILESTONE.put(8003, 60); //aberrant
        FIXED_DRY_STREAK_MILESTONE.put(202, 70); //hound
        FIXED_DRY_STREAK_MILESTONE.put(811, 80); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9815, 90); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9817, 100); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9920, 110); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3831, 120); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9025, 130); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9026, 140); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9836, 150); //hound
        FIXED_DRY_STREAK_MILESTONE.put(92, 160); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3313, 170); //hound
        FIXED_DRY_STREAK_MILESTONE.put(8008, 180); //hound
        FIXED_DRY_STREAK_MILESTONE.put(1906, 190); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9915, 200); //hound
        FIXED_DRY_STREAK_MILESTONE.put(2342, 210); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9024, 220); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9916, 230); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9918, 240); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9919, 250); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9914, 300); //hound

        //bosses
        FIXED_DRY_STREAK_MILESTONE.put(9017, 500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9839, 1000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9806, 1500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9816, 1750); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9903, 2000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(8002, 2250); //hound
        FIXED_DRY_STREAK_MILESTONE.put(1746, 2500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3010, 2750); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3013, 3000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3014, 3250); //hound
        FIXED_DRY_STREAK_MILESTONE.put(8010, 3500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3016, 3750); //hound
        FIXED_DRY_STREAK_MILESTONE.put(4972, 4000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9012, 4250); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3019, 4500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3020, 4750); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3021, 5000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3305, 5250); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9818, 5500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9912, 5750); //hound
        FIXED_DRY_STREAK_MILESTONE.put(9913, 6000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3117, 6250); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3115, 6500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(12239, 6750); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3112, 7000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(3011, 7250); //hound
        FIXED_DRY_STREAK_MILESTONE.put(252, 7500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(449, 7750); //hound
        FIXED_DRY_STREAK_MILESTONE.put(452, 8000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(187, 9000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(188, 10000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(1311, 12500); //hound
        FIXED_DRY_STREAK_MILESTONE.put(1313, 15000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(1318, 20000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(595, 25000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(440, 25000); //hound
        FIXED_DRY_STREAK_MILESTONE.put(438, 25000); //hound

        FIXED_DRY_STREAK_MILESTONE.put(591, 100);
        FIXED_DRY_STREAK_MILESTONE.put(593, 100);
        FIXED_DRY_STREAK_MILESTONE.put(1880, 2000);
        FIXED_DRY_STREAK_MILESTONE.put(1120, 200);
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
