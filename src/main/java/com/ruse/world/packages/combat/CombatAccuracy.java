package com.ruse.world.packages.combat;

import com.ruse.model.Skill;
import com.ruse.util.Misc;
import com.ruse.world.content.BonusManager;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.combat.weapon.FightStyle;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.johnachievementsystem.PerkType;
import org.jetbrains.annotations.NotNull;

import static com.ruse.world.content.combat.CombatType.MAGIC;

public class CombatAccuracy {

    public static boolean roll(Character attacker, Character victim, CombatType type){
        /**
         * PVP Rolls (Not Put In By Default)
         */

        double prayerMod = 1;
        double equipmentBonus;
        double specialBonus = 1;

        if (type == CombatType.DRAGON_FIRE)
            type = MAGIC;

        if (attacker.isPlayer()) {
            Player player = attacker.asPlayer();

            equipmentBonus = type == MAGIC
                    ? player.getBonusManager().getAttackBonus()[BonusManager.ATTACK_MAGIC]
                    : player.getBonusManager().getAttackBonus()[player.getFightType().getBonusType()];

            equipmentBonus /= 7_500;

            if(player.getEquipment().contains(15449)){
                equipmentBonus *= 1.25;
            }

            prayerMod = prayerBonus(player, type);

            if (player.isSpecialActivated()) {
                specialBonus = player.getCombatSpecial().getAccuracyBonus();
            }

        } else {
            NPC npc = attacker.toNpc();

            specialBonus = npc.getDefinition().isBoss() ? 8 : 3;
            equipmentBonus = npc.getCombatLevel() * 2;

        }

        double attackCalc = equipmentBonus + attacker.getBaseAttack(type) + 8;


        attackCalc *= prayerMod;

        if (equipmentBonus < -67) {
            attackCalc = Misc.exclusiveRandom(8) == 0 ? attackCalc : 0;
        }

        attackCalc *= specialBonus;

        equipmentBonus = 1;
        prayerMod = 1;

        double defenceCalc;

        if (victim.isPlayer()) {
            Player player = victim.asPlayer();

            if(attacker.isNpc()){
                NPC npc = attacker.toNpc();

                if(npc.isDying() || npc.getConstitution() <= 0)
                    return false;

                switch(npc.getCombatBuilder().getStrategy().getCombatType()){
                    case MAGIC -> {
                        equipmentBonus = player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC];
                        equipmentBonus /= 10_000;
                        prayerMod = prayerBonus(player, MAGIC);
                    }
                    case MELEE -> {
                        equipmentBonus = player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_STAB];
                        equipmentBonus /= 10_000;
                        prayerMod = prayerBonus(player, CombatType.MELEE);
                    }
                    case RANGED -> {
                        equipmentBonus = player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_RANGE];
                        equipmentBonus /= 10_000;
                        prayerMod = prayerBonus(player, CombatType.RANGED);
                    }
                    case MIXED -> {
                        double bonuss =  player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_STAB] +  player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_RANGE] + player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC];
                        equipmentBonus = bonuss / 3;
                        equipmentBonus /= 10_000;
                        prayerMod = prayerBonus(player, CombatType.MELEE);
                    }
                }

            } else {
                equipmentBonus = 2;
            }

            defenceCalc = Math.floor(equipmentBonus + 90) + 8;
            defenceCalc *= prayerMod;


        } else {
            NPC npc = victim.toNpc();

            if (npc.getDefinition().isBoss()) {
                equipmentBonus = npc.getCombatLevel() * 2;
            } else {
                equipmentBonus = npc.getCombatLevel();
            }

            defenceCalc = Math.floorDiv((int) (equipmentBonus + victim.getBaseDefence(type)), 5) + 8;

        }

        if (equipmentBonus < -67) {
            defenceCalc = Misc.exclusiveRandom(8) == 0 ? defenceCalc : 0;
        }

        if(attacker.isPlayer()){
            Player player = attacker.asPlayer();
            if(AchievementHandler.hasUnlocked(player, PerkType.ACCURACY)){
                attackCalc *= (1 + (AchievementHandler.getPerkLevel(player, PerkType.ACCURACY) * 0.10));
            }
        }

        double A = Math.floor(attackCalc);
        double D = Math.floor(defenceCalc);
        double hitSucceed = A < D ? (A - 1.0) / (2.0 * D) : 1.0 - (D + 1.0) / (2.0 * A);
        hitSucceed = hitSucceed >= 1.0 ? 0.99 : hitSucceed <= 0.0 ? 0.01 : hitSucceed;
        double hitRoll = Misc.RANDOM.nextDouble();
        return hitSucceed >= hitRoll;
    }

    private static double prayerBonus(Player player, @NotNull CombatType type){
        double prayerMod = 1;

        switch(type) {
            case MAGIC -> {
                if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_WILL)) {
                    prayerMod = 1.05;
                }
                if (PrayerHandler.isActivated(player, PrayerHandler.FORTITUDE)) {
                    prayerMod = 1.25;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.FURY_SWIPE)) {
                    prayerMod = 1.25;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_LORE)) {
                    prayerMod = 1.10;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_MIGHT)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                    prayerMod = 1.22;
                } else if (CurseHandler.isActivated(player, CurseHandler.LEECH_MAGIC)) {
                    prayerMod = 1.05 + (player.getLeechedBonuses()[6] * 0.01);
                }
            }
            case MELEE -> {
                if (PrayerHandler.isActivated(player, PrayerHandler.CLARITY_OF_THOUGHT)) {
                    prayerMod = 1.05;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.IMPROVED_REFLEXES)) {
                    prayerMod = 1.10;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.INCREDIBLE_REFLEXES)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
                    prayerMod = 1.20;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                    prayerMod = 1.20;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                    prayerMod = 1.20;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.HUNTERS_EYE)) {
                    prayerMod = 1.20;
                } else if (CurseHandler.isActivated(player, CurseHandler.LEECH_ATTACK)) {
                    prayerMod = 1.05 + (player.getLeechedBonuses()[0] * 0.01);
                } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
                    prayerMod = 1.15 + (player.getLeechedBonuses()[2] * 0.01);
                }
            }
            case RANGED -> {
                if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE)) {
                    prayerMod = 1.05;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
                    prayerMod = 1.10;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
                    prayerMod = 1.15;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                    prayerMod = 1.22;
                } else if (PrayerHandler.isActivated(player, PrayerHandler.HUNTERS_EYE)) {
                    prayerMod = 1.22;
                } else if (CurseHandler.isActivated(player, CurseHandler.LEECH_RANGED)) {
                    prayerMod = 1.05 + (player.getLeechedBonuses()[4] * 0.01);
                }
            }
        }
        return prayerMod;
    }
}
