package com.ruse.world.packages.bosses.single.varthramoth;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.bosses.single.zernath.ZernathInstance;
import com.ruse.world.packages.combat.scripts.npc.single.VarthCombat;
import com.ruse.world.packages.combat.scripts.npc.single.ZernathCombat;

public class VarthBoss extends Boss {
    public VarthBoss(int z) {
        super(3016, new Position(3029, 5231, z), false);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new VarthCombat();
    }

    @Override
    public void onDeath(){
        ((VarthInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered Varthramoth.");
    }
}