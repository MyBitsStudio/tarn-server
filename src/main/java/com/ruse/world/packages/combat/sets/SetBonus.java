package com.ruse.world.packages.combat.sets;

public abstract class SetBonus {

    public double dropChance(){
        return 0.0;
    }

    public double doubleDropChance(){
        return 0.0;
    }

    public double meleeDamage(){
        return 0.0;
    }

    public double mageDamage(){
        return 0.0;
    }

    public double rangeDamage(){
        return 0.0;
    }

    public SetPerk perk(){
        return null;
    }

    public String name(){
        return "";
    }
}
