package com.ruse.world.packages.slot;

import com.ruse.model.CombatIcon;
import com.ruse.model.Hit;
import com.ruse.model.Hitmask;
import com.ruse.model.Locations;
import com.ruse.model.container.impl.Equipment;
import com.ruse.util.Misc;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.max.MagicMax;
import com.ruse.world.packages.combat.max.MeleeMax;
import com.ruse.world.packages.combat.max.RangeMax;

import java.util.Objects;

import static com.ruse.world.packages.combat.sets.SetPerk.AOE_3;

public class EffectHandler {

    public static void handlePlayerAttack(Player p, Character victim){
        if (p.getEquipment().hasAoE()) {
            handleAoE(p, victim,
                    p.getEquipment().getSlotBonuses()[Equipment.WEAPON_SLOT].getBonus() * 3);
        }
        if(p.getEquipment().hasDoubleShot()){
            long calc = Misc.inclusiveRandom(100, 1000 * 5);
            victim.dealDamage(new Hit(calc, Hitmask.RED, CombatIcon.MAGIC));
            victim.getCombatBuilder().addDamage(p, calc);
            victim.getCombatBuilder().attack(p);
        }
        if(p.getEquipment().hasTripleShot()){
            long calc = Misc.inclusiveRandom(200, 1500 * 5);
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

    }

    private static void handleAoE(Character attacker, Character victim,
                                  int radius) {

        // if no radius, loc isn't multi, stops.
        if (radius == 0 || !Locations.Location.inMulti(victim)) {
             System.out.println("Radius 0");
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
                long maxhit = switch (((Player) attacker).getLastCombatType()) {
                    case MELEE -> MeleeMax.newMelee(attacker, victim) / 50;
                    case RANGED -> RangeMax.newRange(attacker, victim) / 50;
                    case MAGIC -> MagicMax.newMagic(attacker, victim) / 50;
                    default -> 10000;
                };

                long calc = Misc.inclusiveRandom(500, maxhit * 5);
                next.dealDamage(new Hit(calc, Hitmask.RED, CombatIcon.MAGIC));
                next.getCombatBuilder().addDamage(attacker, calc);
                next.getCombatBuilder().attack(attacker);
            }
        }

    }
}
