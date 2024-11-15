package com.ruse.world.packages.combat.max;

import com.ruse.model.container.impl.Equipment;
import com.ruse.util.Misc;
import com.ruse.world.content.BonusManager;
import com.ruse.world.content.combat.CombatType;
import com.ruse.world.content.combat.NpcMaxHitLimit;
import com.ruse.world.packages.combat.prayer.CurseHandler;
import com.ruse.world.packages.combat.prayer.PrayerHandler;
import com.ruse.world.packages.johnachievementsystem.AchievementHandler;
import com.ruse.world.packages.johnachievementsystem.PerkType;
import com.ruse.world.packages.serverperks.ServerPerks;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.Character;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class MagicMax {

    public static long newMagic(Character entity, Character victim){
        long maxHit = 0L;

        if(entity.isNpc()){
            NPC npc = entity.toNpc();
            maxHit = npc.getMaxHit();

            if(victim.isPlayer()){
                Player player = victim.asPlayer();

                double defence = player.getBonusManager().getDefenceBonus()[BonusManager.DEFENCE_MAGIC] / 1_000;

                if(AchievementHandler.hasUnlocked(player, PerkType.DEFENCE)){
                    defence *= (1 + (AchievementHandler.getPerkLevel(player, PerkType.DEFENCE) * 0.05));
                }

                if (player.getEquipment().contains(15448)) {
                    defence *= 1.4;
                }

                if (player.getEquipment().contains(22142)) {
                    defence *= 1.4;
                }

                if (player.getEquipment().contains(23088)) {
                    defence *= 1.2;
                }

                if(defence >= 1000)
                    defence = 1000;


                maxHit -= (long) defence;

                if(maxHit <= 0){
                    maxHit = 1;
                }

                double absorb = player.getBonusManager().getExtraBonus()[BonusManager.ABSORB_MAGIC];

                if(AchievementHandler.hasUnlocked(player, PerkType.ABSORB)){
                    absorb *= (1 + (AchievementHandler.getPerkLevel(player, PerkType.ABSORB) * 0.05));
                }

                if (player.getEquipment().contains(23088)) {
                    absorb *= 1.2;
                }

                if (player.getEquipment().contains(22142)) {
                    absorb *= 1.2;
                }

                if(absorb >= 900)
                    absorb = 900;

                if(absorb > 0){
                    double percent = 1 - ( 1 - (absorb / 1000.0));
                    maxHit -= (long) (maxHit * percent);
                }
            }

            if(npc.getDefinition().isBoss()){
                maxHit *= 1.25;
            }

        } else if(entity.isPlayer()){
            Player player = entity.asPlayer();

            double magicStrength = player.getBonusManager().getOtherBonus()[3];

            double prayerMod = 1.0;
            if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_WILL) || CurseHandler.isActivated(player, CurseHandler.LEECH_MAGIC)) {
                prayerMod = 1.05;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_LORE)) {
                prayerMod = 1.10;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_MIGHT)) {
                prayerMod = 1.15;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                prayerMod = 1.23;
            } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
                prayerMod = 1.25;
            } else if(PrayerHandler.isActivated(player, PrayerHandler.SOUL_LEECH)){
                prayerMod = 1.25 + +(player.getLeechedBonuses()[2] * 0.01);
            }

            double effectiveMagicDamage = (120 * prayerMod) *  (magicStrength / 1_000);

            double baseDamage = 1.3 + (effectiveMagicDamage / 10) + (magicStrength / 70) + effectiveMagicDamage * (magicStrength / 630);


            double specialBonus = 1;
            // Special attacks!
            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.MAGIC) {
                specialBonus = player.getCombatSpecial().getStrengthBonus();
            }


            maxHit = (long) (baseDamage * specialBonus);


            if(ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.ALL_PERKS ||
                    ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.MEGA_PERK ||
                    ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.DMG) {
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

            if (player.getVariables().getBooleanValue("monic-damage")) {
                maxHit *= 2;
            }

            maxHit *= (long) multiplyDamage(player);

            if(player.getEquipment().contains(15589))
                maxHit *= 1.5;

            if(player.getEquipment().contains(21570))
                maxHit *= 1.5;

            if(player.getEquipment().contains(14876))
                maxHit *= 1.5;

            if(player.getEquipment().contains(19888))
                maxHit *= 1.25;

            if(player.getEquipment().contains(18823))
                maxHit *= 1.25;


            maxHit *= player.getEquipment().getBonus() == null ? 1 : (long) player.getEquipment().getBonus().mageDamage();

            if(AchievementHandler.hasUnlocked(player, PerkType.MAGE)){
                maxHit *= (1 + (AchievementHandler.getPerkLevel(player, PerkType.MAGE) * 0.10));
            }

            maxHit *= 10;

        }

        if (victim != null && victim.isNpc() && (entity.isPlayer() && !entity.asPlayer().getRank().isDeveloper())) {
            maxHit = NpcMaxHitLimit.limit((NPC) victim, maxHit, entity.asPlayer());
        }

        return maxHit;
    }
    public static double multiplyDamage(@NotNull Player player){
        double multiply = 1.0;
        if(player.getSummoning().getFamiliar() != null) {
            if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.MAGE_PET.npcId) {
                multiply += 0.10;
            }
            if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SHADOW.npcId) {
                multiply += 0.5;
            }
        }

        return multiply;
    }

