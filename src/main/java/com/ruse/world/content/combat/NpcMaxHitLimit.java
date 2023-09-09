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
            case 587, 8013, 9904, 8010, 3308, 9005 -> maxLimit = 550000;
            default -> {
                return damage;
            }
        }
        if(player.getSummoning().getFamiliar() != null){
            if(player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.RAICHU_PET.getNpcId()){
                maxLimit = (long) (maxLimit * 1.15);
            }
        }
        return Math.min(maxLimit, damage);
    }
}
