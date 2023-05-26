package com.ruse.world.content.bosses.multi;

import com.ruse.model.Position;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.bosses.MultiBossInstance;
import com.ruse.world.entity.impl.player.Player;

public class MultiBoss extends Boss {

    private final Player player;
    public MultiBoss(int id, Position position, boolean respawns, Player player) {
        super(id, position, respawns);
        this.player = player;
        scale();
    }

    @Override
    public void scale(){
        int diff = Integer.parseInt(player.getVariables().getInterfaceSettings()[2]);
        if(diff == 0)
            return;
        long hp = getConstitution();
        setConstitution(hp * (diff * 2L));
        getDefinition().setHitpoints(hp * (diff * 2L));
        getDefinition().setMaxHit((int) (getDefinition().getMaxHit() * (1 + (.2 * diff))));
        getDefinition().setAttackSpeed((int) (getDefinition().getAttackSpeed() * (1 - (.2 * diff))));
        getDefinition().setAttackBonus((int) (getDefinition().getAttackBonus() * (1 + (.2 * diff))));
        getDefinition().setDefenceMage((int) (getDefinition().getDefenceMage() * (1 + (.2 * diff))));
        getDefinition().setDefenceMelee((int) (getDefinition().getDefenceMelee() * (1 + (.2 * diff))));
        getDefinition().setDefenceRange((int) (getDefinition().getDefenceRange() * (1 + (.2 * diff))));
        getDefinition().setMaxHit((int) (getDefinition().getMaxHit() * (1 + (.3 * diff))));
    }

}
