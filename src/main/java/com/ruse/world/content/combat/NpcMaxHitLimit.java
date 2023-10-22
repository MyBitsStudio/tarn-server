package com.ruse.world.content.combat;

import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;
import org.jetbrains.annotations.NotNull;

public class NpcMaxHitLimit {

    public static long limit(@NotNull NPC npc, long damage, Player player) {
        long maxLimit;
        switch (npc.getId()) {
            case 587, 8013, 9904, 8010, 3308, 9005 -> maxLimit = 55000;
            case 9906 -> maxLimit = 750000000L;
            case 7553, 14378 -> maxLimit = 1_250_000_000L;
            case 6100, 6104 -> maxLimit = 2_100_000_000L;
            case 6105, 5665 -> maxLimit = 1_000_000_00L;
            case 3010 -> maxLimit = 50_000_000_000L;
            default -> {
                return damage;
            }
        }

        return (long) Math.min(maxLimit, damage);
    }
}
