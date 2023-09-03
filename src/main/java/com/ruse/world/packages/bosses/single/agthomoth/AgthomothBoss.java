package com.ruse.world.packages.bosses.single.agthomoth;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.combat.scripts.npc.single.AgthomothCombat;

public class AgthomothBoss extends Boss {
    public AgthomothBoss(int z) {
        super(3013, new Position(3029, 5231, z), true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new AgthomothCombat();
    }

    @Override
    public void onDeath(){
        ((AgthomothInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered Agthomoth.");
    }
}
