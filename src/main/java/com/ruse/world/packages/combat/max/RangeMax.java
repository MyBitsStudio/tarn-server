package com.ruse.world.packages.combat.max;

import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.container.impl.Equipment;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.NpcMaxHitLimit;
import com.ruse.world.content.combat.effect.EquipmentBonus;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public class RangeMax {

    public static long newRange(Character entity, Character victim){
        long maxHit = 0L;

        if (entity.isNpc()) {
            NPC npc = entity.toNpc();
            maxHit = npc.getMaxHit();

            if(victim.isPlayer()){
                Player player = victim.asPlayer();

                long defence = (long) (player.getBonusManager().getDefenceBonus()[4] / 100_000);

                maxHit -= (defence / 11);

                if(maxHit <= 0){
                    maxHit = 1;
                }
                if(npc.getDefinition().isBoss()){
                    maxHit *= 2;
                }
            }
        } else if (entity.isPlayer()) {
            Player player = entity.asPlayer();

            double rangedStrength = player.getBonusManager().getOtherBonus()[1];
            long rangeLevel = Math.max(player.getSkillManager().getCurrentLevel(Skill.RANGED), 120);

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
                prayerMod = 1.15 + +(player.getLeechedBonuses()[2] * 0.01);
                if (Misc.getRandom(100) <= 1) {
                    player.setDoubleDMGTimer(1);
                    // player.getPacketSender().sendMessage("Coup de grace activated");
                }
            }

            double effectiveRangeDamage = 120 * prayerMod * Math.floorDiv((int) rangedStrength, 1_000);

            double baseDamage = 1.3 + effectiveRangeDamage / 10 + rangedStrength / 70 + effectiveRangeDamage * rangedStrength / 630;


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
                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()) {
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

                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()
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

            if (player.getDoubleDMGTimer() > 0) {
                maxHit *= 2;
            }
            if (player.getMinutesVotingDMG() > 0) {
                maxHit *= 2;
            }
            maxHit *= 10;

            maxHit *= player.getEquipment().getBonus() == null ? 1 : player.getEquipment().getBonus().rangeDamage();
        }


        if (victim != null && victim.isNpc() && (entity.isPlayer() && !entity.asPlayer().getRank().isDeveloper())) {
            maxHit = (long) NpcMaxHitLimit.limit((NPC) victim, maxHit, entity.asPlayer());
        }

        return maxHit;
    }

