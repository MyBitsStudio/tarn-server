package com.ruse.world.content.bosses.counter;

import com.ruse.model.Position;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.Galvek;
import com.ruse.world.content.combat.strategy.impl.SimpleScript;

public class CounterBoss extends Boss {
    public CounterBoss(Position pos) {
        super(595, pos, true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new Galvek();
    }
}
