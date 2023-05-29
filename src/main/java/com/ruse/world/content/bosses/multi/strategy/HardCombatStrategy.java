package com.ruse.world.content.bosses.multi.strategy;

import com.ruse.model.Animation;
import com.ruse.model.Locations;
import com.ruse.model.Projectile;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import org.jetbrains.annotations.NotNull;

public class HardCombatStrategy implements CombatStrategy {
    @Override
    public boolean canAttack(Character entity, Character victim) {
        return victim.isPlayer() && victim.asPlayer().getInstance() != null;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, @NotNull Character victim) {
        NPC npc = (NPC) entity;
        if (Locations.goodDistance(npc.getPosition().copy(), victim.getPosition().copy(), 3)
                && Misc.getRandom(5) <= 3) {
            npc.performAnimation(new Animation(1125));
            //freiza.performGraphic(graphics);
            new Projectile(entity, victim, 720, 44, 3, 43, 31, 0).sendProjectile();

            npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 2,
                    1, CombatType.MELEE, Misc.getRandom(10) > 8));
            //	new Projectile(entity, victim, graphics.getId(), 44, 3, 43, 31, 0).sendProjectile();

        } else {
            npc.performAnimation(new Animation(3007));

            new Projectile(entity, victim, 2264, 44, 3, 43, 31, 0).sendProjectile();
            npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 3, 1, CombatType.MAGIC,
                    Misc.getRandom(5) == 0));
        }
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return 5;
    }

    @Override
    public int attackDistance(Character entity) {
        return 10;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }

    @Override
    public boolean ignoreAttackDistance() {
        return true;
    }
}
