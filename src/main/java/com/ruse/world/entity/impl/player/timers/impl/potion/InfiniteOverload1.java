package com.ruse.world.entity.impl.player.timers.impl.potion;

import com.ruse.model.*;
import com.ruse.world.content.Consumables;
import com.ruse.world.content.EffectTimer;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.entity.impl.player.timers.impl.PotionTimer;
import com.ruse.world.packages.skills.S_Skills;

import java.util.Objects;

public class InfiniteOverload1 extends PotionTimer {

    private int tick = -1;
    public InfiniteOverload1(){
        super(null, 0);
    }
    public InfiniteOverload1(Player player) {
        super(player, DAYS);
    }

    @Override
    public void applyEffect() {
        if(tick == -1){
            tick = 0;
            getPlayer().performAnimation(new Animation(3170));
            getPlayer().dealDamage(new Hit(100, Hitmask.RED, CombatIcon.NONE));
            Consumables.levelIncrease(getPlayer(), Skill.ATTACK, 130);
            Consumables.levelIncrease(getPlayer(), Skill.STRENGTH, 130);
            Consumables.levelIncrease(getPlayer(), Skill.DEFENCE, 130);
            Consumables.levelIncrease(getPlayer(), Skill.RANGED, 130);
            Consumables.levelIncrease(getPlayer(), Skill.MAGIC, 130);
        }
        tick++;
        switch(tick){
            case 300 -> getPlayer().getPacketSender().sendMessage("@red@Your Overload's effect is about to run out.");
            case 400 ->{
                getPlayer().getPacketSender().sendMessage("@red@Your Overload's effect has run out.");
                for (int i = 0; i < 7; i++) {
                    if (i == 3 || i == 5)
                        continue;
                    getPlayer().getNewSkills().setCurrentLevel(Objects.requireNonNull(S_Skills.forId(i)), getPlayer().getNewSkills().getMaxLevel(Objects.requireNonNull(S_Skills.forId(i))), true);
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
        getPlayer().getPacketSender().sendMessage("@red@Your Overload's effect has run out.");
    }

    @Override
    public String getName() {
        return "inf-over-1";
    }
}
