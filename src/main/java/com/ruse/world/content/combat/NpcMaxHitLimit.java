package com.ruse.world.content.combat;

import com.ruse.world.entity.impl.npc.NPC;

public class NpcMaxHitLimit {

    public static double limit(NPC npc, double damage, CombatType type) {
        int maxLimit;
        switch (npc.getId()) {
            // case 187:  // goku
            //case 8009: // hulk
            case 1:
                maxLimit = 80000;
                break;
            case 9908:
            case 9904:
            case 9907:
            case 9906:
            case 8013:
                maxLimit = 10000;
                break;
            case 587:
                maxLimit = 1000000;
                break;
            case 586:
                maxLimit = 200000;
                break;
            default:
                return damage;
        }
        return Math.min(maxLimit, damage);
    }
}
