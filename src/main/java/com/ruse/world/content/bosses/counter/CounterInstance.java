package com.ruse.world.content.bosses.counter;

import com.ruse.GameSettings;
import com.ruse.model.Locations;
import com.ruse.model.Position;
import com.ruse.world.World;
import com.ruse.world.content.bosses.Boss;
import com.ruse.world.content.bosses.BossInstance;
import com.ruse.world.content.instanceMananger.InstanceData;
import com.ruse.world.content.transportation.TeleportHandler;
import com.ruse.world.entity.impl.player.Player;

public class CounterInstance extends BossInstance {

    private final static int TOKEN_ID = 13650, TOKEN_AMOUNT = 10;

    private final CounterBoss[] bosses = new CounterBoss[4];
    private int index;
    private int ticks = 0;
    public CounterInstance(Player p) {
        super(p, Locations.Location.ASTA_NPC, null);
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
                dispose();
            }
        }

        if(ticks % (135 * 1000) == 0){
            getOwner().sendMessage("@red@Your instance has expired.");
            dispose();
        }
    }

    @Override
    public void dispose(){
        for(CounterBoss boss : bosses){
            if(boss != null){
                remove(boss);
            }
        }
        super.dispose();
    }

    public void start(){

        if(getOwner().getInventory().contains(TOKEN_ID, TOKEN_AMOUNT)){
            getOwner().getInventory().delete(TOKEN_ID, TOKEN_AMOUNT);
            getOwner().sendMessage("@blu@You have been charged @red@" + 100 + " @blu@tokens for your instance.");
        } else {
            dispose();
            return;
        }
        super.start();

        moveTo(new Position(3019, 2762, getOwner().getIndex() * 4));
        add(getOwner());

        getOwner().getPacketSender().sendMessage("@red@Your instance has started.");

        Position[] pos = {
                new Position(3019, 2765, getOwner().getIndex() * 4), new Position(3023, 2762, getOwner().getIndex() * 4),
                new Position(3019, 2758, getOwner().getIndex() * 4), new Position(3016, 2762, getOwner().getIndex() * 4)
        };

        for(int i = 0; i < 4; i++){
            bosses[i] = new CounterBoss(pos[i]);
            bosses[i].setSpawnedFor(getOwner());
            add(bosses[i]);
        }

    }


}
