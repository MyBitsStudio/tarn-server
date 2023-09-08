package com.ruse.world.entity.impl.player.timers.impl.monic;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DDDRMonicHour extends PotionTimer {

    public DDDRMonicHour(){
        super(null, 0);
    }

    public DDDRMonicHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "MonicDDDRH";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("monic-ddr", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("monic-ddr", true);
    }

    @Override
    public EffectTimer getEffectTimer() {
        return null;
    }

    @Override
    public Animation getAnimation() {
        return null;
    }
}
