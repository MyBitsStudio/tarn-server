package com.ruse.world.packages.combat;

import com.ruse.model.container.impl.Equipment;
import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static com.ruse.model.container.impl.Equipment.*;

public class CombatConstants {

    private static int[] slayerEquip = {
            21051, 21052, 18819, 4373
    }, slayerHelms = {
            13263, 21075, 21076, 21077, 21078, 21079
    };

    public static boolean wearingSlayerArmor(@NotNull Player player){
        return player.getEquipment().containsAll(slayerEquip)
                && player.getEquipment().containsAny(slayerHelms);
    }

    public static boolean wearingAnySlayer(@NotNull Player player){
        return player.getEquipment().containsAny(slayerEquip)
                || player.getEquipment().containsAny(slayerHelms);
    }

    public static double multiply(Player player){
        double amount = 1.0;
        for(int id : slayerEquip){
            if(player.getEquipment().contains(id)){
                amount += 0.15;
            }
        }
        for(int id : slayerHelms){
            if(player.getEquipment().contains(id)){
                amount += 0.15;
            }
        }
        return amount;
    }

    public static boolean wearingUltimateBankItem(@NotNull Player player){
        return player.getEquipment().containsAny(4446, 19886, 15450, 23087, 19888, 18888, 13555, 15834, 11195, 4489, 18818, 18823);
    }

    public static boolean wearingDonator(@NotNull Player player){
        return player.getEquipment().containsAll(15230, 15231, 15232, 15233, 15234);
    }


    public static boolean waeringRuthlessGladiator(@NotNull Player player){
        return player.getEquipment().containsAll(15005, 15006, 15007, 15008,
                15200, 15201, 15100, 14915);
    }

    public static boolean wearingTarnMaster(@NotNull Player player){
        return player.getEquipment().containsAll(22232, 22239, 22237, 22233,
                22234, 22235, 22236);
    }
}
