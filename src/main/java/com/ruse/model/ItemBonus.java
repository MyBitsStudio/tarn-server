package com.ruse.model;

import com.ruse.model.projectile.ItemEffect;
import com.ruse.util.Misc;

public class ItemBonus {

    private ItemEffect effect;
    private ItemRarity rarity;

    private int bonus = 0;

    public ItemBonus(){
        this.effect = ItemEffect.NOTHING;
        this.rarity = ItemRarity.COMMON;
    }

    public ItemBonus(ItemEffect effect){
        this.effect = effect;
        this.bonus = Misc.inclusiveRandom(effect.getLowBonus(), effect.getHighBonus());
        this.rarity = effect.getRarity();
    }

    public ItemBonus(ItemRarity rarity, ItemEffect effect){
        this.effect = effect;
        if(effect == ItemEffect.NOTHING) {
            this.rarity = rarity;
        } else {
            this.bonus = Misc.inclusiveRandom(effect.getLowBonus(), effect.getHighBonus());
            this.rarity = effect.getRarity();
        }
    }

    public ItemBonus(ItemRarity rarity, ItemEffect effect, int bonus){
        this.effect = effect;
        this.rarity = rarity;
        this.bonus = bonus;
    }

    public ItemBonus(ItemEffect effect, int bonus){
        this.effect = effect;
        this.bonus = bonus;
        if(effect == ItemEffect.NOTHING) {
            this.rarity = ItemRarity.COMMON;
        } else {
            this.rarity = effect.getRarity();
        }
    }

    public ItemEffect getEffect(){ return this.effect;}

    public int getBonus(){ return this.bonus;}

    public ItemRarity getRarity(){ return this.rarity;}

    public void setBonus(int bonus){ this.bonus = bonus;}

    public void setRarity(ItemRarity rarity){ this.rarity = rarity;}

    public void setEffect(ItemEffect effect){
        this.effect = effect;
        if(effect == ItemEffect.NOTHING) {
            this.rarity = ItemRarity.COMMON;
        } else {
            this.bonus = Misc.inclusiveRandom(effect.getLowBonus(), effect.getHighBonus());
            this.rarity = effect.getRarity();
        }
    }

    public void setEffect(ItemEffect effect, int bonus){
        this.effect = effect;
        this.bonus = bonus;
        if(effect == ItemEffect.NOTHING) {
            this.rarity = ItemRarity.COMMON;
        } else {
            this.rarity = effect.getRarity();
        }
    }
}
