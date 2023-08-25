package com.ruse.world.packages.combat.drops;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

public class DropCalculator {

    public static double getDoubleDropChance(@NotNull Player player, int npcId){
        double chance = 0.0;

        chance += player.getEquipment().getDoubleDrop();
        chance += player.getEquipment().getBonus() == null ? 0 : player.getEquipment().getBonus().doubleDropChance();

        return chance;
    }

    public static double getDropChance(@NotNull Player player, int npcId){
        double chance = 0.0;

        chance += player.getEquipment().getDropRateBonus();
        chance += player.getEquipment().getBonus() == null ? 0 : player.getEquipment().getBonus().dropChance();

        if(player.getRank().isDeveloper()){
            chance = 26;
        }

        return chance;
    }
}
