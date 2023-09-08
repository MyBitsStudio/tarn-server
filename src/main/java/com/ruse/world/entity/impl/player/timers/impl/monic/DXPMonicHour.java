package com.ruse.world.entity.impl.player.timers.impl.monic;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class DXPMonicHour extends PotionTimer {

    public DXPMonicHour(){
        super(null, 0);
    }

    public DXPMonicHour(Player player) {
        super(player, HOURS);
    }

    @Override
    public String getName() {
        return "MonicXpH";
    }

    @Override
    public void finish() {
        getPlayer().getPacketSender().sendWalkableInterface(48400, false);
        getPlayer().getVariables().setSetting("monic-xp", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getPacketSender().sendWalkableInterface(48400, true);
        getPlayer().getVariables().setSetting("monic-xp", true);
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
