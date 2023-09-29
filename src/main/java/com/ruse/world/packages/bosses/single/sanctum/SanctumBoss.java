package com.ruse.world.packages.bosses.single.sanctum;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.bosses.single.agthomoth.AgthomothInstance;
import com.ruse.world.packages.combat.scripts.npc.single.SanctumCombat;
import com.ruse.world.packages.combat.scripts.npc.single.ZernathCombat;

public class SanctumBoss extends Boss {
    public SanctumBoss(int z) {
        super(9017, new Position(2014, 4509, z), true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new SanctumCombat();
    }

    @Override
    public void onDeath(Player player){
        super.onDeath(player);
        ((SanctumInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered Sanctum Golem.");
    }
}
