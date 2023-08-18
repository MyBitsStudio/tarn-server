package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.Animation;
import com.ruse.model.Skill;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class InfinitePrayer2 extends PotionTimer {

    public InfinitePrayer2(){
        super(null, 0);
    }
    public InfinitePrayer2(Player player) {
        super(player, DAYS);
    }

    @Override
    public void applyEffect() {
        getPlayer().getSkillManager().setCurrentLevel(Skill.PRAYER,
                getPlayer().getSkillManager().getCurrentLevel(Skill.PRAYER)
                        + 50);
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
    public void onEnd() {
        getPlayer().sendMessage("Your prayer potion has ran out.");
    }

    @Override
    public String getName() {
        return "inf-pray-2";
    }
}
