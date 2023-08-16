package com.ruse.world.packages.combat.scripts.npc;

import com.ruse.model.Animation;
import com.ruse.model.Graphic;
import com.ruse.model.Locations;
import com.ruse.model.Projectile;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatContainer;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.packages.combat.CombatConstants;
import org.jetbrains.annotations.NotNull;

public class DefaultMonsterScript implements CombatStrategy {

    public static Animation animationMelee = new Animation(CombatConstants.FORWARD_CHOP_MELEE);
    public static Animation animationMagic = new Animation(CombatConstants.CAST_SPELL_MAGIC);
    public static Animation animationRange = new Animation(1);

    public static Graphic graphicMagic = new Graphic(CombatConstants.WHITE_CRYSTAL_MAGIC);

    @Override
    public boolean canAttack(Character entity, Character victim) {
        return true;
    }

    @Override
    public CombatContainer attack(Character entity, Character victim) {
        return null;
    }

    @Override
    public boolean customContainerAttack(Character entity, Character victim) {
        NPC monster = entity.toNpc();

        if (Locations.goodDistance(monster.getPosition().copy(), victim.getPosition().copy(), 1)){
            monster.performAnimation(animationMelee);
            monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim, 1, 1, CombatType.MELEE, true));
        } else {
            monster.performAnimation(animationMagic);
            monster.performGraphic(graphicMagic);
            new Projectile(entity, victim, CombatConstants.WHITE_CRYSTAL_MAGIC_PROJECTILE, 44, 3, 43, 31, 0).sendProjectile();
            monster.getCombatBuilder().setContainer(new CombatContainer(monster, victim, 1, 1, CombatType.MAGIC, true));
        }

        return true;
    }

    @Override
    public int attackDelay(@NotNull Character entity) {
        return entity.getAttackSpeed();
    }

    @Override
    public int attackDistance(Character entity) {
        return 5;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.MIXED;
    }
}
