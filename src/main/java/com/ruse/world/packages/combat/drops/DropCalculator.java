package com.ruse.world.packages.combat.drops;

import com.ruse.world.content.combat.prayer.PrayerHandler;
import com.ruse.world.content.equipmentenhancement.BoostType;
import com.ruse.world.content.serverperks.ServerPerks;
import com.ruse.world.entity.impl.player.Player;
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

        if (PrayerHandler.isActivated(player,PrayerHandler.GNOMES_GREED)) {
            chance += 10;
        }

        chance += player.getMode().doubleDropRate();

        chance += player.getDonator().getDr();

        chance += player.getVip().getDr();

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

        chance += player.getDonator().getDr();

        chance += player.getVip().getDr();

        chance += player.getMode().dropRate();

        chance += player.getEquipmentEnhancement().getBoost(BoostType.DR);

        if (ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.DR || ServerPerks.getInstance().getActivePerk() == ServerPerks.Perk.ALL_PERKS) {
            chance *= 1.5;
        }

        if(player.getRank().isDeveloper()){
            chance += 100;
        }

        return chance;
    }
}
