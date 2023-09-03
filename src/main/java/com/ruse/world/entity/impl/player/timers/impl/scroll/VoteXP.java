package com.ruse.world.entity.impl.player.timers.impl.scroll;

import com.ruse.model.Animation;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class VoteXP extends PotionTimer {

    public VoteXP(){
        super(null, 0);
    }

    public VoteXP(Player player, int length) {
        super(player, MINUTES * length);
    }

    @Override
    public String getName() {
        return "VoteXP";
    }

    @Override
    public void finish() {
        getPlayer().getPacketSender().sendWalkableInterface(48400, false);
        getPlayer().getVariables().setSetting("vote-xp", false);
    }

    @Override
    public void applyEffect() {
        getPlayer().getPacketSender().sendWalkableInterface(48400, true);
        getPlayer().getVariables().setSetting("vote-xp", true);
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
