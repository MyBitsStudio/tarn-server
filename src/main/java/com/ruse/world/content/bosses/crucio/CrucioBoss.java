package com.ruse.world.content.bosses.crucio;

import com.ruse.model.Position;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.combat.strategy.CombatStrategy;

public class CrucioBoss extends Boss {

    public CrucioBoss(int z) {
        super(589, new Position(3029, 5231, z), false);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new CrucioCombat();
    }

    @Override
    public void onDeath(){
        this.getInstance().remove(this);
        ((CrucioInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered Crucio.");
    }

}
