package com.ruse.world.entity.impl.player.timers.impl.monic;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class PrayerMonicHour extends PotionTimer {

    public PrayerMonicHour(){
        super(null, 0);
    }

    public PrayerMonicHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "MonicPrayerH";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("monic-prayer", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("monic-prayer", true);
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
