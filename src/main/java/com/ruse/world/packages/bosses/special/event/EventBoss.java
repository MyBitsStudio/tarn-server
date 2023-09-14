package com.ruse.world.packages.bosses.special.event;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.Crimson;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.bosses.single.agthomoth.AgthomothInstance;
import com.ruse.world.packages.combat.scripts.npc.single.AgthomothCombat;

public class EventBoss extends Boss {
    public EventBoss(int z) {
        super(6430, new Position(3029, 5231, z), true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new Crimson();
    }

    @Override
    public void onDeath(Player player){
        super.onDeath(player);
        ((EventInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered the Event Boss.");
    }
}
