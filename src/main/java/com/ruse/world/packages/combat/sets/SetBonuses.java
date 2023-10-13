package com.ruse.world.packages.combat.sets;

import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.combat.CombatConstants;
import com.ruse.world.packages.combat.sets.impl.DeathSet;
import com.ruse.world.packages.combat.sets.impl.RuthlessGladiator;
import com.ruse.world.packages.combat.sets.impl.TarnArmor;
import com.ruse.world.packages.combat.sets.impl.WarlordSet;

/**
 * To be redone, done crapply
 */
public class SetBonuses {

    public static SetBonus checkBonus(Player player){
        if(CombatConstants.waeringRuthlessGladiator(player)) {
            return new RuthlessGladiator();
        } else if(CombatConstants.wearingTarnMaster(player)){
            return new TarnArmor();
        } else if(CombatConstants.wearingDeathArmor(player)){
            return new DeathSet();
        } else if(CombatConstants.wearingWarlordArmor(player)){
            return new WarlordSet();
        }
        return null;
    }
}
