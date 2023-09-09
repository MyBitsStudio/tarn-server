package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.Animation;
import com.ruse.model.Skill;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class InfinitePrayer3 extends PotionTimer {

    public InfinitePrayer3(){
        super(null, 0);
    }
    public InfinitePrayer3(Player player) {
        super(player, DAYS);
    }

    @Override
    public void applyEffect() {
        getPlayer().getSkillManager().setCurrentLevel(Skill.PRAYER,
                getPlayer().getSkillManager().getCurrentLevel(Skill.PRAYER)
                        + 160);
        if (getPlayer().getSkillManager().getCurrentLevel(Skill.PRAYER) > getPlayer().getSkillManager()
                .getMaxLevel(Skill.PRAYER))
            getPlayer().getSkillManager().setCurrentLevel(Skill.PRAYER,
                    getPlayer().getSkillManager().getMaxLevel(Skill.PRAYER));
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
        return "inf-pray-3";
    }
}
