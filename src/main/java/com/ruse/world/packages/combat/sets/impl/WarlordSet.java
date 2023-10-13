package com.ruse.world.packages.combat.sets.impl;

import com.ruse.world.packages.combat.sets.SetBonus;
import com.ruse.world.packages.combat.sets.SetPerk;

public class WarlordSet extends SetBonus {

    @Override
    public double meleeDamage(){
        return 1.10;
    }

    @Override
    public double mageDamage(){
        return 1.0;
    }

    @Override
    public double rangeDamage(){
        return 1.0;
    }

    @Override
    public SetPerk perk(){
        return SetPerk.LIFE_STEALER;
    }

    @Override
    public String name(){
        return "Warlord";
    }
}
