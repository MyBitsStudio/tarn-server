package com.ruse.world.packages.slot;

import com.ruse.util.Misc;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
public enum SlotEffect {

    NOTHING(SlotRarity.NONE, -1),

    DROP_RATE_LOW(SlotRarity.UNCOMMON, 23150, 5, 20),
    DOUBLE_XP(SlotRarity.UNCOMMON, 23151),
    DOUBLE_SLAYER_TICK(SlotRarity.UNCOMMON, 25000),

    DOUBLE_DROP(SlotRarity.RARE, 23152, 5, 20),
    DROP_RATE_MED(SlotRarity.RARE, 23153,25, 50),

    MULTI_KILLS(SlotRarity.LEGENDARY, 23154, 1, 3),
    DOUBLE_CASH(SlotRarity.LEGENDARY, 23155),
    ALL_DAMAGE(SlotRarity.LEGENDARY, 23156, 1, 3),

    AOE_EFFECT(SlotRarity.MYTHICAL, 23157, 1, 4),
    DROP_RATE_HIGH(SlotRarity.MYTHICAL, 23158, 50, 99),
    TRIPLE_CASH(SlotRarity.MYTHICAL, 23159),
    MULTI_SHOT(SlotRarity.MYTHICAL, 23160, 2, 3),

    FIREWALL(SlotRarity.GODLY, 25001)

    ;

    private final SlotRarity rarity;
    private final int[] ranges;
    private final int itemId;
    SlotEffect(SlotRarity rarity, int itemId, int... ranges){
        this.rarity = rarity;
        this.itemId = itemId;
        this.ranges = ranges;
    }

    public static SlotEffect[] uncommons(){
        return Arrays.stream(SlotEffect.values()).anyMatch(effect ->
                effect.getRarity() == SlotRarity.UNCOMMON) ? Arrays.stream(SlotEffect.values()).filter(effect -> effect.getRarity() == SlotRarity.UNCOMMON).toArray(SlotEffect[]::new) : null;
    }

    public static SlotEffect[] rares(){
        return Arrays.stream(SlotEffect.values()).anyMatch(effect ->
                effect.getRarity() == SlotRarity.RARE) ? Arrays.stream(SlotEffect.values()).filter(effect -> effect.getRarity() == SlotRarity.RARE).toArray(SlotEffect[]::new) : null;
    }

    public static SlotEffect[] godly(){
        return Arrays.stream(SlotEffect.values()).anyMatch(effect ->
                effect.getRarity() == SlotRarity.GODLY) ? Arrays.stream(SlotEffect.values()).filter(effect -> effect.getRarity() == SlotRarity.GODLY).toArray(SlotEffect[]::new) : null;
    }

    public static SlotEffect[] legendaries(){
        return Arrays.stream(SlotEffect.values()).anyMatch(effect ->
                effect.getRarity() == SlotRarity.LEGENDARY) ? Arrays.stream(SlotEffect.values()).filter(effect -> effect.getRarity() == SlotRarity.LEGENDARY).toArray(SlotEffect[]::new) : null;
    }

    public static SlotEffect[] mythicals(){
        return Arrays.stream(SlotEffect.values()).anyMatch(effect ->
                effect.getRarity() == SlotRarity.MYTHICAL) ? Arrays.stream(SlotEffect.values()).filter(effect -> effect.getRarity() == SlotRarity.MYTHICAL).toArray(SlotEffect[]::new) : null;
    }

    public static boolean isPerkItem(int itemId){
        return Arrays.stream(SlotEffect.values()).anyMatch(effect -> effect.getItemId() == itemId);
    }

    public static SlotEffect forItemId(int itemId){
        return Arrays.stream(SlotEffect.values()).filter(i -> i.itemId == itemId).findFirst().orElse(NOTHING);
    }

    public boolean isWeapon(){
        return this == SlotEffect.AOE_EFFECT ||
                this == SlotEffect.MULTI_SHOT ||
                this == SlotEffect.FIREWALL;
    }

    public static SlotEffect lowBonus(){
        int random = Misc.random(100);

        if(random == 37)
           return Misc.random(Objects.requireNonNull(mythicals()));
         else if(random >= 89 && random <= 92)
            return Misc.random(Objects.requireNonNull(legendaries()));
         else if(random >= 31 && random <= 47)
            return Misc.random(Objects.requireNonNull(rares()));
         else
            return Misc.random(Objects.requireNonNull(uncommons()));

    }

    public static SlotEffect medBonus(){
        int random = Misc.random(100);

        if(random >= 42 && random <= 46)
           return Misc.random(Objects.requireNonNull(mythicals()));
         else if(random >= 79 && random <= 92)
            return Misc.random(Objects.requireNonNull(legendaries()));
         else if(random >= 31 && random <= 61)
            return Misc.random(Objects.requireNonNull(rares()));
         else
            return Misc.random(Objects.requireNonNull(uncommons()));

    }

    public static SlotEffect highBonus(){
        int random = Misc.random(100);

        if(random >= 42 && random <= 54)
           return Misc.random(Objects.requireNonNull(mythicals()));
         else if(random >= 68 && random <= 93)
            return Misc.random(Objects.requireNonNull(legendaries()));
         else if(random >= 19 && random <= 61)
            return Misc.random(Objects.requireNonNull(rares()));
         else if(random == 13)
            return Misc.random(Objects.requireNonNull(godly()));
         else
            return Misc.random(Objects.requireNonNull(uncommons()));

    }

}

