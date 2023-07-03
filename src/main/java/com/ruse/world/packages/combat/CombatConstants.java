package com.ruse.world.packages.combat;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.ruse.model.container.impl.Equipment.*;

public class CombatConstants {

    public static boolean wearingEventArmor(@NotNull Player player){
        return player.getEquipment().containsAll(23127, 23128, 23129, 23130, 23131, 23132, 23133);
    }

    public static boolean wearingSlayerArmor(@NotNull Player player){
        return player.getEquipment().containsAll(24000, 24001, 24002);
    }

    public static boolean wearingUltimateBankItem(@NotNull Player player){
        return player.getEquipment().containsAny(4446, 19886, 15450, 23087, 19888, 18888, 13555, 15834, 11195, 4489, 18818, 18823);
    }
}
