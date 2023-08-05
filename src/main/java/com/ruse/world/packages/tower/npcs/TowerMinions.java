package com.ruse.world.packages.tower.npcs;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.npc.NPC;
import com.ruse.world.entity.impl.player.Player;

public class TowerMinions extends NPC {
    public TowerMinions(int id, Position position) {
        super(id, position, false);
    }

    public void buff(int[] buffs){

    }

    @Override
    public void onDeath(){
        Player player = getCombatBuilder().getLastAttacker().asPlayer();
        if(player != null){
            player.getTower().getInstance().check(player);
        }
        super.onDeath();
    }


}
