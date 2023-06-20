package com.ruse.world.packages.combat.max;

import com.ruse.model.Item;
import com.ruse.model.Skill;
import com.ruse.model.container.impl.Equipment;
import com.ruse.model.projectile.ItemEffect;
import com.ruse.util.Misc;
import com.ruse.world.content.combat.CombatFactory;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.NpcMaxHitLimit;
import com.ruse.world.content.combat.prayer.CurseHandler;
import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.combat.weapon.FightStyle;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.DropUtils;
import com.ruse.world.content.skill.impl.summoning.Familiar;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public class MeleeMax {

//    public static long melee(Character entity, Character victim) {
//        double maxHit = 0;
//        if (entity.isNpc()) {
//            NPC npc = (NPC) entity;
//            maxHit = npc.getMaxHit();
//            if (npc.getStrengthWeakened()[0]) {
//                maxHit -= (int) (0.10 * maxHit);
//            } else if (npc.getStrengthWeakened()[1]) {
//                maxHit -= (int) (0.20 * maxHit);
//            } else if (npc.getStrengthWeakened()[2]) {
//                maxHit -= (int) (0.30 * maxHit);
//            }
//        } else if (entity.isPlayer()) {
//            Player player = (Player) entity;
//
//            double base = 0;
//            double effective = getEffectiveStr(player);
//            double strengthBonus = player.getBonusManager().getOtherBonus()[0];
//            double specialBonus = 1;
//
//            // Use our multipliers to adjust the maxhit...
//
//            base = 1.3 + effective / 10 + strengthBonus / 80 + effective * strengthBonus / 640;
//
//            // Special effects also affect maxhit
//            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.MELEE) {
//                specialBonus = player.getCombatSpecial().getStrengthBonus();
//            }
//
//            if (specialBonus > 1) {
//                base = Math.round(base) * specialBonus;
//            } else {
//                base = (long) base;
//            }
//
//            if (victim.isNpc()) {
//                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()) {
//                    if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071
//                            || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069) {
//                        base *= 1.05;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
//                        base *= 1.07;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
//                        base *= 1.10;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
//                        base *= 1.15;
//                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
//                        base *= 1.25;
//                    }
//                }
//            }
//            Familiar playerFamiliar = player.getSummoning().getFamiliar();
//
//            if (playerFamiliar != null) {
//                double bonus = DropUtils.getDamageBonus(playerFamiliar.getSummonNpc().getId());
//                base *= bonus;
//            }
//
//            if (player.getDmgPotionTimer() > 0) {
//                base *= 1.5;
//            }
//            if(ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.DAMAGE || ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.ALL_PERKS) {
//                base *= 1.5;
//            }
//
//            for (Item item : player.getEquipment().getItems()) {
//                ItemEffect effect = item.getEffect();
//                if (effect == ItemEffect.NOTHING)
//                    continue;
//                switch (effect) {
//                    /*case STRENGTH_DAMAGE:
//                        base *= item.getBonus() / 2;
//                        break;*/
//                    case ALL_DAMAGE_LOW:
//                        base *= 1.05;
//                        break;
//                    case ALL_DAMAGE_MEDIUM:
//                        base *= 1.07;
//                        break;
//                    case ALL_DAMAGE_HIGH:
//                        base *= 1.09;
//                        break;
//                }
//            }
//
//            if (player.getInventory().contains(4442)) {
//                base *= 1.5;
//            }
//
//            maxHit = (long) base * 10;
//
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
//
//                    if (Misc.random(99) + 1 <= percent) {
//                        maxHit = victim.getConstitution() * 10;
//                    }
//                }
//            }
//
//            if (player.getEquipment().contains(23028) &&
//                    player.getEquipment().contains(23029) &&
//                    player.getEquipment().contains(23030) &&
//                    player.getEquipment().contains(23031) &&
//                    player.getEquipment().contains(23032)) {
//                maxHit *= 1.10;
//            }
//            if (player.getEquipment().contains(23049)) { //Tier 6 Aura
//                maxHit *= 1.10D;
//            }
//            if (player.getEquipment().contains(23212)) { //Tier 7 Aura
//                maxHit *= 1.15D;
//            }
//
//            if (player.getDoubleDMGTimer() > 0) {
//                maxHit *= 2;
//            }
//            if (player.getMinutesVotingDMG() > 0) {
//                maxHit *= 2;
//            }
//        }
//
////        // Dharoks effect
////        if (CombatFactory.fullDharoks(entity)) {
////            long hitpoints = entity.getConstitution() / 10;
////            if (entity.isNpc()) {
////                long missingHealth = ((NPC) entity).getDefinition().getHitpoints() - hitpoints;
////                double addToHit = missingHealth * 0.01 + 1;
////                maxHit *= addToHit;
////            } else {
////                long missingHealth = ((Player) entity).getSkillManager().getMaxLevel(Skill.CONSTITUTION) - hitpoints;
////                double addToHit = missingHealth * 0.01 + 1;
////                maxHit *= addToHit;
////                if (maxHit >= 990)
////                    maxHit = 990;
////            }
////        }
//
//        maxHit *= 10;
//
//        if (victim != null && victim.isNpc() && (entity.isPlayer() && !entity.asPlayer().getRank().isDeveloper())) {
//            maxHit = (long) NpcMaxHitLimit.limit((NPC) victim, maxHit, entity.asPlayer());
//
//        }
//
//        return (long) Math.floor(maxHit);
//    }

    public static long newMelee(Character entity, Character victim){
        long maxHit = 0L;

        if(entity.isNpc()) {
            NPC npc = entity.toNpc();
            maxHit = npc.getMaxHit();

            if (victim.isPlayer()) {
                Player player = victim.asPlayer();

                long defence = (long) (player.getBonusManager().getDefenceBonus()[0] / 1_000_000);

                System.out.println("Defence " + defence);
                maxHit -= (defence / 5);

                maxHit /= 10;
                if (maxHit <= 0) {
                    maxHit = 1;
                }
            }
            System.out.println("NPC Max Hit Melee: " + maxHit);
        } else if(entity.isPlayer()) {
            Player player = entity.asPlayer();

            double effective = getEffectiveStr(player);
            double strengthBonus = player.getBonusManager().getOtherBonus()[0];
            double specialBonus = 1;

            // Use our multipliers to adjust the maxhit...

            double base = 1.3 + effective / 10 + strengthBonus / 70 + effective * strengthBonus / 600;

            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.MELEE) {
                specialBonus = player.getCombatSpecial().getStrengthBonus();
            }

            maxHit = (long) (base * specialBonus);

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

            for (Item item : player.getEquipment().getItems()) {
                ItemEffect effect = item.getEffect();
                if (effect == ItemEffect.NOTHING)
                    continue;
                switch (effect) {
                   /* case MAGIC_DAMAGE:
                        maxHit *= item.getBonus() / 2;
                        break;*/
                    case ALL_DAMAGE_LOW:
                        maxHit *= 1.05;
                        break;
                    case ALL_DAMAGE_MEDIUM:
                        maxHit *= 1.07;
                        break;
                    case ALL_DAMAGE_HIGH:
                        maxHit *= 1.09;
                        break;
                }
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
                        base *= 1.05;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
                        base *= 1.07;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
                        base *= 1.10;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
                        base *= 1.15;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
                        base *= 1.25;
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

        }

        maxHit *= 10;

        if (victim != null && victim.isNpc() && (entity.isPlayer() && !entity.asPlayer().getRank().isDeveloper())) {
            maxHit = (long) NpcMaxHitLimit.limit((NPC) victim, maxHit, entity.asPlayer());
        }

        return maxHit;
    }

    public static double getEffectiveStr(Player player) {
        double styleBonus = 0;
        FightStyle style = player.getFightType().getStyle();

        double otherBonus = 1;

        double prayerMod = 1.0;
        double random = Math.random() * 10;
        if (PrayerHandler.isActivated(player, PrayerHandler.BURST_OF_STRENGTH) || CurseHandler.isActivated(player, CurseHandler.LEECH_STRENGTH)) {
            prayerMod = 1.05;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.SUPERHUMAN_STRENGTH)) {
            prayerMod = 1.1;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.ULTIMATE_STRENGTH)) {
            prayerMod = 1.15;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
            prayerMod = 1.18;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
            prayerMod = 1.23;
        } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
            prayerMod = 1.25;
        } else if(PrayerHandler.isActivated(player, PrayerHandler.SOUL_LEECH)) {
            prayerMod = 1.15 + +(player.getLeechedBonuses()[2] * 0.01);
            if (Misc.getRandom(100) <= 1) {
                player.setDoubleDMGTimer(1);
                // player.getPacketSender().sendMessage("Coup de grace activated");

            }
        }

        return (int) (player.getSkillManager().getCurrentLevel(Skill.STRENGTH) * prayerMod * otherBonus + styleBonus);
    }
}