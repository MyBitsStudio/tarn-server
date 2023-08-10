package com.ruse.world.packages.combat.sets.impl;

import com.ruse.world.packages.combat.sets.SetBonus;
import com.ruse.world.packages.combat.sets.SetPerk;

public class RuthlessGladiator extends SetBonus {

    @Override
    public double meleeDamage(){
        return 0.10;
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
        return SetPerk.AOE_3;
    }

    @Override
    public String name(){
        return "Gladiator";
    }
}
