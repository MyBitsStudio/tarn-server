package com.ruse.world.content.bosses.counter;

import com.ruse.model.Position;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.SimpleScript;

public class CounterBoss extends Boss {
    public CounterBoss() {
        super(906, new Position(3019, 2765), true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new SimpleScript();
    }
}
