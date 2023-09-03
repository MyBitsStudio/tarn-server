package com.ruse.world.packages.bosses.special;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.SpecialBossInstance;

public class IronmanInstance extends SpecialBossInstance {
    public IronmanInstance(Player p, int npcId, int spawn, int cap) {
        super(p, npcId, spawn, cap);
    }

    @Override
    public void start(){
        moveTo(getOwner(), new Position(3015, 2758));
        add(getOwner());

        getOwner().getPacketSender().sendMessage("@blu@[INSTANCE] Your instance has started.");

        Position[] pos = {
                new Position(3019, 2765), new Position(3023, 2762),
                new Position(3019, 2758), new Position(3016, 2762)
        };

        spawnAll(pos);

    }


}
