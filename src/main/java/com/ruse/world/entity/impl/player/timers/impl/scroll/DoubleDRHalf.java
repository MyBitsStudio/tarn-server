package com.ruse.world.entity.impl.player.timers.impl.scroll;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DoubleDRHalf extends PotionTimer {

    public DoubleDRHalf(){
        super(null, 0);
    }

    public DoubleDRHalf(Player player) {
        super(player, MINUTES * 30);
    }

    @Override
    public String getName() {
        return "DoubleDR30";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("double-dr", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("double-dr", true);
    }

    @Override
    public EffectTimer getEffectTimer() {
        return EffectTimer.X2_DR_30MIN;
    }

    @Override
    public Animation getAnimation() {
        return null;
    }
}
