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
        setConstitution(hp * (1 + diff));
        setAtkBoost((int) ((getDefinition().getAttackBonus() * (1 + (diff * .3))) - getDefinition().getAttackBonus()));
        setDefBoost((int) ((getDefinition().getDefenceMage() * (1 + (diff * .3))) - getDefinition().getDefenceMage()));
        setSpeedBoost((int) ((getDefinition().getAttackSpeed() * (1 + (diff * .1))) - getDefinition().getAttackSpeed()));
        setMaxHitBoost((int) ((getDefinition().getMaxHit() * (1 + (diff * .3))) - getDefinition().getMaxHit()));
    }

}
