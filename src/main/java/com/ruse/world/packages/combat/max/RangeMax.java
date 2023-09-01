package com.ruse.world.packages.combat.max;

import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.container.impl.Equipment;
import com.ruse.util.Misc;
import com.ruse.world.content.BonusManager;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.NpcMaxHitLimit;
import com.ruse.world.content.combat.effect.EquipmentBonus;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class RangeMax {

    public static long newRange(Character entity, Character victim){
        long maxHit = 0L;

        if (entity.isNpc()) {
            NPC npc = entity.toNpc();
            maxHit = npc.getMaxHit();

            if(victim.isPlayer()){
                Player player = victim.asPlayer();

                double defence = (player.getBonusManager().getDefenceBonus()[4] / 1_000);

                maxHit -= defence;

                if(maxHit <= 0){
                    maxHit = 1;
                }

                double absorb = player.getBonusManager().getExtraBonus()[BonusManager.ABSORB_RANGED];

                if(absorb > 0){
                    double percent = 1 - ( 1 - (absorb / 1000.0));
                    maxHit -= (long) (maxHit * percent);
                }
            }

            if(npc.getDefinition().isBoss()){
                maxHit *= 1.25;
            }

        } else if (entity.isPlayer()) {
            Player player = entity.asPlayer();

            double rangedStrength = player.getBonusManager().getOtherBonus()[1];

            double prayerMod = 1.0;
            if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE) || CurseHandler.isActivated(player, CurseHandler.LEECH_RANGED)) {
                prayerMod = 1.05;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
                prayerMod = 1.10;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
                prayerMod = 1.15;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                prayerMod = 1.23;
            } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
                prayerMod = 1.25;
            } else if (PrayerHandler.isActivated(player,PrayerHandler.SOUL_LEECH)) {
                prayerMod = 1.15 + (player.getLeechedBonuses()[2] * 0.01);
            }


            double effectiveRangeDamage = (120 * prayerMod) * (rangedStrength / 1_000);


            double baseDamage = 1.3 + (effectiveRangeDamage / 10) + (rangedStrength / 70) + effectiveRangeDamage * (rangedStrength / 630);


            double specialBonus = 1;
            // Special attacks!
            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.RANGED) {
                specialBonus = player.getCombatSpecial().getStrengthBonus();
            }

            maxHit = (long) (baseDamage * specialBonus);

            if (player.getEquipment().contains(23048)) { //Tier 5 Aura
                maxHit *= 1.05D;
            }
            if (player.getEquipment().contains(23049)) { //Tier 6 Aura
                maxHit *= 1.10D;
            }
            if (player.getEquipment().contains(23212)) { //Tier 7 Aura
                maxHit *= 1.15D;
            }
            if (player.getEquipment().contains(22111)) { //Tier 6 Aura
                maxHit *= 1.10D;
            }
            if (player.getDmgPotionTimer() > 0) {
                maxHit *= 1.5;
            }
            if(ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.DAMAGE || ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.ALL_PERKS) {
                maxHit *= 1.5;
            }
            if (player.getInventory().contains(4442)) {
                maxHit *= 1.5;
            }


            switch(player.getEquipment().getDamageBonus()){
                case 1 -> maxHit *= 1.07;
                case 2 -> maxHit *= 1.16;
                case 3 -> maxHit *= 1.23;
            }

            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 1906) {
                maxHit *= 1.1D;
            }
            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 9833) {
                maxHit *= 1.2D;
            }
            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 1801) {// admin pet
                maxHit *= 1.15D;
            }

            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 9013) {// admin pet
                maxHit *= 1.25D;
            }
            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 9016) {// admin pet
                maxHit *= 1.25D;
            }

            if (victim.isNpc()) {
                if(player.getSlayer().getTask() != null) {
                    if (((NPC) victim).getId() == player.getSlayer().getTask().getId()) {
                        if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071
                                || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069) {
                            maxHit *= 1.05;
                        } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
                            maxHit *= 1.07;
                        } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
                            maxHit *= 1.10;
                        } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
                            maxHit *= 1.15;
                        } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
                            maxHit *= 1.25;
                        }
                    }

                    if (((NPC) victim).getId() == player.getSlayer().getTask().getId()
                            && victim.getConstitution() >= ((NPC) victim).getDefaultConstitution()) {
                        int percent = -1;
                        if (player.getEquipment().contains(22000)) {
                            percent = 2;
                        } else if (player.getEquipment().contains(22001)) {
                            percent = 4;
                        } else if (player.getEquipment().contains(22002)) {
                            percent = 6;
                        } else if (player.getEquipment().contains(22003)) {
                            percent = 8;
                        } else if (player.getEquipment().contains(22004)) {
                            percent = 10;
                        }
                        if (Misc.random(99) + 1 <= percent) {
                            maxHit = victim.getConstitution();
                        }
                    }
                }
            }

            if (player.getVariables().getBooleanValue("double-damage")) {
                maxHit *= 2;
            }

            maxHit *= multiplyDamage(player);

            if (player.getMinutesVotingDMG() > 0) {
                maxHit *= 2;
            }

            if(player.getEquipment().contains(15587))
                maxHit *= 1.5;

            maxHit *= 10;

            maxHit *= player.getEquipment().getBonus() == null ? 1 : player.getEquipment().getBonus().rangeDamage();
        }


        if (victim != null && victim.isNpc() && (entity.isPlayer() && !entity.asPlayer().getRank().isDeveloper())) {
            maxHit = (long) NpcMaxHitLimit.limit((NPC) victim, maxHit, entity.asPlayer());
        }

        return maxHit;
    }

    public static double multiplyDamage(@NotNull Player player){
        double multiply = 1.0;
        if(player.getSummoning().getFamiliar() != null) {
            if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.RANGED_PET.npcId) {
                multiply += 0.10;
            }
            if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SHADOW.npcId) {
                multiply += 0.5;
            }
        }
        return multiply;
    }

}
