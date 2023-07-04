package com.ruse.world.packages.slot;

import com.ruse.util.Misc;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SlotBonus {

    private final SlotEffect effect;

    private int bonus = 0;

    public SlotBonus(){
        this.effect = SlotEffect.NOTHING;
    }

    public SlotBonus(@NotNull SlotEffect effect){
        this.effect = effect;
        this.bonus = Misc.inclusiveRandom(effect.getRanges()[0], effect.getRanges()[1]);
    }

    public SlotBonus(SlotEffect effect, int bonus){
        this.effect = effect;
        this.bonus = bonus;
    }


}
