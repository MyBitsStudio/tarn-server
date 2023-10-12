package com.ruse.world.packages.event.impl.props;

import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.content.combat.strategy.impl.Exoden;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.bosses.single.exodon.ExodonInstance;
import com.ruse.world.packages.combat.drops.DropManager;
import com.ruse.world.packages.combat.scripts.npc.DefaultMonsterScript;

public class HallowEvent extends Boss {
    public HallowEvent(int id, Position position) {
        super(id, position, false);
        setEventBoss(true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new DefaultMonsterScript();
    }

    @Override
    public void onDeath(Player player){
        super.onDeath(player);
        for(Player players : World.getPlayers()) {
            if (players == null || !players.isRegistered())
                continue;
            if (players.getBossPlugin() == null)
                continue;
            if (players.getBossPlugin().getDamage(this.getDefinition().getName()) < 1000)
                continue;
            players.getBossPlugin().setDamage(this.getDefinition().getName(), 0L);
            DropManager.getManager().sendDrop(this, players, 1.0);
        }

        World.handler.getEvent("halloween-spawn").onDeath(player, this.getId());
    }

    @Override
    public void onDamage(Player player, long damage){
        player.getBossPlugin().addDamage(this.getDefinition().getName(), damage);
    }
}
