package com.ruse.world.content.combat;

import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;

public class NpcMaxHitLimit {

    public static double limit(NPC npc, double damage, Player player) {
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
            case 3308:
                maxLimit = 50000;
                break;
            case 587:
                maxLimit = 5000000;
                break;
            case 586:
                maxLimit = 200000;
                break;
            case 4540:
                maxLimit = 450000;
                break;
            default:
                return damage;
        }
        return Math.min(maxLimit, damage);
    }
}
