package com.ruse.world.packages.combat;

import com.ruse.world.entity.impl.player.Player;

public class CombatConstants {

    public static boolean wearingEventArmor(Player player){
        return player.getEquipment().contains(3480) && player.getEquipment().contains(3479)
                && player.getEquipment().contains(3478) && player.getEquipment().contains(3477)
                && player.getEquipment().contains(3476) && player.getEquipment().contains(3474);
    }
}