//    public static long ranged(Character entity, Character victim) {
//        double maxHit = 0;
//
//        if (entity.isNpc()) {
//            NPC npc = (NPC) entity;
//            maxHit = (double) npc.getMaxHit() / 10;
//        } else if (entity.isPlayer()) {
//            Player player = (Player) entity;
//
//            double rangedStrength = player.getBonusManager().getOtherBonus()[1];
//            double rangeLevel = player.getSkillManager().getCurrentLevel(Skill.RANGED);
//
//            // Prayers
//            double prayerMod = 1.0;
//            if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE) || CurseHandler.isActivated(player, CurseHandler.LEECH_RANGED)) {
//                prayerMod = 1.05;
//            } else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
//                prayerMod = 1.10;
//            } else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
//                prayerMod = 1.15;
//            } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
//                prayerMod = 1.23;
//            } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
//                prayerMod = 1.25;
//            } else if (PrayerHandler.isActivated(player,PrayerHandler.SOUL_LEECH)) {
//                prayerMod = 1.15 + +(player.getLeechedBonuses()[2] * 0.01);
//                if (Misc.getRandom(100) <= 1) {
//                    player.setDoubleDMGTimer(1);
//                    // player.getPacketSender().sendMessage("Coup de grace activated");
//                }
//            }
//
//            double otherBonuses = 1;
//
//            if (EquipmentBonus.voidRange(player)) {// , CombatType.RANGED)) {
//                otherBonuses = 1.2;
//            }
//            if (EquipmentBonus.voidmRange(player)) {// , CombatType.RANGED)) {
//                otherBonuses = 1.2;
//            }
//
//            // Do calculations of maxhit...
//            double effectiveRangeDamage = (int) (rangeLevel * prayerMod * otherBonuses);
//
//            double baseDamage = 1.3 + effectiveRangeDamage / 10 + rangedStrength / 80 + effectiveRangeDamage * rangedStrength / 640;
//
//            double specialBonus = 1;
//            // Special attacks!
//            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.RANGED) {
//                specialBonus = player.getCombatSpecial().getStrengthBonus();
//            }
//
//            maxHit = (long) baseDamage * specialBonus;
//
//            if (victim.isNpc()) {
//                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()) {
//                    if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071
//                            || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069) {
//                        maxHit *= 1.05;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
//                        maxHit *= 1.07;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
//                        maxHit *= 1.10;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
//                        maxHit *= 1.15;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
//                        maxHit *= 1.25;
//                    }
//                }
//            }
//
//            if (player.getDmgPotionTimer() > 0) {
//                maxHit *= 1.5;
//            }
//            if(ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.DAMAGE || ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.ALL_PERKS) {
//                maxHit *= 1.5;
//            }
//
//            for (Item item : player.getEquipment().getItems()) {
//                ItemEffect effect = item.getEffect();
//                if (effect == ItemEffect.NOTHING)
//                    continue;
//                switch (effect) {
//                    /*case RANGE_DAMAGE:
//                        maxHit *= item.getBonus() / 2;
//                        break;*/
//                    case ALL_DAMAGE_LOW:
//                        maxHit *= 1.05;
//                        break;
//                    case ALL_DAMAGE_MEDIUM:
//                        maxHit *= 1.07;
//                        break;
//                    case ALL_DAMAGE_HIGH:
//                        maxHit *= 1.09;
//                        break;
//                }
//            }
//
//            if (player.getInventory().contains(4442)) {
//                maxHit *= 1.5;
//            }
//            if (player.getEquipment().contains(23049)) { //Tier 6 Aura
//                maxHit *= 1.10D;
//            }
//            if (player.getEquipment().contains(23212)) { //Tier 6 Aura
//                maxHit *= 1.15D;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 1906) {
//                maxHit *= 1.1D;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 9833) {
//                maxHit *= 1.2D;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 1801) {// admin pet
//                maxHit *= 1.15D;
//            }
//
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 9013) {// admin pet
//                maxHit *= 1.25D;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == 9016) {// admin pet
//                maxHit *= 1.25D;
//            }
//            if (player.getEquipment().contains(23034) &&
//                    player.getEquipment().contains(23035) &&
//                    player.getEquipment().contains(23036) &&
//                    player.getEquipment().contains(23037) &&
//                    player.getEquipment().contains(23038)) {
//                maxHit *= 1.10;
//            }
//
//            if (victim.isNpc()) {
//                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()
//                        && victim.getConstitution() >= ((NPC) victim).getDefaultConstitution()) {
//                    int percent = -1;
//                    if (player.getEquipment().contains(22000)) {
//                        percent = 2;
//                    } else if (player.getEquipment().contains(22001)) {
//                        percent = 4;
//                    } else if (player.getEquipment().contains(22002)) {
//                        percent = 6;
//                    } else if (player.getEquipment().contains(22003)) {
//                        percent = 8;
//                    } else if (player.getEquipment().contains(22004)) {
//                        percent = 10;
//                    }
//                    if (Misc.random(99) + 1 <= percent) {
//                        maxHit = victim.getConstitution();
//                    }
//                }
//            }
//            if (player.getDoubleDMGTimer() > 0) {
//                maxHit *= 2;
//            }
//            if (player.getMinutesVotingDMG() > 0) {
//                maxHit *= 2;
//            }
//        }
//
//        maxHit *= 10;
//
//        if (victim != null && victim.isNpc() && (entity.isPlayer() && !entity.asPlayer().getRank().isDeveloper())) {
//            maxHit = (long) NpcMaxHitLimit.limit((NPC) victim, maxHit, entity.asPlayer());
//        }
//        return (long) Math.floor(maxHit);
//    }
}
