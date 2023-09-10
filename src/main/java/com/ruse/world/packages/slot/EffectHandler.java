package com.ruse.world.packages.slot;

import com.ruse.engine.GameEngine;
import com.ruse.engine.task.TaskManager;
import com.ruse.model.*;
import com.ruse.model.container.impl.Equipment;
import com.ruse.util.Misc;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.max.MagicMax;
import com.ruse.world.packages.combat.max.MeleeMax;
import com.ruse.world.packages.combat.max.RangeMax;
import com.ruse.world.packages.slot.effects.FireWall;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.ruse.world.packages.combat.sets.SetPerk.AOE_3;

public class EffectHandler {

    public static void handlePlayerAttack(@NotNull Player p, Character victim){
        if (p.getEquipment().hasAoE()) {
            handleAoE(p, victim,
                    p.getEquipment().getSlotBonuses()[Equipment.WEAPON_SLOT].getBonus() * 3);
        }
        if(p.getEquipment().hasFirewall()){
            TaskManager.submit(new FireWall(p, p.getPosition().getX(), p.getPosition().getY(), 5));
        }
        if(p.getEquipment().hasDoubleShot()){
            long calc = Misc.inclusiveRandom(100, 1000 * 5);
            victim.dealDamage(new Hit(calc, Hitmask.RED, CombatIcon.MAGIC));
            victim.getCombatBuilder().setLastAttacker(p);
            victim.getCombatBuilder().addDamage(p, calc);
            victim.getCombatBuilder().attack(p);
        }
        if(p.getEquipment().hasTripleShot()){
            long calc = Misc.inclusiveRandom(200, 1500 * 5);
            victim.getCombatBuilder().setLastAttacker(p);
            victim.dealDamage(new Hit(calc, Hitmask.RED, CombatIcon.MAGIC));
            victim.getCombatBuilder().addDamage(p, calc);
            victim.dealDamage(new Hit(calc, Hitmask.RED, CombatIcon.MAGIC));
            victim.getCombatBuilder().addDamage(p, calc);
            victim.getCombatBuilder().attack(p);
        }

        if(p.getEquipment().getBonus() != null){
            if(Objects.equals(p.getEquipment().getBonus().perk(), AOE_3)){
                handleAoE(p, victim,
                        6);
            }
        }

        if(p.getSummoning().getFamiliar() != null){
            handlePets(p, victim.toNpc());
        }

    }

    private static void handlePets(@NotNull Player player, NPC victim){
        if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.HEIMDALL_PET.getNpcId()) {
           handleAoE(player, victim,
                    6);
        }
    }

    private static void handleAoE(Character attacker, Character victim,
                                  int radius) {

        // if no radius, loc isn't multi, stops.
        if (radius == 0 || !Locations.Location.inMulti(victim)) {
            return;
        }

        // We passed the checks, so now we do multiple target stuff.

        for (NPC next : ((Player) attacker).getLocalNpcs()) {
            if (next == null) {
                continue;
            }

            if (next.isNpc()) {
                if (!next.getDefinition().isAttackable() || next.isSummoningNpc()) {
                    continue;
                }
            } else {
                continue;
            }

            if (next.getPosition().isWithinDistance(victim.getPosition(), radius) && !next.equals(attacker)
                    && !next.equals(victim) && next.getConstitution() > 0) {
                if (next.isNpc() && next.getConstitution() <= 0 && next.isDying()) {
                    continue;
                }
                if (!RegionClipping.canProjectileAttack(attacker, next)) {
                    continue;
                }

                if(!Locations.Location.inMulti(attacker)) return;

                long maxhit = switch (((Player) attacker).getLastCombatType()) {
                    case MELEE -> MeleeMax.newMelee(attacker, victim) / 10;
                    case RANGED -> RangeMax.newRange(attacker, victim) / 10;
                    case MAGIC -> MagicMax.newMagic(attacker, victim) / 10;
                    default -> 10000;
                };

                long calc = Misc.inclusiveRandom(500, maxhit);
                next.dealDamage(new Hit(calc, Hitmask.RED, CombatIcon.MAGIC));
                next.setAggressive(true);
                next.getCombatBuilder().setLastAttacker(attacker);
                next.getCombatBuilder().addDamage(attacker, calc);
                next.getCombatBuilder().attack(attacker);
            }
        }

    }
}
