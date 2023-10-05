package com.ruse.world.entity.impl.player.timers.impl;

import com.ruse.model.Animation;
import com.ruse.model.Timer;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.skills.SkillingTimer;
import lombok.Getter;

@Getter
public abstract class SkillTimer extends Timer {

    private int tick = 0;

    private Player player;
    public SkillTimer(Player player, long length) {
        super(length);
        this.player = player;
    }

    public void setPlayer(Player player) {
    	this.player = player;
    }

    @Override
    public void onExecute(){
        if(getAnimation() != null)
            getPlayer().performAnimation(getAnimation());

        if(getSkillingTimer() != null){
            player.getPacketSender().sendSkillingTimerSeconds((int) getLength(), getSkillingTimer());
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
        if(getSkillingTimer() != null){
            player.getPacketSender().sendSkillingTimerSeconds(0, getSkillingTimer());
        }
        finish();
    }

    public abstract void finish();

    public abstract void applyEffect();
    public abstract SkillingTimer getSkillingTimer();
    public abstract Animation getAnimation();
}
