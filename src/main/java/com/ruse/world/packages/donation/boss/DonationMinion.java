package com.ruse.world.packages.donation.boss;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.npc.NPC;

public class DonationMinion extends NPC {
    public DonationMinion(Position pos) {
        super(586, pos, false);
        this.getDefinition().setAggressive(true);
        this.getDefinition().setMulti(true);
        this.getDefinition().setRespawn(-1);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new MinionCombat();
    }


}
