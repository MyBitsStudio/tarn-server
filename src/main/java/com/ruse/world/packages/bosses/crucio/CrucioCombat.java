package com.ruse.world.packages.bosses.crucio;

import com.ruse.model.Animation;
import com.ruse.model.Locations;
import com.ruse.model.Projectile;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategies;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;

public class CrucioCombat implements CombatStrategy {

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return victim.isPlayer() && victim.asPlayer().getInstance() != null;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        NPC npc = (NPC) entity;
        int firstAttack = Misc.random(12);
        int secondAttackChance = Misc.random(1);
        int secondAttack = Misc.random(6);
        int thirdAttackChance = Misc.random(1);
        int thirdAttack = Misc.random(3);

        if (Locations.goodDistance(npc.getPosition().copy(), victim.getPosition().copy(), 2)) {
            if(firstAttack >= 9){
                npc.performAnimation(new Animation(6));
                npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 1, 1, CombatType.MAGIC, false));
            } else {
                npc.performAnimation(new Animation(1));
                npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 2, 1, CombatType.MAGIC, true));
            }
        } else {
            npc.performAnimation(new Animation(6));
            new Projectile(entity, victim, 360, 44, 3, 43, 31, 0).sendProjectile();
            npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 2, 1, CombatType.MAGIC, true));
        }

        if(secondAttackChance == 0){
            if(secondAttack >= 5){
                npc.performAnimation(new Animation(6));
                new Projectile(entity, victim, 360, 44, 3, 43, 31, 0).sendProjectile();
                npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 2, 1, CombatType.MAGIC, false));
            } else {
                npc.performAnimation(new Animation(9));
                new Projectile(entity, victim, 360, 44, 3, 43, 31, 0).sendProjectile();
                npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 2, 1, CombatType.MAGIC, true));
            }
        }

        if(thirdAttackChance == 0){
            if(thirdAttack >= 2){
                npc.performAnimation(new Animation(12));
                new Projectile(entity, victim, 360, 44, 3, 43, 31, 0).sendProjectile();
                npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 2, 1, CombatType.MAGIC, false));
            } else {
                npc.performAnimation(new Animation(6));
                new Projectile(entity, victim, 360, 44, 3, 43, 31, 0).sendProjectile();
                npc.getCombatBuilder().setContainer(new CombatContainer(npc, victim, 3, 1, CombatType.MAGIC, true));
            }
        }
        return true;
    }

    @Override
    public int attackDelay(Character entity) {
        return 5;
    }

    @Override
    public int attackDistance(Character entity) {
        return 12;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MAGIC;
    }
}
