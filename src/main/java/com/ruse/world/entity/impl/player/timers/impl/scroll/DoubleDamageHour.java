package com.ruse.world.entity.impl.player.timers.impl.scroll;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DoubleDamageHour extends PotionTimer {

    public DoubleDamageHour(){
        super(null, 0);
    }

    public DoubleDamageHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "DoubleDMGH";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("double-damage", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("double-damage", true);
    }

    @Override
    public EffectTimer getEffectTimer() {
        return EffectTimer.X2_DMG_1HR;
    }

    @Override
    public Animation getAnimation() {
        return null;
    }
}
