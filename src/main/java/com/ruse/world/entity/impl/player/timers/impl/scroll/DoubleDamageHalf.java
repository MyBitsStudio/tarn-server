package com.ruse.world.entity.impl.player.timers.impl.scroll;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DoubleDamageHalf extends PotionTimer {

    public DoubleDamageHalf(){
        super(null, 0);
    }

    public DoubleDamageHalf(Player player) {
        super(player, MINUTES * 30);
    }

    @Override
    public String getName() {
        return "DoubleDMG30";
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
        return EffectTimer.X2_DMG_30MIN;
    }

    @Override
    public Animation getAnimation() {
        return null;
    }
}
