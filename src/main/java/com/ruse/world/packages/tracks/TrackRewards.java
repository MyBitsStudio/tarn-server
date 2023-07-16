package com.ruse.world.packages.tracks;

import com.ruse.world.entity.impl.player.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public abstract class TrackRewards {

    protected List<ProgressReward> rewards = new CopyOnWriteArrayList<>();

    public abstract void loadRewards();

    public void claimRewards(Player player, int position, boolean premium, boolean premiumUnlock){
        for(ProgressReward reward : rewards){
            if(reward.getLevel() <= position){
                if(!reward.isClaimed()){
                    if(player.getInventory().getFreeSlots() >= 1){
                        player.getInventory().add(reward.getItem(), reward.getAmount());
                        reward.claim();
                    } else {
                        player.sendMessage("You have unlocked a reward, but your inventory is full.");
                    }
                }
            }
            if(premium){
                if(premiumUnlock){
                    if(reward.getLevel() <= position){
                        if(!reward.isPremiumClaimed()){
                            if(player.getInventory().getFreeSlots() >= 1){
                                player.getInventory().add(reward.getPremiumItem(), reward.getPremiumAmount());
                                reward.claimPremium(player);
                            } else {
                                player.sendMessage("You have unlocked a premium reward, but your inventory is full.");
                            }
                        }
                    }
                }
            }
        }
    }

}
