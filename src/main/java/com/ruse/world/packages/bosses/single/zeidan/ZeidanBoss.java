package com.ruse.world.packages.bosses.single.zeidan;

import com.ruse.model.Position;
import com.ruse.world.content.combat.strategy.CombatStrategy;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.Boss;
import com.ruse.world.packages.bosses.single.varthramoth.VarthInstance;
import com.ruse.world.packages.combat.scripts.npc.master.ZeidanCombat;
import com.ruse.world.packages.combat.scripts.npc.single.VarthCombat;

public class ZeidanBoss extends Boss {
    public ZeidanBoss(int z) {
        super(3010, new Position(2580, 4515, z), true);
        setAggressive(true);
    }

    @Override
    public CombatStrategy determineStrategy() {
        return new ZeidanCombat();
    }

    @Override
    public void onDeath(Player player){
        super.onDeath(player);
        ((ZeidanInstance)this.getInstance()).getOwner().sendMessage("@blu@You have made it through and conquered Zeidan.");
    }
}
