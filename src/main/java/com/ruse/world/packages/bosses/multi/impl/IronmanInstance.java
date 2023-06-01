package com.ruse.world.packages.bosses.multi.impl;

import com.ruse.model.Position;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.bosses.SpecialBossInstance;
import com.ruse.world.packages.instances.InstanceManager;

public class IronmanInstance extends SpecialBossInstance {
    public IronmanInstance(Player p, int npcId, int spawn, int cap) {
        super(p, npcId, spawn, cap);
    }

    @Override
    public void process(){
        super.process();

        if(!getOwner().getGameMode().isIronman()){
            InstanceManager.getManager().dispose(getOwner());
        }
    }

    @Override
    public void start(){

        moveTo(getOwner(), new Position(3025, 2768));
        add(getOwner());

        getOwner().getPacketSender().sendMessage("@blu@[INSTANCE] Your instance has started.");

        Position[] pos = {
                new Position(3019, 2765), new Position(3023, 2762),
                new Position(3019, 2758), new Position(3016, 2762)
        };

        spawnAll(pos);

    }


}
