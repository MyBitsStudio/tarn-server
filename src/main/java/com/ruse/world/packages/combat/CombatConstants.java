package com.ruse.world.packages.combat;

import com.ruse.world.entity.impl.player.Player;
import org.jetbrains.annotations.NotNull;

import static com.ruse.model.container.impl.Equipment.*;

public class CombatConstants {

    public static boolean wearingEventArmor(@NotNull Player player){
//        return player.getEquipment().getItems()[HEAD_SLOT].getId() == 23127 && player.getEquipment().getItems()[BODY_SLOT].getId() == 231128
//                && player.getEquipment().getItems()[LEG_SLOT].getId() == 23129 && player.getEquipment().getItems()[HANDS_SLOT].getId() == 23130
//                && player.getEquipment().getItems()[FEET_SLOT].getId() == 23131 && player.getEquipment().getItems()[CAPE_SLOT].getId() == 23133
//                && player.getEquipment().getItems()[WEAPON_SLOT].getId() == 23132;
        return player.getEquipment().containsAll(23127, 23128, 23129, 23130, 23131, 23132, 23133);
    }

    public static boolean wearingSlayerArmor(@NotNull Player player){
//        return player.getEquipment().getItems()[HEAD_SLOT].getId() == 24000 && player.getEquipment().getItems()[BODY_SLOT].getId() == 24001
//                && player.getEquipment().getItems()[LEG_SLOT].getId() == 24002;
        return player.getEquipment().containsAll(24000, 24001, 24002);
    }
}
