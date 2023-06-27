package com.ruse.world.packages.tower;

import com.ruse.model.Item;
import com.ruse.model.container.ItemContainer;
import com.ruse.model.container.StackType;
import com.ruse.world.entity.impl.player.Player;
import com.ruse.world.packages.tower.props.Tower;
import com.ruse.world.packages.tower.props.TowerTierRewards;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TowerProgress {

    private Player player;
    private int tier = 0, level = 0, lastLevel = 20, lastTier = 4;
    private ItemContainer rewards;

    public TowerProgress(Player player) {
        this.player = player;
        this.rewards = new ItemContainer(player) {
            @Override
            public int capacity() {
                return 28;
            }

            @Override
            public StackType stackType() {
                return StackType.STACKS;
            }

            @Override
            public ItemContainer refreshItems() {
                return this;
            }

            @Override
            public ItemContainer full() {
                return this;
            }
        };
    }

    public void progress(){
        if(level == lastLevel){
            if(tier == lastTier){
                sendFinish();
                return;
            }
            sendTier();
            return;
        }
        sendProgress();
    }

    private void sendFinish(){
        player.sendMessage("You have completed the tower!");
    }

    private void sendTier(){
        TowerTierRewards rewards = TowerTierRewards.get(tier);
        if(rewards == null){
            return;
        }
        player.sendMessage("You have progressed to tier " + tier + "!");
        for(Item reward : rewards.getRewards()){
            this.rewards.add(reward);
            player.sendMessage("You have received " + reward.getAmount() + " " + reward.getDefinition().getName() + "!");
        }
        tier++;
        level = 0;
    }

    private void sendProgress(){
        Tower tower = Tower.get(tier, level);
        if(tower == null){
            return;
        }
        player.sendMessage("You have progressed to tier " + tier + " level " + level + "!");
        rewards.add(tower.getReward());
        player.sendMessage("You have received " + tower.getReward().getAmount() + " " + tower.getReward().getDefinition().getName() + "!");
        level++;
    }

}
