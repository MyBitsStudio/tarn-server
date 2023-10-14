//package com.ruse.world.content;
//
//import com.ruse.GameSettings;
//import com.ruse.model.Item;
//import com.ruse.model.Locations;
//import com.ruse.model.container.impl.Equipment;
//import com.ruse.world.packages.combat.prayer.PrayerHandler;
//import com.ruse.world.packages.equipmentenhancement.BoostType;
//import com.ruse.world.packages.serverperks.ServerPerks;
//import com.ruse.world.content.skill.impl.summoning.BossPets;
//import com.ruse.world.entity.impl.player.Player;
//import com.ruse.world.packages.combat.CombatConstants;
//
//public class CustomDropUtils {
//
//
//    /**
//     * Increases Drop Rate
//     *
//     * @param player
//     * @return
//     */
//    public static int drBonus(Player player, int npc) {
//        int percentBoost = 0;
//
//        percentBoost += player.getEquipment().getDropRateBonus();
//
//        percentBoost += player.getEquipment().getBonus() == null ? 0 : player.getEquipment().getBonus().dropChance();
//
//        if (player.getEquipment().contains(23044)) { //Tier 1 Aura
//            percentBoost += 5;
//        }
//        if (player.getEquipment().contains(23045)) { //Tier 2 Aura
//            percentBoost += 7;
//        }
//        if (player.getEquipment().contains(23046)) { //Tier 3 Aura
//            percentBoost += 10;
//        }
//        if (player.getEquipment().contains(3324)) { //Tier 3 Aura
//            percentBoost += 3;
//        }
//        if (player.getEquipment().contains(20492)) { //Tier 3 Aura
//            percentBoost += 6;
//        }
//        if (player.getEquipment().contains(4001)) { //Tier 3 Aura
//            percentBoost += 9;
//        }
//        if (player.getEquipment().contains(23047)) { //Tier 4 Aura
//            percentBoost += 15;
//        }
//        if (player.getEquipment().contains(23048)) { //Tier 5 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(23049)) { //Tier 6 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(23212)) { //Tier 7 Aura
//            percentBoost += 25;
//        }
//        if (player.getEquipment().contains(7995)) { //Tier 6 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(22111)) { //Tier 6 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(22109)) { //Tier 6 Aura
//            percentBoost += 40;
//        }
//
//        if (player.getEquipment().contains(22100)) {
//            percentBoost += 20;
//        }
//
//        if (player.getEquipment().contains(17391)) {
//            percentBoost += 10;
//        }
//        if (player.getEquipment().contains(1857) || player.getEquipment().contains(24011)) {
//            percentBoost += 20;
//        }
//
//        if (player.getEquipment().contains(23092)
//                || player.getEquipment().contains(23093)
//                || player.getEquipment().contains(23094)) {// valor rings
//            percentBoost += 10;
//        }
//
//        if(player.getLocation() == Locations.Location.SAPPHIRE_ZONE
//                || player.getLocation() == Locations.Location.EMERALD_ZONE
//                || player.getLocation() == Locations.Location.RUBY_ZONE
//                || player.getLocation() == Locations.Location.DIAMOND_ZONE
//                || player.getLocation() == Locations.Location.ZENYTE_ZONE) {
//            percentBoost += 10;
//
//        }
//        // creator set:
//        if (player.getEquipment().contains(23127))
//            percentBoost += 4;
//        if (player.getEquipment().contains(23128))
//            percentBoost += 4;
//        if (player.getEquipment().contains(23129))
//            percentBoost += 4;
//        if (player.getEquipment().contains(23130))
//            percentBoost += 3;
//        if (player.getEquipment().contains(23131))
//            percentBoost += 3;
//        if (player.getEquipment().contains(23132))
//            percentBoost += 3;
//        if (player.getEquipment().contains(23133))
//            percentBoost += 4;
//		//
//
//        if (player.isInMinigame()) {
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GREEN_FENRIR_PET.npcId) {
//                percentBoost += 10;
//            }
//        }
////
////        if (npc == player.getSlayer().getSlayerTask().getNpcId()) {
////            if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071) {
////                percentBoost += 5;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069 || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
////                percentBoost += 7;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
////                percentBoost += 10;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
////                percentBoost += 15;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
////                percentBoost += 20;
////            }
////        }
//
//        if (player.isInsideRaids()) {
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.DEMON_PET.npcId) {
//                percentBoost += 10;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GOLEM_PET.npcId) {
//                percentBoost += 15;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.DRAGON_PET.npcId) {
//                percentBoost += 25;
//            }
//
//
//        } else {
//            /*if (npc == player.getSlayer().getSlayerTask().getNpcId()) {
//                if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                        && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.RED_FENRIR_PET.npcId) {
//                    percentBoost += 25;
//                }
//            }*/
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SKREEG_PET.npcId) {
//                percentBoost += 10;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ORIX_PET.npcId) {
//                percentBoost += 20;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.CRYSTAL_ORC_PET.npcId) {
//                percentBoost += 25;
//            }
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ODIN_PET.npcId) {
//            percentBoost += 25;
//        }
//
//        //BOSS PETS
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SANCTUM_GOLEM_PET.npcId) {
//            percentBoost += 10;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.MUTANT_HYDRA_PET.npcId) {
//            percentBoost += 12;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GORVEK_PET.npcId) {
//            percentBoost += 14;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.DRAGONITE_PET.npcId) {
//            percentBoost += 16;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ASMODEUS_PET.npcId) {
//            percentBoost += 18;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.MALVEK_PET.npcId) {
//            percentBoost += 20;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ONYX_GRIFFIN_PET.npcId) {
//            percentBoost += 22;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ZEIDAN_GRIMM_PET.npcId) {
//            percentBoost += 24;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.AGTHOMOTH_PET.npcId) {
//            percentBoost += 26;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.LILINRYSS_PET.npcId) {
//            percentBoost += 28;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GROUDON_PET.npcId) {
//            percentBoost += 30;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.VARTHRAMOTH_PET.npcId) {
//            percentBoost += 32;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.TYRANT_LORD_PET.npcId) {
//            percentBoost += 34;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.LUCIFER_PET.npcId) {
//            percentBoost += 36;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.VIRTUOSO_PET.npcId) {
//            percentBoost += 38;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.AGUMON_PET.npcId) {
//            percentBoost += 40;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.WHITE_BEARD_PET.npcId) {
//            percentBoost += 42;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.PANTHER_PET.npcId) {
//            percentBoost += 44;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.LEVIATHAN_PET.npcId) {
//            percentBoost += 46;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.CALAMITY_PET.npcId) {
//            percentBoost += 48;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SLENDER_MAN_PET.npcId) {
//            percentBoost += 50;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.CHARYBDIS_PET.npcId) {
//            percentBoost += 52;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SCYLLA_PET.npcId) {
//            percentBoost += 54;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.EXODEN_PET.npcId) {
//            percentBoost += 56;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.EZKEL_NOJAD_PET.npcId) {
//            percentBoost += 58;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.JANEMBA_PET.npcId) {
//            percentBoost += 60;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.FRIEZA_PET.npcId) {
//            percentBoost += 62;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.PERFECT_CELL_PET.npcId) {
//            percentBoost += 64;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SUPER_BUU_PET.npcId) {
//            percentBoost += 66;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GOKU_PET.npcId) {
//            percentBoost += 68;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.BYAKUYA_PET.npcId) {
//            percentBoost += 70;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.FAZULA_PET.npcId) {
//            percentBoost += 75;
//        }
//
//        //BOSS PETS END
//
//
//        //1% TICKETS
//
//
//        if (player.getInventory().contains(23174)) {
//            percentBoost += 10;
//        }
//        if (player.getInventory().contains(15834)) {
//            percentBoost += 25;
//        }
//        if (player.getInventory().contains(8832)) {
//            percentBoost += 35;
//        }
//        if (player.getInventory().contains(23178)) {
//            percentBoost += 100;
//        }
//
//
//
//        /**
//         * Donator Rank bonusses
//         */
//
//
////        if(player.getAmountDonated() >= Donation.ZENYTE_DONATION_AMOUNT || player.getRights().equals(PlayerRights.YOUTUBER) || player.getRights().equals(PlayerRights.DEVELOPER) || player.getRights().equals(PlayerRights.HELPER) || player.getRights().equals(PlayerRights.MODERATOR) || player.getRights().equals(PlayerRights.ADMINISTRATOR)) {
////            percentBoost += 25;
////        } else if(player.getAmountDonated() >= Donation.ONYX_DONATION_AMOUNT) {
////            percentBoost += 20;
////        } else if(player.getAmountDonated() >= Donation.DIAMOND_DONATION_AMOUNT) {
////            percentBoost += 15;
////        } else if(player.getAmountDonated() >= Donation.RUBY_DONATION_AMOUNT) {
////            percentBoost += 10;
////        } else if(player.getAmountDonated() >= Donation.EMERALD_DONATION_AMOUNT) {
////            percentBoost += 7;
////        } else if(player.getAmountDonated() >= Donation.SAPPHIRE_DONATION_AMOUNT) {
////            percentBoost += 5;
////        }
//
//        if (player.getInventory().contains(4440)) {
//            percentBoost *= 1.5;
//        }
//
//        if (GameSettings.DOUBLEDR) {
//            percentBoost *= 2;
//        }
//
//        if (player.getDoubleDRTimer() > 0) {
//            percentBoost += 100;
//        }
//        if (player.getMinutesVotingDR() > 0) {
//            percentBoost += 100;
//        }
//        if (player.getDrPotionTimer() > 0) {
//            percentBoost *= 2;
//        }
//
//        if (GameSettings.DOUBLE_DROP) {
//            percentBoost += 100;
//        }
//
//
//
//        if (PrayerHandler.isActivated(player,PrayerHandler.GNOMES_GREED)) {
//            percentBoost += 10;
//        }
//        if (PrayerHandler.isActivated(player,PrayerHandler.FURY_SWIPE)) {
//            percentBoost += 25;
//        }
//
//        if(percentBoost >= 2500) {
//            percentBoost = 2500;
//        }
//
//        percentBoost += player.getEquipmentEnhancement().getBoost(BoostType.DR);
//
//        if(player.getInstance() != null){
//            int diff = Integer.parseInt(player.getVariables().getInterfaceSettings()[2]);
//            if(diff != 0){
//                percentBoost += (250 * diff);
//            }
//        }
//
//        return percentBoost;
//    }
//
//
//    public static int getDoubleDropChance(Player player, int npc) {
//        int percentBoost = 0;
//
//        percentBoost += player.getEquipment().getDoubleDrop();
//        percentBoost += player.getEquipment().getBonus() == null ? 0 : player.getEquipment().getBonus().doubleDropChance();
//
//        if (player.getDdrPotionTimer() > 0) {
//            percentBoost += 100;
//        }
//
//        Equipment equipment = player.getEquipment();
//        if (equipment.contains(23092)
//                || equipment.contains(23093)
//                || equipment.contains(23094)) {// valor rings
//			percentBoost += 10;
//        }
//
//        //1% TICKETS
//
//
//        if (player.getEquipment().contains(23044)) { //Tier 1 Aura
//            percentBoost += 5;
//        }
//        if (player.getEquipment().contains(23045)) { //Tier 2 Aura
//            percentBoost += 7;
//        }
//        if (player.getEquipment().contains(23046)) { //Tier 3 Aura
//            percentBoost += 10;
//        }
//        if (player.getEquipment().contains(23047)) { //Tier 4 Aura
//            percentBoost += 15;
//        }
//        if (player.getEquipment().contains(23048)) { //Tier 5 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(23049)) { //Tier 6 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(23212)) { //Tier 7 Aura
//            percentBoost += 25;
//        }
//        if (player.getEquipment().contains(7995)) { //Tier 6 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(8832)) { //Tier 6 Aura
//            percentBoost += 10;
//        }
//        if (player.getEquipment().contains(15834)) { //Tier 6 Aura
//            percentBoost += 10;
//        }
//        if (player.getEquipment().contains(22111)) { //Tier 6 Aura
//            percentBoost += 20;
//        }
//        if (player.getEquipment().contains(22109)) { //Tier 6 Aura
//            percentBoost += 40;
//        }
//
//
//
//		// creator set:
//		if (player.getEquipment().contains(23127))
//			percentBoost += 4;
//		if (player.getEquipment().contains(23128))
//			percentBoost += 4;
//		if (player.getEquipment().contains(23129))
//			percentBoost += 4;
//		if (player.getEquipment().contains(23130))
//			percentBoost += 3;
//		if (player.getEquipment().contains(23131))
//			percentBoost += 3;
//		if (player.getEquipment().contains(23132))
//			percentBoost += 3;
//		if (player.getEquipment().contains(23133))
//			percentBoost += 4;
//		//
//
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ODIN_PET.npcId) {
//            percentBoost += 25;
//        }
//
//        if (player.isInMinigame()) {
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GREEN_FENRIR_PET.npcId) {
//                percentBoost += 10;
//            }
//        }
////        if (npc == player.getSlayer().getSlayerTask().getNpcId()) {
////            if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071) {
////                percentBoost += 5;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069 || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
////                percentBoost += 7;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
////                percentBoost += 10;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
////                percentBoost += 15;
////            } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
////                percentBoost += 20;
////            }
////        }
//        if (player.isInsideRaids()) {
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.DEMON_PET.npcId) {
//                percentBoost += 10;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GOLEM_PET.npcId) {
//                percentBoost += 15;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.DRAGON_PET.npcId) {
//                percentBoost += 25;
//            }
//        } else {
//          /*  if (npc == player.getSlayer().getSlayerTask().getNpcId()) {
//                if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                        && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.RED_FENRIR_PET.npcId) {
//                    percentBoost += 25;
//                }
//            }*/
//
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SKREEG_PET.npcId) {
//                percentBoost += 10;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ORIX_PET.npcId) {
//                percentBoost += 20;
//            }
//            if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                    && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.CRYSTAL_ORC_PET.npcId) {
//                percentBoost += 25;
//            }
//        }
//
//        //BOSS PETS
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SANCTUM_GOLEM_PET.npcId) {
//            percentBoost += 10;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.MUTANT_HYDRA_PET.npcId) {
//            percentBoost += 12;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GORVEK_PET.npcId) {
//            percentBoost += 14;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.DRAGONITE_PET.npcId) {
//            percentBoost += 16;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ASMODEUS_PET.npcId) {
//            percentBoost += 18;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.MALVEK_PET.npcId) {
//            percentBoost += 20;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ONYX_GRIFFIN_PET.npcId) {
//            percentBoost += 22;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.ZEIDAN_GRIMM_PET.npcId) {
//            percentBoost += 24;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.AGTHOMOTH_PET.npcId) {
//            percentBoost += 26;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.LILINRYSS_PET.npcId) {
//            percentBoost += 28;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GROUDON_PET.npcId) {
//            percentBoost += 30;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.VARTHRAMOTH_PET.npcId) {
//            percentBoost += 32;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.TYRANT_LORD_PET.npcId) {
//            percentBoost += 34;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.LUCIFER_PET.npcId) {
//            percentBoost += 36;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.VIRTUOSO_PET.npcId) {
//            percentBoost += 38;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.AGUMON_PET.npcId) {
//            percentBoost += 40;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.WHITE_BEARD_PET.npcId) {
//            percentBoost += 42;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.PANTHER_PET.npcId) {
//            percentBoost += 44;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.LEVIATHAN_PET.npcId) {
//            percentBoost += 46;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.CALAMITY_PET.npcId) {
//            percentBoost += 48;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SLENDER_MAN_PET.npcId) {
//            percentBoost += 50;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.CHARYBDIS_PET.npcId) {
//            percentBoost += 52;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SCYLLA_PET.npcId) {
//            percentBoost += 54;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.EXODEN_PET.npcId) {
//            percentBoost += 56;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.EZKEL_NOJAD_PET.npcId) {
//            percentBoost += 58;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.JANEMBA_PET.npcId) {
//            percentBoost += 60;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.FRIEZA_PET.npcId) {
//            percentBoost += 62;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.PERFECT_CELL_PET.npcId) {
//            percentBoost += 64;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SUPER_BUU_PET.npcId) {
//            percentBoost += 66;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.GOKU_PET.npcId) {
//            percentBoost += 68;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.BYAKUYA_PET.npcId) {
//            percentBoost += 70;
//        }
//        if (player.getSummoning() != null && player.getSummoning().getFamiliar() != null
//                && player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.FAZULA_PET.npcId) {
//            percentBoost += 75;
//        }
//
//        //BOSS PETS END
//
//        if (player.getDoubleDDRTimer() > 0) {
//            percentBoost += 100;
//        }
//
//        if(percentBoost >= 2500) {
//            percentBoost = 2500;
//        }
//
//
//
//        /**
//         * Donator Rank bonusses
//         */
//
//
////        if(player.getAmountDonated() >= Donation.ZENYTE_DONATION_AMOUNT || player.getRights().equals(PlayerRights.YOUTUBER) || player.getRights().equals(PlayerRights.DEVELOPER) || player.getRights().equals(PlayerRights.HELPER) || player.getRights().equals(PlayerRights.MODERATOR) || player.getRights().equals(PlayerRights.ADMINISTRATOR)) {
////            percentBoost += 25;
////        } else if(player.getAmountDonated() >= Donation.ONYX_DONATION_AMOUNT) {
////            percentBoost += 20;
////        } else if(player.getAmountDonated() >= Donation.DIAMOND_DONATION_AMOUNT) {
////            percentBoost += 15;
////        } else if(player.getAmountDonated() >= Donation.RUBY_DONATION_AMOUNT) {
////            percentBoost += 10;
////        } else if(player.getAmountDonated() >= Donation.EMERALD_DONATION_AMOUNT) {
////            percentBoost += 7;
////        } else if(player.getAmountDonated() >= Donation.SAPPHIRE_DONATION_AMOUNT) {
////            percentBoost += 5;
////        }
//
//
//
//
//        return percentBoost;
//    }
//
//
//}
