package com.ruse.world.entity.impl.player.timers.impl.scroll;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DoubleDDRHour extends PotionTimer {

    public DoubleDDRHour(){
        super(null, 0);
    }

    public DoubleDDRHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "DoubleDDRH";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("double-ddr", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("double-ddr", true);
    }

    @Override
    public EffectTimer getEffectTimer() {
        return EffectTimer.X2_DDR_1HR;
    }

    @Override
    public Animation getAnimation() {
        return null;
    }
}
