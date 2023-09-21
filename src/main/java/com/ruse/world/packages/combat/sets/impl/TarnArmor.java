package com.ruse.world.packages.combat.sets.impl;

import com.ruse.world.packages.combat.sets.SetBonus;
import com.ruse.world.packages.combat.sets.SetPerk;

public class TarnArmor extends SetBonus {

    @Override
    public double meleeDamage(){
        return 1.50;
    }

    @Override
    public double mageDamage(){
        return 1.50;
    }

    @Override
    public double rangeDamage(){
        return 1.50;
    }

    @Override
    public SetPerk perk(){
        return SetPerk.FIREWALL;
    }

    @Override
    public String name(){
        return "Tarn";
    }
}
