package com.ruse.world.packages.combat;

import com.ruse.world.entity.impl.player.Player;

public class CombatConstants {

    public static boolean wearingEventArmor(Player player){
        return player.getEquipment().contains(23127) && player.getEquipment().contains(23133)
                && player.getEquipment().contains(23128) && player.getEquipment().contains(23132)
                && player.getEquipment().contains(23129) && player.getEquipment().contains(23131)
                && player.getEquipment().contains(23130);
    }
}
