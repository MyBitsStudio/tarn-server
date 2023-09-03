package com.ruse.world.entity.impl.player.timers.impl.scroll;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class VoteDR extends PotionTimer {

    public VoteDR(){
        super(null, 0);
    }

    public VoteDR(Player player, int length) {
        super(player, MINUTES * length);
    }

    @Override
    public String getName() {
        return "VoteDR";
    }

    @Override
    public void finish() {
        getPlayer().getVariables().setSetting("vote-dr", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getVariables().setSetting("vote-dr", true);
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
