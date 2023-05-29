package com.ruse.world.content.bosses.multi;

import com.ruse.model.Position;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.bosses.multi.strategy.HardCombatStrategy;
import com.ruse.world.content.bosses.multi.strategy.InsaneCombatStrategy;
import com.ruse.world.content.combat.strategy.CombatStrategies;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.SimpleScript;
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
        setConstitution((long) (hp * (2.1 + diff)));
        setAtkBoost((int) ((getDefinition().getAttackBonus() * (1 + (diff * .8))) - getDefinition().getAttackBonus()));
        setDefBoost((int) ((getDefinition().getDefenceMage() * (1 + (diff * .7))) - getDefinition().getDefenceMage()));
        setSpeedBoost((int) ((getDefinition().getAttackSpeed() * (1 + (diff * .4))) - getDefinition().getAttackSpeed()));
        setMaxHitBoost((int) ((getDefinition().getMaxHit() * (1 + (diff * .8))) - getDefinition().getMaxHit()));

    }

    @Override
    public CombatStrategy determineStrategy() {
        int diff = Integer.parseInt(player.getVariables().getInterfaceSettings()[2]);
        if(diff <= 1)
            return CombatStrategies.getStrategy(getId());
        else if(diff == 2)
            return new HardCombatStrategy();
        else
            return new InsaneCombatStrategy();
    }

}
