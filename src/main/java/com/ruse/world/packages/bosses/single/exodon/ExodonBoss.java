package com.ruse.world.packages.bosses.single.exodon;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.Exoden;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.bosses.single.sanctum.SanctumInstance;
import com.ruse.world.packages.combat.scripts.npc.single.SanctumCombat;

public class ExodonBoss extends Boss {
    public ExodonBoss(int z) {
        super(12239, new Position(3029, 5231, z), true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new Exoden();
    }

    @Override
    public void onDeath(){
        ((ExodonInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered Exodon.");
    }
}
