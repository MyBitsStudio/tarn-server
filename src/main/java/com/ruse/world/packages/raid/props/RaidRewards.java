package com.ruse.world.packages.raid.props;

import com.ruse.model.Item;
import com.ruse.util.Misc;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.raid.Raid;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class RaidRewards {

    protected Raid raid;
    protected List<Player> claimed = new CopyOnWriteArrayList<>();

    public RaidRewards(Raid raid){
        this.raid = raid;
    }

    public abstract int[] keys();
    public abstract void claim(Player player, Item key);

        public void claimRewards(Player player){
        if(raid.getFinished().get()){
            if(claimed.contains(player)){
                player.sendMessage("You have already claimed your reward!");
                return;
            }
            claimed.add(player);

            int chance = Misc.inclusiveRandom(0, 326);

            Item key = chance >= 311 ? new Item(keys()[2], 1) : chance >= 282 ? new Item(keys()[1], 1) : new Item(keys()[0], 1);

//            if(raid.getParty().getKeyWithHighestValue().equals(player.getUsername())){
//                if(Misc.random(5) == 0){
//                    key.setAmount(2);
//                }
//            }

            claim(player, key);

            sendInterface(player);

        }

    }

    private void sendInterface(Player player){

    }
}
