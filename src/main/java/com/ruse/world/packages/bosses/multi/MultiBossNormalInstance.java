package com.ruse.world.packages.bosses.multi;

import com.ruse.model.Position;
import com.ruse.world.packages.bosses.MultiBossInstance;
import com.ruse.world.entity.impl.player.Player;

public class MultiBossNormalInstance extends MultiBossInstance {

    public MultiBossNormalInstance(Player p, int npcId,
                                   int amount, int cap){
        super(p, npcId, amount, cap);
    }

    @Override
    public void start(){
        moveTo(getOwner(), new Position(3025, 2768));
        add(getOwner());

        getOwner().getPacketSender().sendMessage("@blu@[INSTANCE] Your instance has started.");

        Position[] pos = {
                new Position(3013, 2764), new Position(3013, 2761), new Position(3013, 2758), new Position(3013, 2755),
                new Position(3016, 2764), new Position(3016, 2761), new Position(3016, 2758), new Position(3016, 2755),
                new Position(3019, 2764), new Position(3019, 2761), new Position(3019, 2758), new Position(3019, 2755),
                new Position(3022, 2764), new Position(3022, 2761), new Position(3022, 2758), new Position(3022, 2755),
        };

        spawnAll(pos);
    }



}
