package com.ruse.world.entity.impl.player.timers.impl.scroll;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DoubleDRHour extends PotionTimer {

    public DoubleDRHour(){
        super(null, 0);
    }

    public DoubleDRHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "DoubleDRH";
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
        return EffectTimer.X2_DR_1HR;
    }

    @Override
    public Animation getAnimation() {
        return null;
    }
}
