package com.ruse.world.content.bosses.multi.impl;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.bosses.BossInstance;
import com.ruse.world.content.bosses.SpecialBossInstance;
import com.ruse.world.content.instanceMananger.InstanceData;
import com.ruse.world.content.instances.InstanceManager;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.player.Player;

public class CounterInstance extends SpecialBossInstance {

    private final static int TOKEN_ID = 13650, TOKEN_AMOUNT = 10;

    private int ticks = 0;
    public CounterInstance(Player p, int npcId, int spawn, int cap) {
        super(p, npcId, spawn, cap);
    }

    @Override
    public void process(){
        super.process();

        checkTokens();
    }

    private void checkTokens(){
        ticks++;
        if(ticks % 135 == 0){
            if(getOwner().getInventory().contains(TOKEN_ID, TOKEN_AMOUNT)){
                getOwner().getInventory().delete(TOKEN_ID, TOKEN_AMOUNT);
                getOwner().sendMessage("@blu@You have been charged @red@" + TOKEN_AMOUNT + " @blu@tokens for your instance.");
            } else {
                InstanceManager.getManager().dispose(getOwner());
            }
        }

        if(ticks % (135 * 1000) == 0){
            getOwner().sendMessage("@red@Your instance has expired.");
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
