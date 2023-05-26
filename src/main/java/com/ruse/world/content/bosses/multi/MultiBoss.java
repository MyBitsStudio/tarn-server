package com.ruse.world.content.bosses.multi;

import com.ruse.model.Position;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.entity.impl.npc.NPC;
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
        setConstitution((long) (hp * ( 1 + (diff * .7))));
        getDefinition().setHitpoints((long) (hp * ( 1 + (diff * .7))));
        getDefinition().setMaxHit((int) (getDefinition().getMaxHit() * (1 + (.2 * diff))));
        getDefinition().setAttackSpeed((int) (getDefinition().getAttackSpeed() * (1 - (.2 * diff))));
        getDefinition().setAttackBonus((int) (getDefinition().getAttackBonus() * (1 + (.3 * diff))));
        getDefinition().setDefenceMage((int) (getDefinition().getDefenceMage() * (1 + (.4 * diff))));
        getDefinition().setDefenceMelee((int) (getDefinition().getDefenceMelee() * (1 + (.4 * diff))));
        getDefinition().setDefenceRange((int) (getDefinition().getDefenceRange() * (1 + (.4 * diff))));
    }

}
