package com.ruse.world.packages.combat;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.ruse.model.container.impl.Equipment.*;

public class CombatConstants {

    public static boolean wearingSlayerArmor(@NotNull Player player){
        return player.getEquipment().containsAll(24000, 24001, 24002);
    }

    public static boolean wearingUltimateBankItem(@NotNull Player player){
        return player.getEquipment().containsAny(4446, 19886, 15450, 23087, 19888, 18888, 13555, 15834, 11195, 4489, 18818, 18823);
    }


    public static boolean waeringRuthlessGladiator(@NotNull Player player){
        return player.getEquipment().containsAll(15005, 15006, 15007, 15008,
                15200, 15201, 15100, 14915);
    }
}
