package com.ruse.world.packages.combat.sets;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;
import com.ruse.world.packages.combat.sets.impl.RuthlessGladiator;

/**
 * To be redone, done crapply
 */
public class SetBonuses {

    public static SetBonus checkBonus(Player player){
        if(CombatConstants.waeringRuthlessGladiator(player)) {
            return new RuthlessGladiator();
        }
        return null;
    }
}
