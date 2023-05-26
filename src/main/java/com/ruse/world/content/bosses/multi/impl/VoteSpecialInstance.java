package com.ruse.world.content.bosses.multi.impl;

import com.ruse.model.Position;
import com.ruse.world.content.bosses.SpecialBossInstance;
import com.ruse.world.entity.impl.player.Player;

public class VoteSpecialInstance extends SpecialBossInstance {

    private final static int TOKEN_ID = 23205, TOKEN_AMOUNT = 1;

    private int ticks = 0;
    public VoteSpecialInstance(Player p, int npcId, int spawn, int cap) {
        super(p, npcId, spawn, cap);
    }

    @Override
    public void process(){
        super.process();

        checkTokens();
    }

    private void checkTokens(){
        ticks++;
        if(ticks % 160 == 0){
            if(getOwner().getInventory().contains(TOKEN_ID, TOKEN_AMOUNT)){
                getOwner().getInventory().delete(TOKEN_ID, TOKEN_AMOUNT);
                getOwner().sendMessage("@blu@You have been charged @red@" + TOKEN_AMOUNT + " @blu@Voting Tickets for your instance.");
            } else {
                dispose(getOwner());
            }
        }

        if(ticks % (160 * 100) == 0){
            getOwner().sendMessage("@red@Your instance has expired.");
            dispose(getOwner());
        }
    }

    @Override
    public void start(){

        moveTo(getOwner(), new Position(3019, 2762));
        add(getOwner());

        getOwner().getPacketSender().sendMessage("@blu@[INSTANCE] Your instance has started.");

        Position[] pos = {
                new Position(3019, 2765), new Position(3023, 2762)
        };

        spawnAll(pos);

    }


}
