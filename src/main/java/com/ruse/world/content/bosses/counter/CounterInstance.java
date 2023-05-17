package com.ruse.world.content.bosses.counter;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.bosses.BossInstance;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.player.Player;

public class CounterInstance extends BossInstance {

    private static int TOKEN_ID = 13650, TOKEN_AMOUNT = 10;

    private int ticks = 0;
    public CounterInstance(Player p, Boss boss) {
        super(p, RegionInstanceType.COUNTER_BOSS, boss);
    }

    @Override
    public void process(){
        super.process();

        checkTokens();
    }

    private void checkTokens(){
        ticks++;

        if(ticks % 65 == 0){
            if(getOwner().getInventory().contains(TOKEN_ID, TOKEN_AMOUNT)){
                getOwner().getInventory().delete(TOKEN_ID, TOKEN_AMOUNT);
                getOwner().sendMessage("@blu@You have been charged @red@" + TOKEN_AMOUNT + " @blu@tokens for your instance.");
            } else {
                dispose();
            }
        }

        if(ticks % (65 * 1000) == 0){
            getOwner().sendMessage("@red@Your instance has expired.");
            dispose();
        }
    }

    public void start(){

        if(getOwner().getInventory().contains(TOKEN_ID, TOKEN_AMOUNT)){
            getOwner().getInventory().delete(TOKEN_ID, TOKEN_AMOUNT);
            getOwner().sendMessage("@blu@You have been charged " + TOKEN_AMOUNT + " tokens for your instance.");
        } else {
            dispose();
            return;
        }
        super.start();

        World.register(getBoss());
        add(getBoss());

        getOwner().setRegionInstance(this);

        getOwner().getPacketSender().sendInterfaceRemoval();
        getOwner().moveTo(new Position(3019, 2767));

        getOwner().getPacketSender().sendMessage("@red@Your instance has started.");
    }


}
