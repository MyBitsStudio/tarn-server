package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.Animation;
import com.ruse.model.Skill;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;
import com.ruse.world.packages.skills.S_Skills;

public class InfinitePrayer extends PotionTimer {

    public InfinitePrayer(){
        super(null, 0);
    }
    public InfinitePrayer(Player player) {
        super(player, DAYS);
    }

    @Override
    public void applyEffect() {
        if (getPlayer().getNewSkills().getCurrentLevel(S_Skills.PRAYER) < getPlayer().getNewSkills()
                .getMaxLevel(S_Skills.PRAYER)) {
            getPlayer().getNewSkills().setCurrentLevel(S_Skills.PRAYER,
                    getPlayer().getNewSkills().getCurrentLevel(S_Skills.PRAYER) + 500, true);
        }
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
    public void finish() {
        getPlayer().sendMessage("Your prayer potion has ran out.");
    }
    @Override
    public String getName() {
        return "inf-pray";
    }
}
