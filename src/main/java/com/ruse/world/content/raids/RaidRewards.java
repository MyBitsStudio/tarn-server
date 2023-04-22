package com.ruse.world.content.raids;

import com.ruse.world.entity.impl.player.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class RaidRewards {

    protected final Raid raid;
    protected List<Player> claimed = new ArrayList<>();

    public RaidRewards(Raid raid){
        this.raid = raid;
    }

    public abstract void claim(Player player);

    public void claimRewards(Player player){
        if(raid.isFinished()){
            if(claimed.contains(player)){
                player.sendMessage("You have already claimed your reward!");
                return;
            }
            claimed.add(player);
            claim(player);
        } else {
            player.sendMessage("You can only claim your reward after the raid has finished!");
        }
    }
}
