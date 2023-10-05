package com.ruse.world.packages.slot;

import com.ruse.model.*;
import com.ruse.model.container.impl.Equipment;
import com.ruse.util.Misc;
import com.ruse.world.World;
import com.ruse.world.clip.region.RegionClipping;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.max.MagicMax;
import com.ruse.world.packages.combat.max.MeleeMax;
import com.ruse.world.packages.combat.max.RangeMax;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.ruse.world.packages.combat.sets.SetPerk.AOE_3;
import static com.ruse.world.packages.combat.sets.SetPerk.FIREWALL;

public class EffectHandler {

    public static void handlePlayerAttack(@NotNull Player p, Character victim){
        if (p.getEquipment().hasAoE()) {
            handleAoE(p, victim,
                    p.getEquipment().getSlotBonuses()[Equipment.WEAPON_SLOT].getBonus() * 3);
        }
        if(p.getEquipment().hasFirewall()){
           handleFirewall(p, victim);
        }
        if(p.getEquipment().hasIcewall()){
            handleIcewall(p, victim);
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

        if(p.getEquipment().hasPosionIvy()){
            p.poisonVictim(victim, CombatType.RANGED);
        }

        if(p.getEquipment().hasRageAll()){
            handleRageAll(p);
        }

        if(p.getEquipment().hasLifeBringer()){
            if(Misc.random(165) == 33){
                p.heal(50);
                p.sendMessage("Your life bringer perk has healed you for 50 health.");
            }
        }

        if(p.getEquipment().getBonus() != null){
            if(Objects.equals(p.getEquipment().getBonus().perk(), AOE_3)){
                handleAoE(p, victim,
                        6);
            }
            if(Objects.equals(p.getEquipment().getBonus().perk(), FIREWALL)){
                handleFirewall(p, victim);
            }
        }

        if(p.getSummoning().getFamiliar() != null){
            handlePets(p, victim.toNpc());
        }

    }

    private static void handleRageAll(@NotNull Player player){
        ObjectArrayList<NPC> npcs = World.getNearbyNPCs(player.getPosition(), 10);
        for(NPC npc : npcs){
            if(npc != null){
                if(!npc.isAggressive() && npc.getDefinition().isAttackable()){
                    npc.setAggressive(true);
                    npc.setAggressiveDistance(10);
                    npc.setForceAggressive(true);
                }
            }
        }
    }

    public static void handlePlayerDefence(NPC attacker, @NotNull Player defender, long damage){
        if(defender.getEquipment().hasLifeStealer()){
            if(Misc.random(250) == 66){
                defender.heal(damage / 10);
                defender.sendMessage("Your life stealer perk has healed you for " + damage / 10 + " health.");
            }
        }

        if(defender.getEquipment().hasBounceBack()){
            if(Misc.random(250) == 121){
                attacker.dealDamage(new Hit(damage / 10, Hitmask.RED, CombatIcon.MAGIC));
                attacker.getCombatBuilder().setLastAttacker(defender);
                attacker.getCombatBuilder().addDamage(defender, damage / 10);
                attacker.getCombatBuilder().attack(defender);
                defender.sendMessage("Your bounce back perk has dealt " + damage / 10 + " damage to your attacker.");
            }
        }
    }

    private static void handlePets(@NotNull Player player, NPC victim){
        if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.HEIMDALL_PET.getNpcId()) {
           handleAoE(player, victim,
                    6);
        }
        if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ODIN_PET.getNpcId()) {
            handleFirewall(player, victim);
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

                next.dealDamage(new Hit(maxhit, Hitmask.RED, CombatIcon.MAGIC));
                next.setAggressive(true);
                next.getCombatBuilder().setLastAttacker(attacker);
                next.getCombatBuilder().addDamage(attacker, maxhit);
                next.getCombatBuilder().attack(attacker);
            }
        }

    }

    private static void handleFirewall(Character attacker, Character victim) {

        // if no radius, loc isn't multi, stops.
        if (!Locations.Location.inMulti(victim)) {
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

            if (next.getPosition().isWithinDistance(victim.getPosition(), 6) && !next.equals(attacker)
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

                next.performGraphic(new Graphic(453));
                next.dealDamage(new Hit(maxhit, Hitmask.RED, CombatIcon.MAGIC));
                next.dealDamage(new Hit(maxhit, Hitmask.RED, CombatIcon.MAGIC));
                next.dealDamage(new Hit(maxhit, Hitmask.RED, CombatIcon.MAGIC));
                next.setAggressive(true);
                next.getCombatBuilder().setLastAttacker(attacker);
                next.getCombatBuilder().addDamage(attacker, maxhit);
                next.getCombatBuilder().addDamage(attacker, maxhit);
                next.getCombatBuilder().addDamage(attacker, maxhit);
                next.getCombatBuilder().attack(attacker);
            }
        }

    }

    private static void handleIcewall(Character attacker, Character victim) {

        // if no radius, loc isn't multi, stops.
        if (!Locations.Location.inMulti(victim)) {
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

            if (next.getPosition().isWithinDistance(victim.getPosition(), 6) && !next.equals(attacker)
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

                next.performGraphic(new Graphic(281));
                next.dealDamage(new Hit(maxhit, Hitmask.RED, CombatIcon.MAGIC));
                next.dealDamage(new Hit(maxhit, Hitmask.RED, CombatIcon.MAGIC));
                next.dealDamage(new Hit(maxhit, Hitmask.RED, CombatIcon.MAGIC));
                next.setAggressive(true);
                next.getCombatBuilder().setLastAttacker(attacker);
                next.getCombatBuilder().addDamage(attacker, maxhit);
                next.getCombatBuilder().addDamage(attacker, maxhit);
                next.getCombatBuilder().addDamage(attacker, maxhit);
                next.getCombatBuilder().attack(attacker);
            }
        }

    }
}
