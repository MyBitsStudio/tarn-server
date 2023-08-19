package com.ruse.world.entity.impl.player.timers.impl;

import com.ruse.model.Animation;
import com.ruse.model.Timer;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;

@Getter
public abstract class PotionTimer extends Timer {

    private int tick = 0;

    private final Player player;
    public PotionTimer(Player player, long length) {
        super(length);
        this.player = player;
    }

    @Override
    public void onExecute(){
        if(getAnimation() != null)
            getPlayer().performAnimation(getAnimation());

        if(getEffectTimer() != null){
            player.getPacketSender().sendEffectTimerSeconds(getEffectTimer().getTime(), getEffectTimer());
        }

        applyEffect();
    }

    @Override
    public void onTick() {
        tick++;
        if(tick % 100 == 0){
            applyEffect();
        }
    }

    @Override
    public void onEnd(){
        if(getEffectTimer() != null){
            player.getPacketSender().sendEffectTimerSeconds(0, getEffectTimer());
        }
        finish();
    }

    public abstract void finish();

    public abstract void applyEffect();
    public abstract EffectTimer getEffectTimer();
    public abstract Animation getAnimation();
}
