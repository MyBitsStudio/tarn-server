package com.ruse.world.content.raids.firefight.npc.one.strategy;

import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.raids.firefight.FireFightRaid;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class FFOneMinionStrategy implements CombatStrategy {

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return victim.isPlayer() && victim.asPlayer().getRaid() != null;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, @NotNull Character victim) {
        NPC npc = (NPC) entity;
        if (victim.getConstitution() <= 0) {
            return true;
        }
        if (npc.isChargingAttack()) {
            return true;
        }
        if (!victim.isPlayer()) {
            return true;
        }
        Player player = (Player) victim;

        if(player.getRaid() != null) {
            FireFightRaid raid = (FireFightRaid) player.getRaid();




        }
        return false;
    }

    @Override
    public int attackDelay(Character entity) {
        return 5;
    }

    @Override
    public int attackDistance(Character entity) {
        return 8;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