//    public static long magic(Character entity, Character victim) {
//        double maxHit = 0;
//
//        if (entity.isNpc()) {
//            NPC npc = (NPC) entity;
//            maxHit = npc.getMaxHit() / 10;
//        } else if (entity.isPlayer()) {
//            Player player = (Player) entity;
//
//            double magicStrength = player.getBonusManager().getOtherBonus()[3];
//            double magicLevel = player.getSkillManager().getCurrentLevel(Skill.MAGIC);
//
//
//
//            // Prayers
//            double prayerMod = 1.0;
//            if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_WILL) || CurseHandler.isActivated(player, CurseHandler.LEECH_MAGIC)) {
//                prayerMod = 1.05;
//            } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_LORE)) {
//                prayerMod = 1.10;
//            } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_MIGHT)) {
//                prayerMod = 1.15;
//            } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
//                prayerMod = 1.23;
//            } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
//                prayerMod = 1.25;
//            } else if(PrayerHandler.isActivated(player, PrayerHandler.SOUL_LEECH)){
//                prayerMod = 1.25 + +(player.getLeechedBonuses()[2] * 0.01);
//                if (Misc.getRandom(100) <= 1) {
//                    player.setDoubleDMGTimer(1);
//                    // player.getPacketSender().sendMessage("Coup de grace activated");
//                }
//            }
//            double otherBonuses = 1;
//
//            // Void hits 10% more
//            // Do calculations of maxhit...
//            double effectiveMagicDamage = (int) (magicLevel * prayerMod * otherBonuses);
//
//
//            double baseDamage = 1.3 + effectiveMagicDamage / 10 + magicStrength / 80 + effectiveMagicDamage * magicStrength / 640;
//
//            double specialBonus = 1;
//            // Special attacks!
//            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.MAGIC) {
//                specialBonus = player.getCombatSpecial().getStrengthBonus();
//            }
//
//
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
//            if (player.getEquipment().contains(23021) &&
//                    player.getEquipment().contains(23022) &&
//                    player.getEquipment().contains(23023) &&
//                    player.getEquipment().contains(23024) &&
//                    player.getEquipment().contains(23025)) {
//                maxHit *= 1.10;
//            }
//            if (player.getEquipment().contains(23048)) { //Tier 5 Aura
//                maxHit *= 1.05D;
//            }
//            if (player.getEquipment().contains(23049)) { //Tier 6 Aura
//                maxHit *= 1.10D;
//            }
//            if (player.getEquipment().contains(23212)) { //Tier 7 Aura
//                maxHit *= 1.15D;
//            }
//            if (player.getEquipment().contains(22111)) { //Tier 6 Aura
//                maxHit *= 1.10D;
//            }
//            if (player.getDmgPotionTimer() > 0) {
//                maxHit *= 1.5;
//            }
//            if(ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.DAMAGE || ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.ALL_PERKS) {
//                maxHit *= 1.5;
//            }
//            for (Item item : player.getEquipment().getItems()) {
//                ItemEffect effect = item.getEffect();
//                if (effect == ItemEffect.NOTHING)
//                    continue;
//                switch (effect) {
//                   /* case MAGIC_DAMAGE:
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
//
//        if (victim != null && victim.isNpc() && (entity.isPlayer() && !entity.asPlayer().getRank().isDeveloper())) {
//            maxHit = (long) NpcMaxHitLimit.limit((NPC) victim, maxHit, entity.asPlayer());
//        }
//        return (long) Math.floor(maxHit);

}
