package com.ruse.world.packages.bosses.multi;

import com.ruse.model.Position;
import com.ruse.world.packages.bosses.MultiBossInstance;
import com.ruse.world.packages.donation.FlashDeals;
import com.ruse.world.packages.instances.InstanceManager;
import com.ruse.world.entity.impl.player.Player;

public class MultiBossFlashInstance extends MultiBossInstance {


    public MultiBossFlashInstance(Player p, int npcId, int amount, long time) {
        super(p, npcId, amount, time);
    }

    @Override
    public void process(){
        super.process();

//        if(!FlashDeals.getDeals().isActive()){
//            InstanceManager.getManager().dispose(getOwner());
//        }
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
