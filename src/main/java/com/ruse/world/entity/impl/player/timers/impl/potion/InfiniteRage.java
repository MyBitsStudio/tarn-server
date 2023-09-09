package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.*;
import com.ruse.world.content.Consumables;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;

public class InfiniteRage extends PotionTimer {

    private int tick = -1;
    public InfiniteRage(){
        super(null, 0);
    }
    public InfiniteRage(Player player) {
        super(player, DAYS * 7);
    }

    @Override
    public void applyEffect() {
        if(tick == -1){
            tick = 0;
            getPlayer().performAnimation(new Animation(3170));
            getPlayer().dealDamage(new Hit(100, Hitmask.RED, CombatIcon.NONE));
        }
        Consumables.levelIncrease(getPlayer(), Skill.ATTACK, 190);
        Consumables.levelIncrease(getPlayer(), Skill.STRENGTH, 190);
        Consumables.levelIncrease(getPlayer(), Skill.DEFENCE, 190);
        Consumables.levelIncrease(getPlayer(), Skill.RANGED, 190);
        Consumables.levelIncrease(getPlayer(), Skill.MAGIC, 190);
        tick++;
        switch(tick){
            case 300 -> getPlayer().getPacketSender().sendMessage("@red@Your Rages's effect is about to run out.");
            case 400 ->{
                getPlayer().getPacketSender().sendMessage("@red@Your Rage's effect has run out.");
                for (int i = 0; i < 7; i++) {
                    if (i == 3 || i == 5)
                        continue;
                    getPlayer().getSkillManager().setCurrentLevel(Skill.forId(i), getPlayer().getSkillManager().getMaxLevel(i));
                }
                stop();
            }
        }
    }

    @Override
    public EffectTimer getEffectTimer() {
        return EffectTimer.T1_INF_OVERLOAD;
    }

    @Override
    public Animation getAnimation() {
        return new Animation(829);
    }


    @Override
    public void finish() {
        getPlayer().getPacketSender().sendMessage("@red@Your Rage's effect has run out.");
    }

    @Override
    public String getName() {
        return "inf-rage-1";
    }
}
