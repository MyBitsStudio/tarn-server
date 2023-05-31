package com.ruse.world.packages.donation.boss;

import com.ruse.engine.task.Task;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.Animation;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.player.Player;

public class MinionCombat implements CombatStrategy {
    @Override
    public boolean canAttack(Character entity, Character victim) {
        return victim.isPlayer();
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        DonationMinion npc = (DonationMinion) entity;
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

        npc.setChargingAttack(true);
        npc.performAnimation(new Animation(npc.getDefinition().getAttackAnimation()));
        npc.getCombatBuilder().setContainer(new CombatContainer(npc, player, 2, 0, CombatType.MELEE, true));

        TaskManager.submit(new Task(1, npc, false) {
            int tick = 0;

            @Override
            public void execute() {
                if (tick == 2) {
                    npc.setChargingAttack(false);
                    stop();
                }
                tick++;
            }
        });
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return 4;
    }

    @Override
    public int attackDistance(Character entity) {
        return 5;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MELEE;
    }
}
