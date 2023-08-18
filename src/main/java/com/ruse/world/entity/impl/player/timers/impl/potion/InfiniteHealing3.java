package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class InfiniteHealing3 extends PotionTimer {

    public InfiniteHealing3(){
        super(null, 0);
    }
    public InfiniteHealing3(Player player) {
        super(player, DAYS);
    }

    @Override
    public void applyEffect() {
        getPlayer().heal(200);
    }

    @Override
    public EffectTimer getEffectTimer() {
        return null;
    }

    @Override
    public Animation getAnimation() {
        return new Animation(829);
    }

    @Override
    public void onEnd() {
        getPlayer().sendMessage("Your healing potion has ran out.");
    }

    @Override
    public String getName() {
        return "inf-heal-3";
    }
}
