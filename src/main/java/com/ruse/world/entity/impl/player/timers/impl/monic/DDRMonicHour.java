package com.ruse.world.entity.impl.player.timers.impl.monic;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DDRMonicHour extends PotionTimer {

    public DDRMonicHour(){
        super(null, 0);
    }

    public DDRMonicHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "MonicDDRH";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("monic-dr", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("monic-dr", true);
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
