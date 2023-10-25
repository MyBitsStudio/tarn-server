package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.Animation;
import com.ruse.model.Skill;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;
import com.ruse.world.packages.skills.S_Skills;

public class InfinitePrayer1 extends PotionTimer {

    public InfinitePrayer1(){
        super(null, 0);
    }
    public InfinitePrayer1(Player player) {
        super(player, DAYS);
    }

    @Override
    public void applyEffect() {
        if (getPlayer().getNewSkills().getCurrentLevel(S_Skills.PRAYER) < getPlayer().getNewSkills()
                .getMaxLevel(S_Skills.PRAYER)) {
            getPlayer().getNewSkills().setCurrentLevel(S_Skills.PRAYER,
                    getPlayer().getNewSkills().getCurrentLevel(S_Skills.PRAYER) + 50, true);
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
        return "inf-pray-1";
    }
}
