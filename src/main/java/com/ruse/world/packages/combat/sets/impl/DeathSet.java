package com.ruse.world.packages.combat.sets.impl;

import com.ruse.world.packages.combat.sets.SetBonus;
import com.ruse.world.packages.combat.sets.SetPerk;

public class DeathSet extends SetBonus {

    @Override
    public double meleeDamage(){
        return 1.10;
    }

    @Override
    public double mageDamage(){
        return 1.10;
    }

    @Override
    public double rangeDamage(){
        return 1.10;
    }

    @Override
    public SetPerk perk(){
        return SetPerk.LIFE_BRINGER;
    }

    @Override
    public String name(){
        return "Death";
    }
}
