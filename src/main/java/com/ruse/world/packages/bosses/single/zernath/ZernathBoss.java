package com.ruse.world.packages.bosses.single.zernath;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.bosses.single.agthomoth.AgthomothInstance;
import com.ruse.world.packages.combat.scripts.npc.single.AgthomothCombat;
import com.ruse.world.packages.combat.scripts.npc.single.ZernathCombat;

public class ZernathBoss extends Boss {
    public ZernathBoss(int z) {
        super(3831, new Position(3029, 5231, z), true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new ZernathCombat();
    }

    @Override
    public void onDeath(Player player){
        super.onDeath(player);
        ((ZernathInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered Zernath.");
    }
}
