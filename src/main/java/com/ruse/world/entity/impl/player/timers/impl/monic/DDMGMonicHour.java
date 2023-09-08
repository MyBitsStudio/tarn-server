package com.ruse.world.entity.impl.player.timers.impl.monic;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DDMGMonicHour extends PotionTimer {

    public DDMGMonicHour(){
        super(null, 0);
    }

    public DDMGMonicHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "MonicDMGH";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("monic-damage", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("monic-damage", true);
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
