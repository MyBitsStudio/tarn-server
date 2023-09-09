package com.ruse.world.packages.combat.drops;

import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.equipmentenhancement.BoostType;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.content.skill.impl.summoning.BossPets;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;
import org.jetbrains.annotations.NotNull;

public class DropCalculator {

    public static double getDoubleDropChance(@NotNull Player player, int npcId){
        double chance = 0.0;

        chance += player.getEquipment().getDoubleDrop();
        chance += player.getEquipment().getBonus() == null ? 0 : player.getEquipment().getBonus().doubleDropChance();

        if (player.getInventory().contains(21816)) {
            chance += player.getInventory().getAmount(21816);
        }
        if (player.getInventory().contains(21814)) {
            chance += player.getInventory().getAmount(21814);
        }
        if (player.getEquipment().contains(12630)) {
            chance += 250;
        }
        if (player.getEquipment().contains(23330)) {
            chance += 250;
        }
        if(CombatConstants.wearingDonator(player)){
            chance += 1000;
        }


        if (PrayerHandler.isActivated(player,PrayerHandler.GNOMES_GREED)) {
            chance += 10;
        }

        if(player.getVariables().getBooleanValue("double-ddr")){
            chance *= 2;
        }

        if(player.getVariables().getBooleanValue("monic-ddr")){
            chance *= 2;
        }

        chance += player.getMode().doubleDropRate();

        chance += player.getLoyalty().dropChance();

        chance += player.getVip().getDr();

        if(chance >= 3000)
            chance = 3000;

        chance += player.getEquipmentEnhancement().getBoost(BoostType.DR);

        if(player.getRank().isDeveloper()){
            chance += 100;
        }

        return chance;
    }

    public static double getDropChance(@NotNull Player player, int npcId){
        double chance = 0.0;

        chance += player.getEquipment().getDropRateBonus();
        chance += player.getEquipment().getBonus() == null ? 0 : player.getEquipment().getBonus().dropChance();

        if (player.getInventory().contains(21816)) {
            chance += player.getInventory().getAmount(21816);
        }
        if (player.getInventory().contains(21815)) {
            chance += player.getInventory().getAmount(21815);
        }
        if (player.getEquipment().contains(12630)) {
            chance += 250;
        }
        if (player.getEquipment().contains(23330)) {
            chance += 250;
        }
        if(CombatConstants.wearingDonator(player)){
            chance += 1000;
        }

        if (ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.DR || ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.ALL_PERKS) {
            chance *= 1.5;
        }

        if(player.getVariables().getBooleanValue("double-dr")){
            chance *= 2;
        }

        if(player.getVariables().getBooleanValue("monic-dr")){
            chance *= 2;
        }

        if(player.getVariables().getBooleanValue("vote-dr")){
            chance *= 1.25;
        }

        if(CombatConstants.wearingDonator(player)){
            chance += 1000;
        }

        chance *= multiply(player);

        chance += player.getLoyalty().doubleDropChance();

        chance += player.getVip().getDr();

        chance += player.getMode().dropRate();

        if(chance >= 3000)
            chance = 3000;

        chance += player.getEquipmentEnhancement().getBoost(BoostType.DR);


        if(player.getRank().isDeveloper()){
            chance = 4500;
        }

        return chance;
    }

    public static double multiply(@NotNull Player player){
        double multiply = 1.0;
        if(player.getSummoning().getFamiliar() != null) {
            if (player.getSummoning().getFamiliar().getSummonNpc().getId() == BossPets.BossPet.SHADOW.npcId) {
                multiply += 1;
            }
        }
        return multiply;
    }
}
